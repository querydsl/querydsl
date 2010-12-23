package com.mysema.query.lucene.session;

import com.mysema.query.lucene.LuceneQuery;



/**
 * General interface on using Lucene.
 * 
 * @author laimw
 */
public interface LuceneSession {

    /**
     * Creates a new LuceneQuery
     * 
     * @return
     */
    LuceneQuery createQuery();
    
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
