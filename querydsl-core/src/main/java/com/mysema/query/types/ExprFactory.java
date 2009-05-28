/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Expr;

/**
 * ExprFactory is a factory interface for EConstant instance creation
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {

    /**
     * Create a constant for the given integer
     * 
     * 
     * @param i
     * @return
     */
    Expr<Integer> createConstant(int i);

    /**
     * Create a constant for the given class
     * 
     * @param <A>
     * @param obj
     * @return
     */
    <A> Expr<Class<A>> createConstant(Class<A> obj);

    /**
     * Create a constant for the given object
     * 
     * @param <A>
     * @param obj
     * @return
     */
    <A> Expr<A> createConstant(A obj);

}
