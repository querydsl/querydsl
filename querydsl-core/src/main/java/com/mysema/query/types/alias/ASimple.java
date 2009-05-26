/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.alias;

import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;

/**
 * Alias to symbol
 */
public class ASimple<D> extends ESimple<D> implements Alias {
    private final Expr<?> from;
    private final String to;

    public ASimple(Expr<D> from, String to) {
        super(from.getType());
        this.from = from;
        this.to = to;
    }

    public Expr<?> getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}