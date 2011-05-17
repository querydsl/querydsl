/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * OperationImpl is the default implementation of the Operation interface
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class OperationImpl<T> extends ExpressionBase<T> implements Operation<T>{

    private static final long serialVersionUID = 4796432056083507588L;

    private final List<Expression<?>> args;

    private final Operator<? super T> operator;

    public static <RT> Operation<RT> create(Class<? extends RT> type, Operator<? super RT> operator, Expression<?>... args){
        return new OperationImpl<RT>(type, operator, args);
    }

    public OperationImpl(Class<? extends T> type, Operator<? super T> operator, Expression<?>... args){
        this(type, operator, Arrays.asList(args));
    }

    public OperationImpl(Class<? extends T> type, Operator<? super T> operator, List<Expression<?>> args){
        super(type);
        this.operator = operator;
        this.args = Collections.unmodifiableList(args);
    }

    @Override
    public Expression<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public Operator<? super T> getOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof Operation<?>){
            Operation<?> op = (Operation<?>)o;
            return op.getOperator().equals(operator)
                && op.getArgs().equals(args)
                && op.getType().equals(type);
        }else{
            return false;
        }
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public int hashCode(){
        return type.hashCode();
    }

}
