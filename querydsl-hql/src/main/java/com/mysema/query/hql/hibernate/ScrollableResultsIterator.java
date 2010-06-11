/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.io.IOException;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

import org.hibernate.ScrollableResults;

import com.mysema.commons.lang.CloseableIterator;

/**
 * ScrollableResultsIterator is an CloseableIterator adapter for ScrollableResults
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class ScrollableResultsIterator<T> implements CloseableIterator<T> {

    private final ScrollableResults results;
    
    private final boolean asArray;
    
    @Nullable
    private Boolean hasNext;

    public ScrollableResultsIterator(ScrollableResults results) {
        this(results, false);
    }
    
    public ScrollableResultsIterator(ScrollableResults results, boolean asArray) {
        this.results = results;
        this.asArray = asArray;        
    }

    @Override
    public void close() throws IOException {
        try {
            results.close();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        if (hasNext == null){
            hasNext = results.next();
        }
        return hasNext;        
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        if (hasNext()){
            hasNext = null;
            if (asArray) {
                return (T) results.get();
            } else {
                return (T) results.get(0);
            }    
        }else{
            throw new NoSuchElementException();
        }        
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}