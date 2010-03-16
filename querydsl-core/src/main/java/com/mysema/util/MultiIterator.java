/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.Nullable;

/**
 * MultiIterator provides a cartesian view on the given iterators
 * 
 * <pre>
 * e.g. (1,2) and (100, 200, 300)
 * are expanded to (1, 100) (1, 200) (1, 300) (2, 100) (2, 200) (2, 300)
 * </pre>
 * 
 * @author tiwe
 * @version $Id$
 */
public class MultiIterator<T> implements Iterator<T[]> {

    @Nullable
    private Boolean hasNext;

    private int index = 0;

    private final List<Iterable<T>> iterables;

    private final List<Iterator<T>> iterators;

    private final boolean[] lastEntry;

    private final Object[] values;
    
    public MultiIterator(List<Iterable<T>> iterables){
        this.iterables = iterables;
        this.iterators = new ArrayList<Iterator<T>>(iterables.size()); 
        for (int i = 0; i < iterables.size(); i++){
            iterators.add(null);
        }
        this.lastEntry = new boolean[iterables.size()];
        this.values = new Object[iterables.size()];        
    }

    @Override
    public boolean hasNext() {
        while (hasNext == null) {
            produceNext();
        }
        return hasNext.booleanValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] next() {
        while (hasNext == null) {
            produceNext();
        }
        if (hasNext.booleanValue()) {
            hasNext = null;
            return (T[]) values.clone();
        } else {
            throw new NoSuchElementException();
        }
    }

    private void produceNext() {
        for (int i = index; i < iterables.size(); i++) {
            if (iterators.get(i) == null || (!iterators.get(i).hasNext() && i > 0)) {
                iterators.set(i, iterables.get(i).iterator());
            }
            if (!iterators.get(i).hasNext()) {
                hasNext = i == 0 ? Boolean.FALSE : null;
                return;
            }
            values[i] = iterators.get(i).next();
            lastEntry[i] = !iterators.get(i).hasNext();
            hasNext = Boolean.TRUE;
        }
        index = iterables.size() - 1;
        while (lastEntry[index] && index > 0){
            index--;
        }            
    }

    @Override
    public void remove() {
        // do nothing
    }

}
