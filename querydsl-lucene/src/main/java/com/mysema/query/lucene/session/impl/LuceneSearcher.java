package com.mysema.query.lucene.session.impl;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import com.mysema.query.QueryException;

/**
 * Simple wrapper to encapsulate searcher specific actions.
 * 
 * @author laim
 * 
 */
public class LuceneSearcher implements Leasable {

    private final IndexSearcher searcher;

    public LuceneSearcher(Directory directory) {
        try {
            this.searcher = new IndexSearcher(directory);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public boolean isCurrent() {
        try {
            return searcher.getIndexReader().isCurrent();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void release() {
        try {
            searcher.getIndexReader().decRef();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void lease() {
        searcher.getIndexReader().incRef();
    }

    public IndexSearcher getIndexSearcer() {
        return searcher;
    }

}
