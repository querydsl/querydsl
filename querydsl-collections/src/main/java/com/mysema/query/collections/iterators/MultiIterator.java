package com.mysema.query.collections.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * MultiIterator provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MultiIterator extends IteratorBase<Object[]>{

    private final List<Iterable<?>> iterables = new ArrayList<Iterable<?>>();
    
    private Iterator<?>[] iterators;
    
    private Object[] values;
    
    private boolean[] lastEntry;
    
    private int index = 0;
        
    public boolean hasNext() {
        return iterators[index].hasNext();
    }

    public Object[] next() {
        for (int i = index; i < iterators.length; i++){
            values[i] = iterators[i].next();
            lastEntry[i] = !iterators[i].hasNext();
            if (!iterators[i].hasNext() && i > 0){     
                iterators[i] = iterables.get(i).iterator();                
            }            
        }        
        index = iterators.length -1;
        while (lastEntry[index] && index > 0) index--;
        return values;
    }
        
    public MultiIterator add(Iterable<?> iterable) {
        iterables.add(iterable);
        return this;
    }
    
    public MultiIterator add(Object... instances) {
        return add(Arrays.asList(instances));
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
   

}
