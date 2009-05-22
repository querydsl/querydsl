/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import com.mysema.query.collections.IteratorSource;
import com.mysema.query.types.expr.Expr;

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
    
    private IteratorSource iteratorSource;
    
    protected Iterator<?>[] iterators;
    
    protected boolean[] lastEntry;
    
    protected final List<Expr<?>> sources = new ArrayList<Expr<?>>();
    
    protected Object[] values;
    
    public MultiIterator add(Expr<?> expr) {
        sources.add(expr);
        return this;
    }
    
    public boolean hasNext() {
        while (hasNext == null){
            produceNext();
        }
        return hasNext.booleanValue();                
    }

    /**
     * Initialize the MultiIterator instance
     * 
     * @param indexSupport
     * @return
     */
    public MultiIterator init(IteratorSource iteratorSource){
        this.iteratorSource = iteratorSource;
        this.iterators = new Iterator<?>[sources.size()];
        this.lastEntry = new boolean[iterators.length];
        this.values = new Object[iterators.length];
        return this;
    }
                
    public Object[] next() {
        while (hasNext == null){
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
            if (iterators[i] == null || (!iterators[i].hasNext() && i > 0)){
                iterators[i] = iteratorSource.getIterator(sources.get(i), values);
            }
            if (!iterators[i].hasNext()){
                hasNext = i == 0 ? Boolean.FALSE : null;
                return;    
            }
            values[i] = iterators[i].next();            
            lastEntry[i] = !iterators[i].hasNext();
            hasNext = Boolean.TRUE;
        }        
        index = iterators.length -1;
        while (lastEntry[index] && index > 0) index--;
    }
    
    public void remove() {
        // do nothing
    }   

}
