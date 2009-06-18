/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

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
public class Grammar {

    public static <A extends Number & Comparable<?>> ENumber<Double> avg( Expr<A> left) {
        return ONumber.create(Double.class, Ops.AVG_AGG, left);
    }

    public static ENumber<Long> count() {
        return ONumber.create(Long.class, Ops.COUNT_ALL_AGG);        
    }

    public static ENumber<Long> count(Expr<?> expr) {
        return ONumber.create(Long.class, Ops.COUNT_AGG, expr);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Number & Comparable<?>> ENumber<A> max( Expr<A> left) {
        return ONumber.create((Class<A>)left.getType(), Ops.MAX_AGG, left);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Number & Comparable<?>> ENumber<A> min( Expr<A> left) {
        return ONumber.create((Class<A>)left.getType(), Ops.MIN_AGG, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> sum( Expr<A> left) {
        return ONumber.create(Double.class, Ops.SUM_AGG, left);
    }
    
    protected Grammar() {}
}
