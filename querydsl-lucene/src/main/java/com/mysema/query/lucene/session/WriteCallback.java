package com.mysema.query.lucene.session;

import org.apache.lucene.index.IndexWriter;

/**
 * Callback which has Lucene Writer instance.
 * 
 * @author laimw
 *
 */
public interface WriteCallback {

    void write(IndexWriter writer);
    
}
