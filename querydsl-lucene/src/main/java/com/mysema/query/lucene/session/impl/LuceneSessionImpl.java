package com.mysema.query.lucene.session.impl;

import javax.annotation.Nullable;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.LuceneSerializer;
import com.mysema.query.lucene.TypedQuery;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneWriter;
import com.mysema.query.lucene.session.SessionClosedException;
import com.mysema.query.lucene.session.SessionReadOnlyException;

public class LuceneSessionImpl implements LuceneSession {

    private final boolean readOnly;

    private boolean closed = false;

    private final LuceneSessionFactoryImpl sessionFactory;

    @Nullable
    private LuceneSearcher searcher;

    @Nullable
    private FileLockingWriter writer;

    private final LuceneSerializer serializer = new LuceneSerializer(true, true);

    public LuceneSessionImpl(LuceneSessionFactoryImpl sessionFactory, boolean readOnly) {
        this.sessionFactory = sessionFactory;
        this.readOnly = readOnly;
    }

    @Override
    public LuceneQuery createQuery() {
        checkClosed();
        return new LuceneQuery(serializer, getSearcher().getIndexSearcer());
    }

    @Override
    public <T> TypedQuery<T> createQuery(Class<T> clazz) {
        checkClosed();
        return new TypedQuery<T>(serializer, getSearcher().getIndexSearcer(),
                                 sessionFactory.getDocumentToObjectTransformer(clazz));
    }

    @Override
    public LuceneWriter beginAppend() {
        checkClosed();
        return getWriter(false);
    }

    @Override
    public LuceneWriter beginReset() {
        checkClosed();
        return getWriter(true);
    }

    @Override
    public void close() {
        checkClosed();

        QueryException searcherException = null;

        if (searcher != null) {
            try {
                sessionFactory.release(searcher);
            } catch (QueryException e) {
                searcherException = e;
            }
        }

        if (writer != null) {
            sessionFactory.release(writer);
        }

        if (searcherException != null) {
            throw searcherException;
        }

        closed = true;
    }

    @Override
    public void flush() {
        checkClosed();

        if (writer == null) {
            return;
        }

        writer.commit();

        if (searcher != null) {
            sessionFactory.release(searcher);
        }
        searcher = null;

    }

    private LuceneSearcher getSearcher() {
        if (searcher == null) {
            searcher = sessionFactory.leaseSearcher();
        }
        return searcher;
    }

    private LuceneWriter getWriter(boolean createNew) {
        if (readOnly) {
            throw new SessionReadOnlyException("Read only session, cannot create writer");
        }

        if (writer == null) {
            writer = sessionFactory.leaseWriter(createNew);
        }

        return writer;
    }

    private void checkClosed() {
        if (closed) {
            throw new SessionClosedException("Session is closed");
        }
    }

}
