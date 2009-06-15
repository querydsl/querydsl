/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Iterator;

import com.mysema.query.types.expr.Expr;

/**
 * IteratorSource provides a Expr -> Iterator mapping
 * 
 * @author tiwe
 * @version $Id$
 */
public interface IteratorSource {

    /**
     * @param <A>
     * @param expr
     * @return
     */
    <A> Iterator<A> getIterator(Expr<A> expr);

    /**
     * @param <A>
     * @param expr
     * @param bindings
     * @return
     */
    <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings);

}
