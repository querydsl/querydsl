package com.mysema.query.lucene.session;



/**
 * General interface on using Lucene.
 * 
 * @author laimw
 */
public interface LuceneSession {

//    /**
//     * @return
//     */
//    LuceneQuery createQuery();
    
    /**
     * Lucene query callback for querying
     * 
     * @param callback
     * @return
     */
    <T> T query(QueryCallback<T> callback);

    /**
     * Creates a new index, adds updates to it and publishes the new index to
     * all readers after the callback finishes.
     * 
     * @param callback
     */
    void updateNew(WriteCallback callback);

    /**
     * Updates the current index and publishes it to all readers after the callback
     * finishes.
     * 
     * @param callback
     */
    void update(WriteCallback callback);

}
