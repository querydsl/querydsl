package com.mysema.query.lucene.session;

public interface LuceneSessionFactory {

    LuceneSession getCurrentSession();
    
    LuceneSession openSession(boolean readOnly);
    
}
