/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.QueryIndexSupport;
import com.mysema.query.types.expr.Expr;

/**
 * SimpleIteratorSource is the default implementation of the IndexSupport interface
 *
 * @see QueryIndexSupport
 *
 * @author tiwe
 * @version $Id$
 */
public class SimpleIteratorSource implements IteratorSource{
    
    private final Map<Expr<?>,Iterable<?>> exprToIt;
   
    public SimpleIteratorSource() {
        this(new HashMap<Expr<?>,Iterable<?>>());
    }
    
    public SimpleIteratorSource(Map<Expr<?>,Iterable<?>> exprToIt){
        this.exprToIt = Assert.notNull(exprToIt);
    }
    
    public SimpleIteratorSource add(Expr<?> expr, Iterable<?> it){
        this.exprToIt.put(Assert.notNull(expr), Assert.notNull(it));
        return this;
    }
   
    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }
       
}
