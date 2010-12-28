package com.mysema.query.lucene.session;

import org.apache.lucene.index.IndexWriter;

import com.mysema.query.lucene.LuceneQuery;

public interface LuceneSession {

    LuceneQuery createQuery();
    
    IndexWriter createAppendWriter();
    
    IndexWriter createOverwriteWriter();
    
    void flush();

    void close();

}
