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
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.Expr;

/**
 * ODateTime represents DateTime operations
 * 
 * @author tiwe
 *
 * @param <OpType>
 * @param <D>
 */
public class ODateTime<OpType extends Comparable<?>, D extends Comparable<?>> extends
EDateTime<D> implements Operation<OpType, D> {

    /**
     * Factory method
     * 
     * @param <O>
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <O extends Comparable<?>,D extends Comparable<?>> EDateTime<D> create(Class<D> type, Operator<O> op, Expr<?>... args){
        return new ODateTime<O,D>(type, op, args);
    }
    
    private final List<Expr<?>> args;

    private final Operator<OpType> op;

    ODateTime(Class<D> type, Operator<OpType> op, Expr<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    ODateTime(Class<D> type, Operator<OpType> op, List<Expr<?>> args) {
        super(type);
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
    public Operator<OpType> getOperator() {
        return op;
    }
}
