/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
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
public class OBoolean extends EBoolean implements Operation<Boolean, Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    public static EBoolean create(Operator<Boolean> op, Expr<?>... args){
        return new OBoolean(op, args);
    }
   
    private final Operation<Boolean, Boolean> opMixin;
    
    OBoolean(Operator<Boolean> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }
    
    OBoolean(Operator<Boolean> op, List<Expr<?>> args) {
        opMixin = new OperationMixin<Boolean,Boolean>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    
    @Override
    public EBoolean asExpr() {
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
    public Operator<Boolean> getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public EBoolean not() {
        if (opMixin.getOperator() == Ops.NOT && opMixin.getArg(0) instanceof EBoolean){
            return (EBoolean) opMixin.getArg(0);
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