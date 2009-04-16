/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Iterator;

import com.mysema.query.grammar.types.Expr;

/**
 * IteratorSource provides a Expr -> Iterator mapping
 *
 * @author tiwe
 * @version $Id$
 */
public interface IteratorSource {
    
    <A> Iterator<A> getIterator(Expr<A> expr);

    <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings);

}
