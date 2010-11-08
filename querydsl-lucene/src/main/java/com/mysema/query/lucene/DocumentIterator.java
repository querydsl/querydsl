/**
 * 
 */
package com.mysema.query.lucene;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.QueryException;

public final class DocumentIterator implements CloseableIterator<Document> {
    
    private final ScoreDoc[] scoreDocs;
    
    private int cursor;
    
    private final Searcher searcher;

    public DocumentIterator(ScoreDoc[] scoreDocs, int offset, Searcher searcher) {
        this.scoreDocs = scoreDocs;
        cursor = offset;
        this.searcher = searcher;
    }

    @Override
    public boolean hasNext() {
        return cursor != scoreDocs.length;
    }

    @Override
    public Document next() {
        try {
            return searcher.doc(scoreDocs[cursor++].doc);
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void remove() {
    }

    @Override
    public void close() {
        
    }

}