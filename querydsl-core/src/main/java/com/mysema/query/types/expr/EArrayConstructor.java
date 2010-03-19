/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Visitor;

/**
 * EArrayConstructor extends EConstructor to represent array initializers
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
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