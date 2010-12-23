package com.mysema.query.lucene.session;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;

/**
 * General interface on using Lucene.
 * 
 * @author laimw
 */
public interface LuceneSession {

    /**
     * Lucene query callback for querying
     * 
     * @param clazz
     * @param callback
     * @return
     * @throws IOException
     * @throws CorruptIndexException
     */
    <T> T query(QueryCallback<T> callback) throws CorruptIndexException, IOException;

    /**
     * Creates a new index, adds updates to it and publishes the new index to
     * all readers after callback finishes.
     * 
     * @param callback
     * @throws IOException
     */
    void updateNew(WriteCallback callback) throws IOException;

    /**
     * Updates the current index and publishes it to all readers after callback
     * finishes.
     * 
     * @param callback
     * @throws IOException
     */
    void update(WriteCallback callback) throws IOException;

}
