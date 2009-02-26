/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.mysema.query.grammar.types.Expr;

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
public class MultiIterator implements Iterator<Object[]>{

    private Boolean hasNext;
    
    protected int index = 0;
    
    private final List<Iterable<?>> iterables = new ArrayList<Iterable<?>>();
    
    protected Iterator<?>[] iterators;
    
    protected boolean[] lastEntry;
    
    protected Object[] values;
    
    public MultiIterator add(Expr<?> expr, final Iterable<?> iterable) {
        iterables.add(iterable);
        return this;
    }
    
    public boolean hasNext() {
        if (hasNext == null){
            produceNext();
        }
        return hasNext.booleanValue();                
    }

    public MultiIterator init(){
        iterators = new Iterator<?>[iterables.size()];
        for (int i = 0; i < iterators.length; i++){
            iterators[i] = iterables.get(i).iterator();
        }
        lastEntry = new boolean[iterators.length];
        values = new Object[iterators.length];
        return this;
    }
                
    public Object[] next() {
        if (hasNext == null){
            produceNext();
        }
        if (hasNext.booleanValue()){
            hasNext = null;
            return values.clone();
        }else{
            throw new NoSuchElementException();
        }
    }
    
    private void produceNext(){
        for (int i = index; i < iterators.length; i++){   
            if (!iterators[i].hasNext()){
                hasNext = Boolean.FALSE;
                return;
            }
            values[i] = iterators[i].next();
            lastEntry[i] = !iterators[i].hasNext();
            if (!iterators[i].hasNext() && i > 0){     
                iterators[i] = iterables.get(i).iterator();                
            }            
            hasNext = Boolean.TRUE;
        }        
        index = iterators.length -1;
        while (lastEntry[index] && index > 0) index--;
    }

    public void remove() {
        
    }   

}
