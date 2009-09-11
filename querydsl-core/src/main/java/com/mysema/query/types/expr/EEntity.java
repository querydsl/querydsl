/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * EEntity represents a general entity expression
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 */
@SuppressWarnings("serial")
public abstract class EEntity<D> extends Expr<D> {
    
    public EEntity(Class<? extends D> type) {
        super(type);
    }
    
}