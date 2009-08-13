/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * EArrayConstructor extends EConstructor to represent array initializers
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class EArrayConstructor<D> extends EConstructor<D[]> {
    
    private final Class<D> elementType;

    @SuppressWarnings("unchecked")
    public EArrayConstructor(Class<D> type, Expr<D>... args) {
        super((Class)Object.class, args);
        this.elementType = type;
    }

    public final Class<D> getElementType() {
        return elementType;
    }
}