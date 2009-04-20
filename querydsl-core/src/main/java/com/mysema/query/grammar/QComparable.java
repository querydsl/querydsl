/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.SimpleExprFactory;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QComparable provides helper methods to construct comparison expressions for types
 * that don't implement Comparable in generic fashion such as Joda time types
 *
 * @author tiwe
 * @version $Id$
 */
public class QComparable {
   
    protected static final SimpleExprFactory factory = new SimpleExprFactory();
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean before(Expr<A> left, A right) {
        return factory.createBoolean(Ops.BEFORE, left, factory.createConstant(right));
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean before(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.BEFORE, left, right);
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean after(Expr<A> left, A right) {
        return factory.createBoolean(Ops.AFTER, left, factory.createConstant(right));
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean after(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.AFTER, left, right);
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean boe(Expr<A> left,A right) {
        return factory.createBoolean(Ops.BOE, left, factory.createConstant(right));
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean boe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.BOE, left, right);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean aoe(Expr<A> left, A right) {
        return factory.createBoolean(Ops.AOE, left, factory.createConstant(right));
    }
    
    @SuppressWarnings("unchecked")
    public static <A extends Comparable> EBoolean aoe(Expr<A> left, Expr<A> right) {
        return factory.createBoolean(Ops.AOE, left, right);
    }
}
