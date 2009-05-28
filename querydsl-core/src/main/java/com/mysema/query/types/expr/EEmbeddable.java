/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

/**
 * EEmbeddable represents an embeddable expression
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 */
public abstract class EEmbeddable<D> extends Expr<D> {
    public EEmbeddable(Class<? extends D> type) {
        super(type);
    }
}