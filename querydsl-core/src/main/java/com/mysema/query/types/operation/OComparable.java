/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * OComparable represents Comparable operations
 * 
 * @author tiwe
 * 
 * @param <OpType>
 * @param <D>
 */
public class OComparable<OpType, D extends Comparable<?>> extends
        EComparable<D> implements Operation<OpType, D> {
    
    private final List<Expr<?>> args;
    
    private final Operator<OpType> op;

    OComparable(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    OComparable(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
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
     * @param <O>
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <O,D extends Comparable<?>> EComparable<D> create(Class<D> type, Operator<O> op, Expr<?>... args){
        return new OComparable<O,D>(type, op, args);
    }
}