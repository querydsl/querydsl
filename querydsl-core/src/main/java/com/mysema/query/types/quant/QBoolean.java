/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.quant;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Operator;

/**
 * The Class Boolean.
 */
public class QBoolean extends EBoolean implements Quant<Boolean> {
    private final Expr<?> col;
    private final Operator<?> op;

    public QBoolean(Operator<?> op, CollectionType<?> col) {
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