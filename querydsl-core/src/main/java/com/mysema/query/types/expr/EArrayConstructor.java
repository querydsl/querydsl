/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.lang.reflect.Array;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.EConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Visitor;

/**
 * EArrayConstructor extends {@link EConstructor} to represent array initializers
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
public class EArrayConstructor<D> extends EConstructor<D[]> {
    
    private static final long serialVersionUID = 8667880104290226505L;
    
    private final Class<D> elementType;

    @SuppressWarnings("unchecked")
    public EArrayConstructor(Expr<?>... args) {
        this((Class)Object[].class, (Expr[])args);
    }
    
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
    
    @SuppressWarnings("unchecked")
    @Override
    public D[] newInstance(Object... args){
	if (args.getClass().getComponentType().equals(elementType)){
	    return (D[])args;
	}else{
	    D[] rv = (D[]) Array.newInstance(elementType, args.length);
	    System.arraycopy(args, 0, rv, 0, args.length);
	    return rv;
	}
    }
}