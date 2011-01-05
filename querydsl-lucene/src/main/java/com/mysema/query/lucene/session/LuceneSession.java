package com.mysema.query.lucene.session;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.TypedQuery;

public interface LuceneSession {

    /**
     * Creates a new query.
     * 
     * @return LuceneQuery instance
     * @throws SessionClosedException if session is closed
     */
    LuceneQuery createQuery();

    /**
     * Creates a new typed query.
     * 
     * @param <T> The result type
     * @param clazz The result class
     * @return A new typed query instance
     * @throws SessionClosedException if session is closed
     */
    <T> TypedQuery<T> createQuery(Class<T> clazz);

    /**
     * Adds documents to index. Creates a new index if the index is not
     * available.
     * 
     * @return LuceneWriter instance
     * @throws SessionReadOnlyException if session is opened in read-only mode
     * @throws SessionClosedException if session is closed
     */
    LuceneWriter beginAppend();

    /**
     * Resets the whole index and starts new writer. Old readers will still see
     * the index as it was when they were opened but new readers will see only
     * the data this writer has committed.
     * 
     * @return LuceneWriter instance
     * @throws SessionReadOnlyException if session is opened in read-only mode
     * @throws SessionClosedException if session is closed
     */
    LuceneWriter beginReset();

    /**
     * Flushes all data writer has changed. Queries created after this will see
     * the new index. Queries created before, will see the old snapshot.
     * 
     * @throws SessionClosedException if session is closed
     */
    void flush();

    /**
     * Closes the session. All writes are committed and resources are cleared.
     * This should always be called in finally block if this session was created
     * explicitly by calling session factory's openSession
     * 
     * @throws SessionClosedException if session is closed
     */
    void close();

}
