/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Visitor;

/**
 * NumberOperation represents numeric operations
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class NumberOperation<T extends Number & Comparable<?>>
        extends NumberExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -3593040852095778453L;

    /**
     * Factory method
     *
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <D extends Number & Comparable<?>> NumberExpression<D> create(Class<? extends D> type, Operator<? super D> op, Expression<?>... args){
        return new NumberOperation<D>(type, op, args);
    }

    private final Operation<T> opMixin;

    NumberOperation(Class<? extends T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    NumberOperation(Class<? extends T> type, Operator<? super T> op, List<Expression<?>> args) {
        super(type);
        this.opMixin = new OperationImpl<T>(type, op, args);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public NumberExpression<T> negate(){
        if (opMixin.getOperator() == Ops.NEGATE){
            return (NumberExpression<T>) opMixin.getArg(0);
        }else{
            return super.negate();
        }
    }
    
    @Override
    public Expression<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator<? super T> getOperator() {
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
