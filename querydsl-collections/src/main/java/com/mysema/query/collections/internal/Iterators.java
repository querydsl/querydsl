/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.iterators.FilterIterator;
import org.codehaus.janino.ExpressionEvaluator;

/**
 * Iterators provides
 *
 * @author tiwe
 * @version $Id$
 */
public class Iterators {
    private Iterators(){}
    
    public static class FilteringIterator<RT> extends FilterIterator<RT>{
        public FilteringIterator(Iterator<?> it,  final ExpressionEvaluator ev) {
            super((Iterator<RT>)it, new Predicate<RT>(){
                public boolean evaluate(RT object) {
                    try {
                        return (Boolean) ev.evaluate(new Object[]{object});
                    } catch (InvocationTargetException e) {
                        String error = "Caught " + e.getClass().getName();
                        throw new RuntimeException(error, e);
                    }
                }                
            });
        }        
            
    }
    
    public abstract static class ItBase<RT> implements Iterator<RT>{
        public void remove() {
        }
    }
    
    public static class ProjectingIterator<RT> extends WrappingIt<RT>{
        private ExpressionEvaluator ev;
        public ProjectingIterator(Iterator<?> it,  ExpressionEvaluator ev) {
            super(it);
            try {
                this.ev = ev;
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
            }
        }
        public RT next() {
            try {
                return (RT) ev.evaluate(new Object[]{nextFromOrig()});
            } catch (InvocationTargetException e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
            }
        }        
    }
    
    public abstract static class WrappingIt<RT> extends ItBase<RT>{
        private Iterator<?> it;  
        public WrappingIt(Iterator<?> it) {
            this.it = it;
        }
        public boolean hasNext() {
            return it.hasNext();
        }
        protected Object nextFromOrig() {
            return it.next();
        }
        
    }
}
