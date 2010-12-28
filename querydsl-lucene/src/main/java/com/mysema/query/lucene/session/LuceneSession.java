package com.mysema.query.lucene.session;

import com.mysema.query.lucene.LuceneQuery;

public interface LuceneSession {

    LuceneQuery createQuery();
    
    LuceneWriter beginAppend();
    
    LuceneWriter beginOverwrite();
    
    void flush();

    void close();

}
