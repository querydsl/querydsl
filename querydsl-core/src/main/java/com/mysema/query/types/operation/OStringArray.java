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
import com.mysema.query.types.expr.Expr;

/**
 * OStringArray represents a String[] typed operation
 * 
 * @author tiwe
 * 
 */
public class OStringArray extends Expr<String[]> implements
        Operation<String, String[]> {
    
    private final List<Expr<?>> args;
    
    private final Operator<String> op;

    OStringArray(Operator<String> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    OStringArray(Operator<String> op, List<Expr<?>> args) {
        super(String[].class);
        this.op = op;
        this.args = Collections.unmodifiableList(args);
    }

    @Override
    public List<Expr<?>> getArgs() {
        return args;
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public Operator<String> getOperator() {
        return op;
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
}