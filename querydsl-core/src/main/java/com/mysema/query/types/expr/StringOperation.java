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
 * StringOperation represents a String typed operation
 *
 * @author tiwe
 *
 */
public class StringOperation extends StringExpression implements Operation<String> {

    private static final long serialVersionUID = 6846556373847139549L;

    public static StringExpression create(Operator<? super String> op, Expression<?>... args) {
        return new StringOperation(op, args);
    }

    private final Operation<String> opMixin;

    protected StringOperation(Operator<? super String> op, Expression<?>... args) {
        this(op, Arrays.asList(args));
    }

    protected StringOperation(Operator<? super String> op, List<Expression<?>> args) {
        this.opMixin = new OperationImpl<String>(String.class, op, args);
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
    public Operator<? super String> getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public boolean equals(Object o) {
        return opMixin.equals(o);
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

}
