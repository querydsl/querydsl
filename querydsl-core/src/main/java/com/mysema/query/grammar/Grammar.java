/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;


import static com.mysema.query.grammar.types.Factory.createBoolean;
import static com.mysema.query.grammar.types.Factory.createConstant;
import static com.mysema.query.grammar.types.Factory.createNumber;

import com.mysema.query.grammar.types.CollectionType;
import com.mysema.query.grammar.types.Expr;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
public class Grammar {
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> add(Expr<A> left, A right) {
        return createNumber(Ops.ADD, left, createConstant(right));
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> add(Expr<A> left, Expr<A> right) {
        return createNumber(Ops.ADD, left, right);
    }

    public static <A extends Number & Comparable<A>> Expr.EComparable<A> div(Expr<A> left, A right) {
        return createNumber(Ops.DIV, left, createConstant(right));
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> div(Expr<A> left, Expr<A> right) {
        return createNumber(Ops.DIV, left, right);
    }
    
    public static <A> Expr.EBoolean in(A left, CollectionType<A> right){
        return createBoolean(Ops.IN, createConstant(left), (Expr<?>)right);
    }

    public static <A extends Number & Comparable<A>> Expr.EComparable<A> mult(Expr<A> left, A right) {
        return createNumber(Ops.MULT, left, createConstant(right));
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> mult(Expr<A> left, Expr<A> right) {
        return createNumber(Ops.MULT, left, right);
    }

    public static Expr.EBoolean not(Expr.EBoolean left) {
        return createBoolean(Ops.NOT, left);
    }

    public static <A extends Number & Comparable<A>> Expr.EComparable<A> sqrt(Expr<A> left) {
        return createNumber(Ops.SQRT, left);
    }
    
    public static <A extends Number & Comparable<A>> Expr.EComparable<A> sub(Expr<A> left, A right) {
        return createNumber(Ops.SUB, left, createConstant(right));
    }

    public static <A extends Number & Comparable<A>> Expr.EComparable<A> sub(Expr<A> left, Expr<A> right) {
        return createNumber(Ops.SUB, left, right);
    }
    
}
