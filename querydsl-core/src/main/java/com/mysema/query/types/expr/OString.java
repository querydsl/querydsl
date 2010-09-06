/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Visitor;

/**
 * OString represents a String typed operation
 *
 * @author tiwe
 *
 */
public class OString extends EString implements Operation<String> {

    private static final long serialVersionUID = 6846556373847139549L;

    public static EString create(Operator<? super String> op, Expr<?>... args){
        return new OString(op, args);
    }

    private final Operation<String> opMixin;

    OString(Operator<? super String> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    OString(Operator<? super String> op, List<Expr<?>> args) {
        this.opMixin = new OperationMixin<String>(this, op, args);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
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
    public Operator<? super String> getOperator() {
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
