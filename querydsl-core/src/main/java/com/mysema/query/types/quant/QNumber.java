/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * @author tiwe
 * 
 * @param <Q>
 */
public class QNumber<Q extends Number & Comparable<? super Q>> extends
        ENumber<Q> implements Quant<Q> {

    private final Expr<?> col;

    private final Op<?> op;

    public QNumber(Class<Q> type, Op<?> op, CollectionType<Q> col) {
        super(type);
        this.op = op;
        this.col = (Expr<?>) col;
    }

    public Op<?> getOperator() {
        return op;
    }

    public Expr<?> getTarget() {
        return col;
    }
}