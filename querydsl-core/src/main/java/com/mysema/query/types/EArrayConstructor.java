/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.commons.lang.Assert;

/**
 * EArrayConstructor extends {@link EConstructor} to represent array initializers
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
// TODO : split into interface and implementation
public class EArrayConstructor<D> extends EConstructor<D[]> {
    
    private static final long serialVersionUID = 8667880104290226505L;
    
    private final Class<D> elementType;

    @SuppressWarnings("unchecked")
    public EArrayConstructor(Class<D[]> type, Expr<D>... args) {
        super(type, new Class[0], args);
        this.elementType = (Class<D>) Assert.notNull(type.getComponentType(),"componentType");
    }

    public final Class<D> getElementType() {
        return elementType;
    }
    
    public void accept(Visitor v){
        v.visit(this);
    }
}