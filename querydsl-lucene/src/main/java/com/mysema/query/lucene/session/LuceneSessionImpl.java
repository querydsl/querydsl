package com.mysema.query.lucene.session;

import java.io.File;
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
import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.LuceneSerializer;

/**
 * Lucene session implementation
 * 
 * @author laimw
 * 
 */
public class LuceneSessionImpl implements LuceneSession {

    private final Logger logger = LoggerFactory.getLogger(LuceneSessionImpl.class);

    private final Directory directory;

    private final AtomicReference<IndexSearcher> searcher = new AtomicReference<IndexSearcher>();

    private final LuceneSerializer serializer = new LuceneSerializer(true, true);

    public LuceneSessionImpl(Directory directory) {
        this.directory = directory;
    }

    public LuceneSessionImpl(String indexPath) throws IOException {
        File folder = new File(indexPath);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Could not create directory: "
                    + folder.getAbsolutePath());
        }

        try {
            directory = new SimpleFSDirectory(folder);
        } catch (IOException e) {
            logger.error("Could not create lucene directory to "
                    + folder.getAbsolutePath());
            throw e;
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

    private IndexSearcher getSearcher() throws IOException {
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
            is.getIndexReader().decRef();

            createNewSearcher(is);
        }

        return searcher.get();
    }

    @Override
    public <T> T query(QueryCallback<T> callback) {
        try {
            IndexSearcher is = getSearcher();
            T results = null;
            try {
                // Incrementing the reference count
                is.getIndexReader().incRef();
                results = callback.query(new LuceneQuery(serializer, is));

            } finally {
                // Releasing the reference count
                // This can be the last to actually close the reader
                is.getIndexReader().decRef();
            }

            return results;
        } catch (IOException e) {
            throw new QueryException(e);
        }

    }

    @Override
    public void update(WriteCallback callback) {
        try {
            update(callback, false);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    private void update(WriteCallback callback, boolean create) throws IOException {
        IndexWriter writer = new IndexWriter(directory, new StandardAnalyzer(
                Version.LUCENE_CURRENT), create, MaxFieldLength.LIMITED);

        try {
            callback.write(writer);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                logger.error("Writer close failed", e);
                try {
                    if (IndexWriter.isLocked(directory)) {
                        IndexWriter.unlock(directory);
                    }
                } catch (IOException e1) {
                    logger.error("Lock release failed", e1);
                }
            }
        }

    }

    @Override
    public void updateNew(WriteCallback callback) {
        try {
            update(callback, true);
        } catch (IOException e) {
            throw new QueryException(e);
        }

    }

}
