package com.mysema.query.lucene.session;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;

/**
 * Callback which has Lucene Writer instance.
 * 
 * @author laimw
 *
 */
public interface WriteCallback {

    void write(IndexWriter writer) throws CorruptIndexException, IOException;
    
}
