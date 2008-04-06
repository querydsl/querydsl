/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops.Op;

/**
 * Factory provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Factory {
    
    public static void checkArg(String name, Object obj) {
        if (obj == null) throw new IllegalArgumentException(name + " was null");        
    }
    
    public static final Expr.Boolean createBoolean(Op<Boolean> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.Boolean(operator, args);
    }
    
    public static final <OP, RT extends Comparable<RT>> Expr.Comparable<RT> createComparable(Op<OP> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.Comparable<OP,RT>(operator, args);
    }
    
    @SuppressWarnings("unchecked")
    public static final <A> Expr<A> createConstant(A obj) {
        checkArg("constant",obj);
        if (obj instanceof Expr)
            return (Expr<A>) obj;
        return new Expr.Constant<A>(obj);
    }

    public static final <N extends Number,D extends Comparable<D>> Expr.Comparable<D> createNumber(Op<N> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.Number<N,D>(operator, args);
    }
    
    public static final Expr.String createString(Op<String> operator, Expr<?>... args) {
        checkArg("operator",operator);
        checkArg("args",args);
        return new Operation.String(operator, args);
    }

}
