/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * OBoolean represents boolean operations
 * 
 * @author tiwe
 * 
 */
public class OBoolean extends EBoolean implements Operation<Boolean, Boolean> {

    private final List<Expr<?>> args;
    
    private final Operator<Boolean> op;

    public OBoolean(Operator<Boolean> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    public OBoolean(Operator<Boolean> op, List<Expr<?>> args) {
        this.op = op;
        this.args = Collections.unmodifiableList(args);
        validate();
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
    public List<Expr<?>> getArgs() {
        return args;
    }

    @Override
    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public Operator<Boolean> getOperator() {
        return op;
    }
}