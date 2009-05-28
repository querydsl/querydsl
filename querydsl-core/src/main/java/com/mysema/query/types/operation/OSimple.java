/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.Expr;

/**
 * OSimple represents a simple operation expression
 * 
 * @author tiwe
 * 
 * @param <OpType>
 * @param <D>
 */
public class OSimple<OpType, D> extends Expr<D> implements
        Operation<OpType, D> {
    private final List<Expr<?>> args;
    private final Operator<OpType> op;

    public OSimple(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    public OSimple(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.op = op;
        this.args = Collections.unmodifiableList(args);
        validate();
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public Operator<OpType> getOperator() {
        return op;
    }
}