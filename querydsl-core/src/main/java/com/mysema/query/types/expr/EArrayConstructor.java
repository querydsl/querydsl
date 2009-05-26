/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * 
 * @author tiwe
 * 
 * @param <D>
 */
public class EArrayConstructor<D> extends EConstructor<D[]> {
    private Class<D> elementType;

    public EArrayConstructor(Class<D> type, Expr<D>... args) {
        super(null, args);
        this.elementType = type;
    }

    public final Class<D> getElementType() {
        return elementType;
    }
}