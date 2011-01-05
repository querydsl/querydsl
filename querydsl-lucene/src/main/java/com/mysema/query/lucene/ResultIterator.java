/**
 * 
 */
package com.mysema.query.lucene;

import java.io.IOException;

import org.apache.commons.collections15.Transformer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.QueryException;

public final class ResultIterator<T> implements CloseableIterator<T> {
    
    private final ScoreDoc[] scoreDocs;
    
    private int cursor;
    
    private final Searcher searcher;
    
    private final Transformer<Document,T> transformer;

    public ResultIterator(ScoreDoc[] scoreDocs, int offset, Searcher searcher, Transformer<Document, T> transformer) {
        this.scoreDocs = scoreDocs;
        cursor = offset;
        this.searcher = searcher;
        this.transformer = transformer;
    }

    @Override
    public boolean hasNext() {
        return cursor != scoreDocs.length;
    }

    @Override
    public T next() {
        try {
            return transformer.transform(searcher.doc(scoreDocs[cursor++].doc));
        } catch (IOException e) {
            throw new QueryException(e);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        
    }

}