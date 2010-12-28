package com.mysema.query.lucene.session;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.LuceneWriter;

public interface LuceneSession {

    LuceneQuery createQuery();
    
    LuceneWriter beginAppend();
    
    LuceneWriter beginOverwrite();
    
    void flush();

    void close();

}
