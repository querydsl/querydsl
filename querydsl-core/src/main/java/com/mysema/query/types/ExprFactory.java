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

    Expr<Integer> createConstant(int i);

    <A> Expr<Class<A>> createConstant(Class<A> obj);

    <A> Expr<A> createConstant(A obj);

}
