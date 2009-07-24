/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class AggregationFunctions {
    
    private AggregationFunctions(){}

    public static <A extends Number & Comparable<?>> ENumber<Double> avg( Expr<A> left) {
        return ONumber.create(Double.class, Ops.AggOps.AVG_AGG, left);
    }

    public static ENumber<Long> count() {
        return Ops.AggOps.COUNT_ALL_AGG_EXPR;        
    }

    public static ENumber<Long> count(Expr<?> expr) {
        return ONumber.create(Long.class, Ops.AggOps.COUNT_AGG, expr);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> max( Expr<A> left) {
        return ONumber.create(left.getType(), Ops.AggOps.MAX_AGG, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> min( Expr<A> left) {
        return ONumber.create(left.getType(), Ops.AggOps.MIN_AGG, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> sum( Expr<A> left) {
        return ONumber.create(Double.class, Ops.AggOps.SUM_AGG, left);
    }
    
}
