/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.Expr;

/**
 * ODateTime represents DateTime operations
 * 
 * @author tiwe
 *
 * @param <OpType>
 * @param <D>
 */

public class ODateTime<OpType extends Comparable<?>, D extends Comparable<?>> extends
    EDateTime<D> implements Operation<OpType, D> {

    private static final long serialVersionUID = 6523293814317168556L;

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
    public static <O extends Comparable<?>,D extends Comparable<?>> EDateTime<D> create(Class<D> type, Operator<O> op, Expr<?>... args){
        return new ODateTime<O,D>(type, op, args);
    }

    private final Operation<OpType, D> opMixin;
    
    ODateTime(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    ODateTime(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
        this.opMixin = new OperationMixin<OpType, D>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public EDateTime<D> asExpr() {
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
