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
public class OSimple<OpType, D> extends Expr<D> implements Operation<OpType, D> {
    
    private final List<Expr<?>> args;
    
    private final Operator<OpType> op;

    OSimple(Class<? extends D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    OSimple(Class<? extends D> type, Operator<OpType> op, List<Expr<?>> args) {
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
    
    /**
     * Factory method
     * 
     * @param <OpType>
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <OpType,D> Expr<D> create(Class<? extends D> type, Operator<OpType> op, Expr<?>... args){
        return new OSimple<OpType,D>(type, op, args);
    }
}