/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;

/**
 * The Class Simple.
 */
public class QSimple<Q> extends ESimple<Q> implements Quant<Q> {

    private final Expr<?> col;

    private final Operator<?> op;

    public QSimple(Class<Q> type, Operator<?> op, CollectionType<Q> col) {
        super(type);
        this.op = op;
        this.col = (Expr<?>) col;
    }

    public Operator<?> getOperator() {
        return op;
    }

    public Expr<?> getTarget() {
        return col;
    }
}