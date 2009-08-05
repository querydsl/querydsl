/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class AggregationFunctions {
    
    private AggregationFunctions(){}

    /**
     * Use expr.avg() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<Double> avg( ENumber<A> left) {
        return left.avg();
    }

    
    public static ENumber<Long> count() {
        return Ops.AggOps.COUNT_ALL_AGG_EXPR;        
    }

    /**
     * Use expr.avg() instead
     */
    @Deprecated
    public static ENumber<Long> count(Expr<?> expr) {
        return expr.count();
    }

    /**
     * Use expr.max() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> max( ENumber<A> left) {
        return left.max();
    }

    /**
     * Use expr.min() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> min( ENumber<A> left) {
        return left.min();
    }

    /**
     * Use expr.sum() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> sum( ENumber<A> left) {
        return left.sum();
    }
    
}
