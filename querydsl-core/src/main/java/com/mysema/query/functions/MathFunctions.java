/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops.MathOps;

/**
 * MathFunctions provides additional math functions that are not available 
 * in standard numeric expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public final class MathFunctions {

    private MathFunctions() {
    }

    /**
     * use left.abs() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> abs(ENumber<A> left) {
        return left.abs();
    }

    /**
     * use left.sqrt() instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<Double> sqrt(ENumber<A> left) {
        return left.sqrt();
    }
    
    /**
     * use ENumber.random() instead
     */
    @Deprecated
    public static ENumber<Double> random() {
        return ENumber.random();
    }
    
    /**
     * use ENumber.max(left, right) instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> max(Expr<A> left, Expr<A> right) {
        return ENumber.max(left, right);
    }

    /**
     * use ENumber.min(left, right) instead
     */
    @Deprecated
    public static <A extends Number & Comparable<?>> ENumber<A> min(Expr<A> left, Expr<A> right) {
        return ENumber.min(left, right);
    }

    /**
     * use left.ceil() instead
     */
    @Deprecated
    public static ENumber<Double> ceil(ENumber<Double> left) {
        return left.ceil();
    }
    
    /**
     * use left.round() instead
     */
    @Deprecated
    public static ENumber<Double> round(ENumber<Double> left) {
        return left.round();
    }
    
    /**
     * use left.floor() instead
     */
    @Deprecated
    public static ENumber<Double> floor(ENumber<Double> left) {
        return left.floor();
    }
    
    // REMAINING
    
    public static ENumber<Double> acos(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ACOS, left);
    }

    public static ENumber<Double> asin(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ASIN, left);
    }

    public static ENumber<Double> atan(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ATAN, left);
    }

    public static ENumber<Double> cos(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.COS, left);
    }

    public static ENumber<Double> exp(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.EXP, left);
    }

    public static ENumber<Double> log(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.LOG, left);
    }

    public static ENumber<Double> log10(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.LOG10, left);
    }

    public static ENumber<Double> pow(Expr<Double> left, Expr<Double> right) {
        return ONumber.create(left.getType(), MathOps.POWER, left, right);
    }

    public static ENumber<Double> sin(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.SIN, left);
    }

    public static ENumber<Double> tan(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.TAN, left);
    }

}
