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
public abstract class EEmbeddable<D> extends ESimple<D> {
    public EEmbeddable(Class<? extends D> type) {
        super(type);
    }
}