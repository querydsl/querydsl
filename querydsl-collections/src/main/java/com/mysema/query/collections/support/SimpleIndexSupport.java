/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysema.query.collections.IndexSupport;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * DefaultIndexSupport is the default implementation of the IndexSupport interface
 *
 * @see IndexSupport
 *
 * @author tiwe
 * @version $Id$
 */
public class SimpleIndexSupport implements IndexSupport{
    
    private Map<Expr<?>,Iterable<?>> exprToIt;
    
    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    public void init(Map<Expr<?>,Iterable<?>> exprToIt, JavaOps ops, List<? extends Expr<?>> sources, EBoolean condition) {
        // do nothing
    }
        
}
