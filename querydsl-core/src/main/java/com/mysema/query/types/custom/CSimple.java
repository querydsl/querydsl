/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.Expr;

/**
 * CSimple defines custom simple expressions
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class CSimple<T> extends Expr<T> implements Custom<T> {
    
    public CSimple(Class<? extends T> type) {
        super(type);
    }

    @Override
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }
}