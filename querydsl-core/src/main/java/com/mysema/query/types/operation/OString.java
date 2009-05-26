/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * @author tiwe
 * 
 */
public class OString extends EString implements Operation<String, String> {
    private final List<Expr<?>> args;
    private final Op<String> op;

    public OString(Op<String> op, Expr<?>... args) {
        this(op, asList(args));
    }

    public OString(Op<String> op, List<Expr<?>> args) {
        this.op = op;
        this.args = unmodifiableList(args);
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