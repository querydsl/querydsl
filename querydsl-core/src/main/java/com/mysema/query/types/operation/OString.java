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
    
    private final List<Expr<?>> args;

    private final Operator<String> op;

    OString(Operator<String> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    OString(Operator<String> op, List<Expr<?>> args) {
        this.op = op;
        this.args = Collections.unmodifiableList(args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }
    
    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }
    
    @Override
    public Operator<String> getOperator() {
        return op;
    }

    @Override
    public EString asExpr() {
        return this;
    }
}