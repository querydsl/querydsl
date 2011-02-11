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
import com.mysema.query.types.Visitor;

/**
 * SimpleOperation represents a simple operation expression
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class SimpleOperation<T> extends SimpleExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -285668548371034230L;

    /**
     * Factory method
     *
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <D> SimpleExpression<D> create(Class<D> type, Operator<? super D> op, Expression<?>... args){
        return new SimpleOperation<D>(type, op, args);
    }

    private final Operation< T> opMixin;

    SimpleOperation(Class<T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    SimpleOperation(Class<T> type, Operator<? super T> op, List<Expression<?>> args) {
        super(type);
        this.opMixin = new OperationImpl<T>(type, op, args);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
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
