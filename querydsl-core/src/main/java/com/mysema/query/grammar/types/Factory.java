/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;

/**
 * Factory provides factory methods for various needs
 * 
 * @author tiwe
 * @version $Id$
 */
public class Factory {
    
    public static void checkArg(String name, Object obj) {
        if (obj == null) throw new IllegalArgumentException(name + " was null");        
    }
    
    public static final Expr.EBoolean createBoolean(Op<Boolean> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.OBoolean(operator, args);
    }
    
    public static final <OpType, RT extends Comparable<? super RT>> Expr.EComparable<RT> createComparable(Class<RT> type, Op<OpType> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.OComparable<OpType,RT>(type, operator, args);
    }
    
    @SuppressWarnings("unchecked")
    public static final <A> Expr<A> createConstant(A obj) {
        checkArg("constant",obj);
        if (obj instanceof Expr)
            return (Expr<A>) obj;
        return new Expr.EConstant<A>(obj);
    }

    public static final <OpType extends Number,D extends Number & Comparable<? super D>> Expr.ENumber<D> createNumber(Class<D> type, Op<OpType> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.ONumber<OpType,D>(type, operator, args);
    }
    
    public static final Expr.EString createString(Op<String> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.OString(operator, args);
    }
    
    public static final Expr<String[]> createStringArray(Op<String> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.OStringArray(operator, args);
    }

}
