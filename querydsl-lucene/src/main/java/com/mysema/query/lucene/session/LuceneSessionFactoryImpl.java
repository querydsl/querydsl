package com.mysema.query.lucene.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryException;

public class LuceneSessionFactoryImpl implements LuceneSessionFactory {

    private static final Logger logger = LoggerFactory.getLogger(LuceneSessionFactoryImpl.class);

    private final Directory directory;

    private final AtomicReference<IndexSearcher> searcher = new AtomicReference<IndexSearcher>();

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
        try {

            IndexWriter writer = null;

            if (createNew == false) {
                try {
                    writer =
                        new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), false,
                                        MaxFieldLength.LIMITED);

                } catch (FileNotFoundException e) {
                    // Convience to create a new index if it's not already
                    // existing
                    createNew = true;
                }
            }
            if (createNew == true) {
                writer =
                    new IndexWriter(directory, new StandardAnalyzer(Version.LUCENE_30), true,
                                    MaxFieldLength.LIMITED);
            }

            return new LuceneWriterImpl(writer);

        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public void flush(LuceneSessionImpl session) {
        LuceneWriterImpl writer = session.getWriter();
        if (writer == null) {
            return;
        }

        writer.commit();

        // Close the reader, so the next query will get fresh reader
        closeReader(session);
    }

    public IndexSearcher leaseSearcher() {
        try {
            if (searcher.get() == null) {
                createNewSearcher(null);
            }

            // Checking do we need to refresh the reader
            IndexSearcher is = searcher.get();
            if (!is.getIndexReader().isCurrent()) {
                // Underlying index has changed

                // Decreasing the reference counter so that
                // count can go to zero either here or
                // when final searcher has done it's job
                // This pairs with createNewSearcher incRef()
                try {
                    is.getIndexReader().decRef();
                } catch (IOException e) {
                    logger.error("Could not release index reader", e);
                }

                createNewSearcher(is);
            }

            // Incrementing reference as we lease this out
            // This pairs with closeReaders decRef()
            searcher.get().getIndexReader().incRef();

            return searcher.get();

        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    private IndexSearcher createNewSearcher(IndexSearcher expected) throws IOException {
        IndexSearcher is = new IndexSearcher(directory);
        if (!searcher.compareAndSet(expected, is)) {
            // Some thread already created a new one so just close this
            is.close();
        } else {
            // Incrementing the reference count first time
            // We want to keep using the same reader until the index is changed
            is.getIndexReader().incRef();
        }
        return searcher.get();
    }

    public void closeSession(LuceneSessionImpl session) {
        QueryException readerException = null;
        try {
            closeReader(session);
        } catch (QueryException e) {
            readerException = e;
        }

        closeWriter(session);

        if (readerException != null) {
            throw readerException;
        }

    }

    private void closeReader(LuceneSessionImpl session) {

        if (session.getIndexSearcher() == null) {
            return;
        }

        try {
            // Decrementing the reader, if this is last reference,
            // reader will be closed
            session.getIndexSearcher().getIndexReader().decRef();
            session.removeIndexSearcher();
        } catch (IOException e) {
            logger.error("Reader close failed", e);
            throw new QueryException("Reader close failed", e);
        }
    }

    private void closeWriter(LuceneSessionImpl session) {
        // Always close writer
        try {
            LuceneWriterImpl writer = session.getWriter();
            if (writer != null) {

                // TODO What would be best way to control this?
                writer.getIndexWriter().optimize();

                writer.getIndexWriter().close();
            }
        } catch (IOException e) {
            logger.error("Writer close failed", e);
            try {
                if (IndexWriter.isLocked(directory)) {
                    IndexWriter.unlock(directory);
                }
            } catch (IOException e1) {
                logger.error("Lock release failed", e1);
                throw new QueryException(e1);
            }
            throw new QueryException(e);
        }
    }

}
