/*
 * Copyright 2012, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.expr;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Ops.MathOps;

/**
 * Extended Math expressions, supported by the SQL module
 *
 * @author tiwe
 *
 */
public final class MathExpressions {

    /**
     * @param num
     * @return acos(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> acos(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.ACOS, num);
    }

    /**
     * @param num
     * @return asin(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> asin(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.ASIN, num);
    }

    /**
     * @param num
     * @return atan(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> atan(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.ATAN, num);
    }

    /**
     * @param num
     * @return cos(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cos(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.COS, num);
    }

    /**
     * @param num
     * @return cosh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cosh(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.COSH, num);
    }

    /**
     * @param num
     * @return cot(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cot(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.COT, num);
    }

    /**
     * @param num
     * @return coth(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> coth(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.COTH, num);
    }

    /**
     * @param num
     * @return deg(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> degrees(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.DEG, num);
    }

    /**
     * @param num
     * @return exp(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> exp(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.EXP, num);
    }

    /**
     * @param num
     * @return ln(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> ln(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.LN, num);
    }

    /**
     * @param num
     * @param base
     * @return log(num, base)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> log(Expression<A> num, int base) {
        return NumberOperation.create(Double.class, Ops.MathOps.LOG, num, ConstantImpl.create(base));
    }

    /**
     * Return the greater of the given values
     *
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> max(Expression<A> left, Expression<A> right) {
        return NumberExpression.max(left, right);
    }

    /**
     * Return the smaller of the given values
     *
     * @return min(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> min(Expression<A> left, Expression<A> right) {
        return NumberExpression.min(left, right);
    }

    /**
     * @param num
     * @param exponent
     * @return power(num, exponent)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> power(Expression<A> num, int exponent) {
        return NumberOperation.create(Double.class, Ops.MathOps.POWER, num, ConstantImpl.create(exponent));
    }

    /**
     * @param num
     * @return rad(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> radians(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.RAD, num);
    }

    /**
     * Returns the random expression
     * @return random()
     */
    public static NumberExpression<Double> random() {
        return NumberExpression.random();
    }

    /**
     * Return a random number expression with the given seed
     *
     * @param seed
     * @return
     */
    public static NumberExpression<Double> random(int seed) {
        return NumberOperation.create(Double.class, MathOps.RANDOM2, ConstantImpl.create(seed));
    }

    /**
     * Round to nearest integer
     *
     * @param num
     * @return
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> round(Expression<A> num) {
        return NumberOperation.create(num.getType(), MathOps.ROUND, num);
    }

    /**
     * Round to s decimal places
     *
     * @param num
     * @param s
     * @return
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> round(Expression<A> num, int s) {
        return NumberOperation.create(num.getType(), MathOps.ROUND2, num, ConstantImpl.create(s));
    }

    /**
     * @param num
     * @return sign(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Integer> sign(Expression<A> num) {
        return NumberOperation.create(Integer.class, Ops.MathOps.SIGN, num);
    }

    /**
     * @param num
     * @return sin(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> sin(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.SIN, num);
    }

    /**
     * @param num
     * @return sinh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> sinh(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.SINH, num);
    }

    /**
     * @param num
     * @return tan(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> tan(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.TAN, num);
    }

    /**
     * @param num
     * @return tanh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> tanh(Expression<A> num) {
        return NumberOperation.create(Double.class, Ops.MathOps.TANH, num);
    }

    private MathExpressions() {}
}
