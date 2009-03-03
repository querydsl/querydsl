/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * DefaultIteratorFactory is the default implementation of the IteratorFactory interface
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIteratorFactory implements IteratorFactory{
    
    private final Map<Expr<?>,Iterable<?>> exprToIt;
    
    public DefaultIteratorFactory(Map<Expr<?>,Iterable<?>> exprToIt){
        this.exprToIt = exprToIt;
    }

    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    public void init(List<Expr<?>> orderedSources, EBoolean condition) {
        // do nothing
        
    }

}
