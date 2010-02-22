/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * OString represents a String typed operation
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("serial")
public class OString extends EString implements Operation<String, String> {
    
    public static EString create(Operator<String> op, Expr<?>... args){
        return new OString(op, args);
    }
    
    private final Operation<String, String> opMixin;

    OString(Operator<String> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    OString(Operator<String> op, List<Expr<?>> args) {
        this.opMixin = new OperationMixin<String, String>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public EString asExpr() {
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
    public Operator<String> getOperator() {
        return opMixin.getOperator();
    }
}