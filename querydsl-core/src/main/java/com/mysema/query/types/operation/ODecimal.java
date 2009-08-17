/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.EDecimal;
import com.mysema.query.types.expr.Expr;

/**
 * ONumber represents numeric operations
 * 
 * @author tiwe
 * 
 * @param <OpType>
 * @param <D>
 */
public class ODecimal<OpType extends Number, D extends Number & Comparable<?>>
        extends EDecimal<D> implements Operation<OpType, D> {
    
    private final List<Expr<?>> args;
    
    private final Operator<OpType> op;

    ODecimal(Class<? extends D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    ODecimal(Class<? extends D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.op = op;
        this.args = Collections.unmodifiableList(args);
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
     * @param <O>
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <O extends Number,D extends Number & Comparable<?>> EDecimal<D> create(Class<? extends D> type, Operator<O> op, Expr<?>... args){
        return new ODecimal<O,D>(type, op, args);
    }
}