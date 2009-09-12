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
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * OBoolean represents boolean operations
 * 
 * @author tiwe
 * 
 */
@SuppressWarnings("serial")
public class OBoolean extends EBoolean implements Operation<Boolean, Boolean> {

    public static EBoolean create(Operator<Boolean> op, Expr<?>... args){
        return new OBoolean(op, args);
    }
    
    private final List<Expr<?>> args;

    private final Operator<Boolean> op;

    OBoolean(Operator<Boolean> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }
    
    OBoolean(Operator<Boolean> op, List<Expr<?>> args) {
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
    public Operator<Boolean> getOperator() {
        return op;
    }
    
    @Override
    public EBoolean not() {
        if (op == Ops.NOT && args.get(0) instanceof EBoolean){
            return (EBoolean) args.get(0);
        }else{
            return super.not();
        }
    }

    @Override
    public EBoolean asExpr() {
        return this;
    }
}