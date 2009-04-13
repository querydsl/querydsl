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
public class SimpleExprFactory {
    
    public Expr.EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args) {
        return new Operation.OBoolean(Assert.notNull(operator), Assert.notNull(args));
    }
    
    public <OpType, RT extends Comparable<? super RT>> Expr.EComparable<RT> createComparable(Class<RT> type, Op<OpType> operator, Expr<?>... args) {
        return new Operation.OComparable<OpType,RT>(type, Assert.notNull(operator), Assert.notNull(args));
    }
    
    @SuppressWarnings("unchecked")
    public <A> Expr<A> createConstant(A obj) {
        if (obj instanceof Expr) return (Expr<A>) obj;
        return new Expr.EConstant<A>(Assert.notNull(obj));
    }

    public <OpType extends Number,D extends Number & Comparable<? super D>> Expr.ENumber<D> createNumber(Class<? extends D> type, Op<OpType> operator, Expr<?>... args) {
        return new Operation.ONumber<OpType,D>(type, Assert.notNull(operator), Assert.notNull(args));
    }
    
    public Expr.EString createString(Op<String> operator, Expr<?>... args) {
        return new Operation.OString(Assert.notNull(operator), Assert.notNull(args));
    }
    
    public Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args) {
        return new Operation.OStringArray(Assert.notNull(operator), Assert.notNull(args));
    }

}
