/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.ENumber;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops.MathOps;
import com.mysema.query.types.operation.ONumber;

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
