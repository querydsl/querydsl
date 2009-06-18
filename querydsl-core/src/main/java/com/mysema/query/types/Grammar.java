/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {

    protected static final ExprFactory exprFactory = SimpleExprFactory.getInstance();

    protected static final OperationFactory operationFactory = SimpleOperationFactory.getInstance();

    public static <A extends Number & Comparable<?>> ENumber<Double> avg( Expr<A> left) {
        return operationFactory.createNumber(Double.class, Ops.AVG_AGG, left);
    }

    public static ENumber<Long> count() {
        return operationFactory.createNumber(Long.class, Ops.COUNT_ALL_AGG);        
    }

    public static ENumber<Long> count(Expr<?> expr) {
        return operationFactory.createNumber(Long.class, Ops.COUNT_AGG, expr);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> max( Expr<A> left) {
        return operationFactory.createNumber(left.getType(), Ops.MAX_AGG, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> min( Expr<A> left) {
        return operationFactory.createNumber(left.getType(), Ops.MIN_AGG, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> sum( Expr<A> left) {
        return operationFactory.createNumber(Double.class, Ops.SUM_AGG, left);
    }
    
    protected Grammar() {}
}
