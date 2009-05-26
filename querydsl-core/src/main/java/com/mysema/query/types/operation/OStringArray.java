/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * @author tiwe
 * 
 */
public class OStringArray extends Expr<String[]> implements
        Operation<String, String[]> {
    private final List<Expr<?>> args;
    private final Op<String> op;

    public OStringArray(Op<String> op, Expr<?>... args) {
        this(op, Arrays.asList(args));
    }

    public OStringArray(Op<String> op, List<Expr<?>> args) {
        super(null);
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

    public Op<String> getOperator() {
        return op;
    }
}