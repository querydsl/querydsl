package com.mysema.query.lucene.session.impl;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneSessionFactory;

public class LuceneSessionFactoryImpl implements LuceneSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(LuceneSessionFactoryImpl.class);

    private final Directory directory;

    private final AtomicReference<LuceneSearcher> searcher = new AtomicReference<LuceneSearcher>();

    // private LuceneInternalsFactory factory = new LuceneInternalsFactory() {
    // IndexSearcher
    // }
    //
    // public static interface LuceneInternalsFactory {
    //
    // }

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

    }

    public LuceneSessionFactoryImpl(Directory directory) {
        this.directory = directory;
    }

    @Override
    public LuceneSession getCurrentSession() {
        return LuceneSessionHolder.getCurrentSession();
    }

    @Override
    public LuceneSession openSession(boolean readOnly) {
        return new LuceneSessionImpl(this, readOnly);
    }

    public LuceneWriterImpl getWriter(boolean createNew) {
        return new LuceneWriterImpl(directory, createNew);
    }

    public LuceneSearcher leaseSearcher() {
        try {
            if (searcher.get() == null) {
                createNewSearcher(null);
            }

            // Checking do we need to refresh the reader
            LuceneSearcher s = searcher.get();
            if (!s.isCurrent()) {
                // Underlying index has changed

                // Decreasing the reference counter so that
                // count can go to zero either here or
                // when final searcher has done it's job
                // This pairs with createNewSearcher incRef()
                try {
                    s.release();
                } catch (QueryException e) {
                    logger.error("Could not release index reader", e);
                }

                createNewSearcher(s);
            }

            // Incrementing reference as we lease this out
            // This pairs with closeReaders decRef()
            searcher.get().lease();

            return searcher.get();

        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    private LuceneSearcher createNewSearcher(LuceneSearcher expected) throws IOException {
        LuceneSearcher s = new LuceneSearcher(new IndexSearcher(directory));
        if (!searcher.compareAndSet(expected, s)) {
            // Some thread already created a new one so just close this
            s.close();
        } else {
            // Incrementing the reference count first time
            // We want to keep using the same reader until the index is changed
            s.lease();
        }
        return searcher.get();
    }

}
