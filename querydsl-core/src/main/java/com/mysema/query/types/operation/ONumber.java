/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * ONumber represents numeric operations
 * 
 * @author tiwe
 * 
 * @param <OpType>
 * @param <D>
 */
@SuppressWarnings("serial")
public class ONumber<OpType extends Number, D extends Number & Comparable<?>>
        extends ENumber<D> implements Operation<OpType, D> {
    
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
    public static <O extends Number,D extends Number & Comparable<?>> ENumber<D> create(Class<? extends D> type, Operator<O> op, Expr<?>... args){
        return new ONumber<O,D>(type, op, args);
    }
    
    private final Operation<OpType, D> opMixin;
    
    ONumber(Class<? extends D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    ONumber(Class<? extends D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.opMixin = new OperationMixin<OpType, D>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public ENumber<D> asExpr() {
        return this;
    }

    @Override
    public Expr<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator<OpType> getOperator() {
        return opMixin.getOperator();
    }
}