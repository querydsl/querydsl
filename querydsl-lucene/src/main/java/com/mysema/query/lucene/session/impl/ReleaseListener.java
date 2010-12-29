package com.mysema.query.lucene.session.impl;

/**
 * Helps to make sure the resources are released as they should be.
 * 
 * @author laim
 */
public interface ReleaseListener {
    
    void lease(LuceneSearcher searcher);
    
    void release(LuceneSearcher searcher);
    
    void close(LuceneWriterImpl writer);
    
}
