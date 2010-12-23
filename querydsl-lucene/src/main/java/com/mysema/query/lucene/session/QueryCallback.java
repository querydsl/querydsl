package com.mysema.query.lucene.session;

import com.mysema.query.lucene.LuceneQuery;

/**
 * 
 * @author laimw
 * 
 */
public interface QueryCallback<T> {
    
    T query(LuceneQuery query);
    
}
