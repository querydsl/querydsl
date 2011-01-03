package com.mysema.query.lucene.session.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneSessionFactory;
import com.mysema.query.lucene.session.SessionNotBoundException;

public class LuceneSessionFactoryImpl implements LuceneSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(LuceneSessionFactoryImpl.class);

    private final Directory directory;

    @Nullable
    private volatile LuceneSearcher searcher;

    ExecutorService searchUpdater = Executors.newSingleThreadExecutor();

    private final AtomicBoolean creatingNew = new AtomicBoolean(false);

    @Nullable
    private final ReleaseListener releaseListener;

    private long defaultLockTimeout = 2000;

    public LuceneSessionFactoryImpl(String indexPath) throws IOException {
        File folder = new File(indexPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create directory: " + folder.getAbsolutePath());
        }

        try {
            directory = new SimpleFSDirectory(folder);
        } catch (IOException e) {
            logger.error("Could not create lucene directory to " + folder.getAbsolutePath());
            throw e;
        }
        releaseListener = null;
    }

    public LuceneSessionFactoryImpl(Directory directory) {
        this.directory = directory;
        releaseListener = null;
    }

    public LuceneSessionFactoryImpl(Directory directory, ReleaseListener releaseListener) {
        this.directory = directory;
        this.releaseListener = releaseListener;
    }

    @Override
    public LuceneSession getCurrentSession() {
        if (!LuceneSessionHolder.isTransactionalScope()) {
            throw new SessionNotBoundException("There is no transactional scope");
        }

        if (!LuceneSessionHolder.hasCurrentSession(this)) {

            if (logger.isTraceEnabled()) {
                logger.trace("Binding new session to thread");
            }

            LuceneSession session = openSession(LuceneSessionHolder.getReadOnly());
            LuceneSessionHolder.setCurrentSession(this, session);
        }

        return LuceneSessionHolder.getCurrentSession(this);
    }

    @Override
    public LuceneSession openSession(boolean readOnly) {
        return new LuceneSessionImpl(this, readOnly);
    }

    public FileLockingWriter getWriter(boolean createNew) {
        return new FileLockingWriter(directory, createNew, defaultLockTimeout, releaseListener);
    }

    public LuceneSearcher leaseSearcher() {
        try {
            if (searcher == null) {
                synchronized (this) {
                    if (searcher == null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Creating first searcher");
                        }
                        searcher = createNewSearcher();
                    }
                }
            }
            if (creatingNew.compareAndSet(false, true)) {
                if (!searcher.isCurrent()) {
                    try {
                        // This release pairs with createNewSearcher lease
                        searcher.release();
                    } catch (QueryException e) {
                        logger.error("Could not release searcher", e);
                    }
                    searcher = createNewSearcher();
                }

                creatingNew.set(false);
            }

            // Incrementing reference as we lease this out
            // This pairs with LuceneSessions close
            searcher.lease();
            return searcher;

        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    private LuceneSearcher createNewSearcher() throws IOException {
        LuceneSearcher s = new LuceneSearcher(directory, releaseListener);
        if (logger.isDebugEnabled()) {
            logger.debug("Created searcher " + s);
        }
        // Increment the first time
        s.lease();
        return s;
    }

    public void setDefaultLockTimeout(long defaultLockTimeout) {
        this.defaultLockTimeout = defaultLockTimeout;
    }

}
