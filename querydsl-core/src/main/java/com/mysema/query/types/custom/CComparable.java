/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * CComparable defines custom comparable expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class CComparable<T extends Comparable<?>> extends
        EComparable<T> implements Custom<T> {
    public CComparable(Class<T> type) {
        super(type);
    }

    @Override
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }
}