/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.apache.commons.collections15.iterators.UniqueFilterIterator;

import com.mysema.query.QueryMetadata;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractIterable<I,T> implements Iterable<T>{

    protected final ExprEvaluatorFactory evaluatorFactory;
    
    protected final IteratorFactory iteratorFactory;
    
    protected final QueryMetadata metadata;
    
    private final boolean forCount;
    
    public AbstractIterable(QueryMetadata metadata, 
            ExprEvaluatorFactory evaluatorFactory, 
            IteratorFactory iteratorFactory,
            boolean forCount) {
        this.metadata = metadata;
        this.evaluatorFactory = evaluatorFactory;
        this.iteratorFactory = iteratorFactory;
        this.forCount = forCount;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Iterator<T> iterator() {
        // from and where
        Iterator<I> it = initialIterator();
        if (it.hasNext()) {
            if (!forCount){
                // order
                if (!metadata.getOrderBy().isEmpty()){
                    it = orderedIterator(it);    
                }
                // projection
                Iterator<T> rv = projectedIterator(it);
                // distinct
                if (metadata.isDistinct()){
                    rv = distinctIterator(rv);
                }
                return rv;                
            }else{
                if (metadata.isDistinct()){
                    Iterator<T> rv = projectedIterator(it);
                    return distinctIterator(rv);
                }else{
                    return (Iterator<T>) it;
                }    
            }

        } else {
            return Collections.<T>emptyList().iterator();
        }
    }
    
    /**
     * @param it
     * @return
     */
    @SuppressWarnings("unchecked")
    private <RT> Iterator<RT> distinctIterator(Iterator<RT> it){
        boolean array = false;
        if (!metadata.getProjection().isEmpty() && metadata.getProjection().get(0).getType().isArray()){
            array = true;
        }else if (metadata.getProjection().isEmpty() && metadata.getJoins().size() > 1){
            array = true;
        }        
        if (array){
            return (Iterator<RT>)new FilterIterator<Object[]>((Iterator<Object[]>)it,
                    new Predicate<Object[]>() {     
                private Set<List<Object>> rows = new HashSet<List<Object>>();
                @Override
                public boolean evaluate(Object[] object) {
                    return rows.add(Arrays.asList(object));
                }
            });
        }else{
            return new UniqueFilterIterator<RT>(it);    
        }        
    }
    
    /**
     * creates the initial Iterator providing the filtered source content
     * 
     * @return
     */
    protected abstract Iterator<I> initialIterator();
            
    /**
     * reorders the filtered source Iterator based on the ordering criteria of the 
     * QueryMetadata
     * 
     * @param it
     * @return
     */
    protected abstract Iterator<I> orderedIterator(Iterator<I> it);
    
    /**
     * creates the projection based on the given Iterator and the projection definition of the 
     * QueryMetadata
     * 
     * @param it
     * @return
     */
    protected abstract Iterator<T> projectedIterator(Iterator<I> it);
  
}