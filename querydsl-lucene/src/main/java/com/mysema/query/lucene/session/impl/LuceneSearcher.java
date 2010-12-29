package com.mysema.query.lucene.session.impl;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;

import com.mysema.query.QueryException;

/**
 * Simple wrapper to encapsulate searcher specific actions.
 * 
 * @author laim
 *
 */
public class LuceneSearcher {

    private final IndexSearcher searcher;

    public LuceneSearcher(IndexSearcher searcher) {
        this.searcher = searcher;
    }

    public boolean isCurrent() {
        try {
            return searcher.getIndexReader().isCurrent();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public void release() {
        try {
            searcher.getIndexReader().decRef();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public void lease() {
        searcher.getIndexReader().incRef();
    }

    public IndexSearcher getIndexSearcer() {
        return searcher;
    }

}
