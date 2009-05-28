/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * EConstant represents constant expressions
 * 
 * @author tiwe
 * 
 * @param <D> Java type of constant
 */
public class EConstant<D> extends Expr<D> {
    private final D constant;

    @SuppressWarnings("unchecked")
    public EConstant(D constant) {
        super((Class<D>) constant.getClass());
        this.constant = constant;
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

    @Override
    public boolean equals(Object o) {
        return o instanceof EConstant ? ((EConstant<?>) o).getConstant()
                .equals(constant) : false;
    }
}