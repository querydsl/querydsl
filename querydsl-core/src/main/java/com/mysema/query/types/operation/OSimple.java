/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * @author tiwe
 * 
 * @param <OpType>
 * @param <D>
 */
public class OSimple<OpType, D> extends ESimple<D> implements
        Operation<OpType, D> {
    private final List<Expr<?>> args;
    private final Op<OpType> op;

    public OSimple(Class<D> type, Op<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    public OSimple(Class<D> type, Op<OpType> op, List<Expr<?>> args) {
        super(type);
        this.op = op;
        this.args = Collections.unmodifiableList(args);
        validate();
    }

    public List<Expr<?>> getArgs() {
        return args;
    }

    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    public Op<OpType> getOperator() {
        return op;
    }
}