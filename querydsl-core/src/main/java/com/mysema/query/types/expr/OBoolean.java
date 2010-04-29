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
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Visitor;

/**
 * OBoolean represents boolean operations
 * 
 * @author tiwe
 * 
 */
public class OBoolean extends EBoolean implements Operation<Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    public static EBoolean create(Operator<? super Boolean> op, Expr<?>... args){
        return new OBoolean(op, args);
    }
   
    private final Operation<Boolean> opMixin;
    
    OBoolean(Operator<? super Boolean> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }
    
    OBoolean(Operator<? super Boolean> op, List<Expr<?>> args) {
        opMixin = new OperationMixin<Boolean>(this, op, args);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
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
    public Operator<? super Boolean> getOperator() {
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