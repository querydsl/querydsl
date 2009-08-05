/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.HashMap;
import java.util.Map;

import com.mysema.commons.lang.Assert;

/**
 * EConstant represents constant expressions
 * 
 * @author tiwe
 * 
 * @param <D> Java type of constant
 */

public class EConstant<D> extends Expr<D> {

    @SuppressWarnings("unchecked")
    private static final EConstant<Integer>[] integers = new EConstant[256];
    
    private static final Map<Class<?>, Expr<?>> classToExpr = new HashMap<Class<?>, Expr<?>>();
    
    static{
        for (int i = 0; i < integers.length; i++) {
            integers[i] = new EConstant<Integer>(i - 128);
        }
    }
    
    /**
     * Factory method
     * 
     * @param <A>
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <A> Expr<Class<A>> create(Class<A> obj) {
        if (classToExpr.containsKey(obj)) {
            return (Expr<Class<A>>) classToExpr.get(obj);
        } else {
            Expr<Class<A>> expr = new EConstant<Class<A>>(obj);
            classToExpr.put(obj, expr);
            return expr;
        }
    }

    /**
     * Factory method
     * 
     * @param <D>
     * @param val
     * @return
     */
    public static <D> Expr<D> create(D val){
        return new EConstant<D>(Assert.notNull(val,"val is null"));
    }

    /**
     * Factory method
     * 
     * @param i
     * @return
     */
    public static Expr<Integer> create(int i){
        if (i >= -128 && i <= 127) {
            return integers[i + 128];
        } else {
            return new EConstant<Integer>(i);
        }
    }

    private final D constant;

    @SuppressWarnings("unchecked")
    private EConstant(D constant) {
        super((Class<D>) constant.getClass());
        this.constant = constant;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof EConstant ? ((EConstant<?>) o).getConstant().equals(constant) : false;
    }
    
    /**
     * Get the embedded constant
     * 
     * @return
     */
    public D getConstant() {
        return constant;
    }
    
    @Override
    public int hashCode() {
        return constant.hashCode();
    }
    
}