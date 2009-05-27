/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;

/**
 * The Class Comparable.
 */
public class QComparable<Q extends Comparable<? super Q>> extends
        EComparable<Q> implements Quant<Q> {

    private final Expr<?> col;

    private final Operator<?> op;

    public QComparable(Class<Q> type, Operator<?> op, CollectionType<Q> col) {
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