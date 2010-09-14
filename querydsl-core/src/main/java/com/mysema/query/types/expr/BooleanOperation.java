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
import com.mysema.query.types.OperationMixin;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Visitor;

/**
 * BooleanOperation represents boolean operations
 *
 * @author tiwe
 *
 */
public class BooleanOperation extends BooleanExpression implements Operation<Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    public static BooleanExpression create(Operator<? super Boolean> op, Expression<?>... args){
        return new BooleanOperation(op, args);
    }

    private final Operation<Boolean> opMixin;

    BooleanOperation(Operator<? super Boolean> op, Expression<?>... args) {
        this(op, Arrays.asList(args));
    }

    BooleanOperation(Operator<? super Boolean> op, List<Expression<?>> args) {
        opMixin = new OperationMixin<Boolean>(this, op, args);
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
    public Operator<? super Boolean> getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public BooleanExpression not() {
        if (opMixin.getOperator() == Ops.NOT && opMixin.getArg(0) instanceof BooleanExpression){
            return (BooleanExpression) opMixin.getArg(0);
        }else{
            return super.not();
        }
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
