/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;
import com.mysema.query.util.Assert;

/**
 * Factory provides factory methods for various needs
 * 
 * @author tiwe
 * @version $Id$
 */
public class Factory {
    
    public static final Expr.EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args) {
        return new Operation.OBoolean(Assert.notNull(operator), Assert.notNull(args));
    }
    
    public static final <OpType, RT extends Comparable<? super RT>> Expr.EComparable<RT> createComparable(Class<RT> type, Op<OpType> operator, Expr<?>... args) {
        return new Operation.OComparable<OpType,RT>(type, Assert.notNull(operator), Assert.notNull(args));
    }
    
    @SuppressWarnings("unchecked")
    public static final <A> Expr<A> createConstant(A obj) {
        if (obj instanceof Expr) return (Expr<A>) obj;
        return new Expr.EConstant<A>(Assert.notNull(obj));
    }

    public static final <OpType extends Number,D extends Number & Comparable<? super D>> Expr.ENumber<D> createNumber(Class<? extends D> type, Op<OpType> operator, Expr<?>... args) {
        return new Operation.ONumber<OpType,D>(type, Assert.notNull(operator), Assert.notNull(args));
    }
    
    public static final Expr.EString createString(Op<String> operator, Expr<?>... args) {
        return new Operation.OString(Assert.notNull(operator), Assert.notNull(args));
    }
    
    public static final Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args) {
        return new Operation.OStringArray(Assert.notNull(operator), Assert.notNull(args));
    }

}
