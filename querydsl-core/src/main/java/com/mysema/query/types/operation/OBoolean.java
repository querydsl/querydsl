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
 * @author tiwe
 * 
 */
public class OBoolean extends EBoolean implements Operation<Boolean, Boolean> {
    private final List<Expr<?>> args;
    private final Op<Boolean> op;

    public OBoolean(Op<Boolean> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    public OBoolean(Op<Boolean> op, List<Expr<?>> args) {
        this.op = op;
        this.args = Collections.unmodifiableList(args);
        validate();
    }

    public List<Expr<?>> getArgs() {
        return args;
    }

    public Expr<?> getArg(int i) {
        return args.get(i);
    }

    public Op<Boolean> getOperator() {
        return op;
    }
}