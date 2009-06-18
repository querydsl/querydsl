/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.MathOps;

/**
 * MathFunctions provides math functions
 * 
 * @author tiwe
 * @version $Id$
 */
public final class MathFunctions {

    private MathFunctions() {
    }

    public static <A extends Number & Comparable<?>> ENumber<A> mult(Expr<A> left, A right) {
        return ONumber.create(left.getType(), Ops.MULT, left, EConstant.create(right));
    }

    public static <A extends Number & Comparable<?>> ENumber<A> mult(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), Ops.MULT, left, right);
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> div(ENumber<A> left, A right) {
        return ONumber.create(Double.class, Ops.DIV, left, EConstant.create(right));
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> div(Expr<A> left, Expr<A> right) {
        return ONumber.create(Double.class, Ops.DIV, left, right);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> add(Expr<A> left, A right) {
        return ONumber.create(left.getType(), Ops.ADD, left, EConstant.create(right));
    }

    public static <A extends Number & Comparable<?>> ENumber<A> add(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), Ops.ADD, left, right);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> sub(Expr<A> left, A right) {
        return ONumber.create(left.getType(), Ops.SUB, left, EConstant.create(right));
    }

    public static <A extends Number & Comparable<?>> ENumber<A> sub(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), Ops.SUB, left, right);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> abs(Expr<A> left) {
        return ONumber.create(left.getType(), MathOps.ABS, left);
    }

    public static ENumber<Double> acos(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ACOS, left);
    }

    public static ENumber<Double> asin(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ASIN, left);
    }

    public static ENumber<Double> atan(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ATAN, left);
    }

    public static ENumber<Double> ceil(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.CEIL, left);
    }

    public static ENumber<Double> cos(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.COS, left);
    }

    public static ENumber<Double> exp(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.EXP, left);
    }

    public static ENumber<Double> floor(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.FLOOR, left);
    }

    public static ENumber<Double> log(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.LOG, left);
    }

    public static ENumber<Double> log10(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.LOG10, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> max(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), MathOps.MAX, left, right);
    }

    public static <A extends Number & Comparable<?>> ENumber<A> min(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), MathOps.MIN, left, right);
    }

    public static ENumber<Double> pow(Expr<Double> left, Expr<Double> right) {
        return ONumber.create(left.getType(), MathOps.POWER, left, right);
    }

    public static ENumber<Double> random() {
        return ONumber.create(Double.class, MathOps.RANDOM);
    }

    public static ENumber<Double> round(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.ROUND, left);
    }

    public static ENumber<Double> sin(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.SIN, left);
    }

    public static <A extends Number & Comparable<?>> ENumber<Double> sqrt(Expr<A> left) {
        return ONumber.create(Double.class, MathOps.SQRT, left);
    }

    public static ENumber<Double> tan(Expr<Double> left) {
        return ONumber.create(left.getType(), MathOps.TAN, left);
    }

}
