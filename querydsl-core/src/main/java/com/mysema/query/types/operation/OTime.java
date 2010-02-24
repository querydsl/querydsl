/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.Expr;

/**
 * OTime represents Time functions
 * 
 * @author tiwe
 *
 * @param <OpType>
 * @param <D>
 */
@SuppressWarnings("serial")
public class OTime<OpType, D extends Comparable<?>> extends ETime<D> implements Operation<OpType, D> {

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
    public static <O,D extends Comparable<?>> ETime<D> create(Class<D> type, Operator<O> op, Expr<?>... args){
        return new OTime<O,D>(type, op, args);
    }
    
    private final Operation<OpType, D> opMixin;
    
    OTime(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    OTime(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.opMixin = new OperationMixin<OpType, D>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public ETime<D> asExpr() {
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
    
    @Override
    public boolean equals(Object o){
        return opMixin.equals(o);
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
}
