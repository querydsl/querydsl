package com.mysema.query.lucene.session;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.LuceneSerializer;

public class LuceneSessionImpl implements LuceneSession {

    private boolean readOnly;
    
    private boolean closed = false;

    private LuceneSessionFactoryImpl sessionFactory;

    private IndexSearcher searcher;
    
    private IndexWriter writer;

    private LuceneSerializer serializer = new LuceneSerializer(true, true);

    public LuceneSessionImpl(LuceneSessionFactoryImpl sessionFactory,
                             boolean readOnly) {
        this.sessionFactory = sessionFactory;
        this.readOnly = readOnly;
    }

    @Override
    public LuceneQuery createQuery() {
        checkClosed();
        return new LuceneQuery(serializer, getSearcher());
    }

    private IndexSearcher getSearcher() {
        if(searcher == null) {
            searcher = sessionFactory.leaseSearcher();
        }
        return searcher;
    }
    
    @Override
    public IndexWriter createAppendWriter() {
        checkClosed();
        return createWriter(false);
    }

    @Override
    public IndexWriter createOverwriteWriter() {
        checkClosed();
        return createWriter(true);
    }

    private IndexWriter createWriter(boolean createNew) {
        if (readOnly) {
            throw new QueryException("Read only session, cannot create writer");
        }
        
        if (writer == null) {
            writer = sessionFactory.getWriter(createNew);
        }
        
        return writer;
    }

    @Override
    public void close() {
        checkClosed();
        sessionFactory.closeSession(this);
        closed = true;
    }
    
    @Override
    public void flush() {
        checkClosed();
        sessionFactory.flush(this);
    }
    
    public IndexSearcher getIndexSearcher() {
        return searcher;
    }
    
    public void removeIndexSearcher() {
        searcher = null;
    }
    
    public IndexWriter getIndexWriter() {
        return writer;
    }

    
    private void checkClosed() {
        if (closed) {
            throw new QueryException("Session is closed");
        }
    }

   

}
