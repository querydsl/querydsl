package com.mysema.query.lucene.session.impl;

import java.io.IOException;

import javax.annotation.Nullable;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import com.mysema.query.QueryException;

/**
 * Simple wrapper to encapsulate searcher specific actions.
 * 
 * @author laim
 * 
 */
public class LuceneSearcher {

    private final IndexSearcher searcher;
    
    @Nullable
    private final ReleaseListener releaseListener;

    public LuceneSearcher(Directory directory, ReleaseListener releaseListener) {
        try {
            this.searcher = new IndexSearcher(directory);
            this.releaseListener = releaseListener;
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

    public void release() {
        if (releaseListener != null) {
            releaseListener.release(this);
        }
        try {
            searcher.getIndexReader().decRef();
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    public void lease() {
        if (releaseListener != null) {
            releaseListener.lease(this);
        }
        searcher.getIndexReader().incRef();
    }

    public IndexSearcher getIndexSearcer() {
        return searcher;
    }

}
