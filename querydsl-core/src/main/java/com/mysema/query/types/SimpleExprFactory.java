/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.HashMap;
import java.util.Map;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;

/**
 * SimpleExprFactory is the default implementation of the ExprFactory interface
 * 
 * @author tiwe
 * @version $Id$
 */
public class SimpleExprFactory implements ExprFactory {

    private static final ExprFactory instance = new SimpleExprFactory();

    private final Map<Class<?>, Expr<?>> classToExpr = new HashMap<Class<?>, Expr<?>>();

    @SuppressWarnings("unchecked")
    private final Expr<Integer>[] integers = new Expr[256];

    SimpleExprFactory() {
        for (int i = 0; i < integers.length; i++) {
            integers[i] = new EConstant<Integer>(i - 128);
        }
    }

    public static ExprFactory getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <A> Expr<A> createConstant(A obj) {
        if (obj instanceof Expr) {
            return (Expr<A>) obj;
        } else {
            return new EConstant<A>(Assert.notNull(obj));
        }
    }

    @SuppressWarnings("unchecked")
    public <A> Expr<Class<A>> createConstant(Class<A> obj) {
        if (classToExpr.containsKey(obj)) {
            return (Expr<Class<A>>) classToExpr.get(obj);
        } else {
            Expr<Class<A>> expr = new EConstant<Class<A>>(obj);
            classToExpr.put(obj, expr);
            return expr;
        }
    }

    public Expr<Integer> createConstant(int i) {
        if (i >= -128 && i <= 127) {
            return integers[i + 128];
        } else {
            return new EConstant<Integer>(i);
        }
    }

}
