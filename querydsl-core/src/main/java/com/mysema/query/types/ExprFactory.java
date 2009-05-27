/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Expr;

/**
 * ExprFactory provides
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ExprFactory {

    /**
     * 
     * @param i
     * @return
     */
    Expr<Integer> createConstant(int i);

    /**
     * 
     * @param <A>
     * @param obj
     * @return
     */
    <A> Expr<Class<A>> createConstant(Class<A> obj);

    /**
     * 
     * @param <A>
     * @param obj
     * @return
     */
    <A> Expr<A> createConstant(A obj);

}
