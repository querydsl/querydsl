/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.types.dsl;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Ops.MathOps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Extended Math expressions, supported by the SQL module
 *
 * @author tiwe
 *
 */
public final class MathExpressions {

    /**
     * Create a {@code acos(num)} expression
     *
     * <p>Returns the principal value of the arc cosine of num, expressed in radians.</p>
     *
     * @param num numeric expression
     * @return acos(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> acos(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.ACOS, num);
    }

    /**
     * Create a {@code asin(num)} expression
     *
     * <p>Returns the principal value of the arc sine of num, expressed in radians.</p>
     *
     * @param num numeric expression
     * @return asin(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> asin(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.ASIN, num);
    }

    /**
     * Create a {@code atan(num)} expression
     *
     * <p>Returns the principal value of the arc tangent of num, expressed in radians.</p>
     *
     * @param num numeric expression
     * @return atan(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> atan(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.ATAN, num);
    }

    /**
     * Create a {@code cos(num)} expression
     *
     * <p>Returns the cosine of an angle of num radians.</p>
     *
     * @param num numeric expression
     * @return cos(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cos(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.COS, num);
    }

    /**
     * Create a {@code cosh(num)} expression
     *
     * <p>Returns the hyperbolic cosine of num radians.</p>
     *
     * @param num numeric expression
     * @return cosh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cosh(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.COSH, num);
    }

    /**
     * Create a {@code cot(num)} expression
     *
     * <p>Returns the cotangent of num.</p>
     *
     * @param num numeric expression
     * @return cot(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> cot(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.COT, num);
    }

    /**
     * Create a {@code coth(num)} expression
     *
     * <p>Returns the hyperbolic cotangent of num.</p>
     *
     * @param num numeric expression
     * @return coth(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> coth(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.COTH, num);
    }

    /**
     * Create a {@code deg(num)} expression
     *
     * <p>Convert radians to degrees.</p>
     *
     * @param num numeric expression
     * @return deg(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> degrees(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.DEG, num);
    }

    /**
     * Create a {@code exp(num)} expression
     *
     * <p>Returns the base-e exponential function of num, which is e raised to the power num.</p>
     *
     * @param num numeric expression
     * @return exp(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> exp(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.EXP, num);
    }

    /**
     * Create a {@code ln(num)} expression
     *
     * <p>Returns the natural logarithm of num.</p>
     *
     * @param num numeric expression
     * @return ln(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> ln(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.LN, num);
    }

    /**
     * Create a {@code log(num, base)} expression
     *
     * @param num numeric expression
     * @param base base
     * @return log(num, base)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> log(Expression<A> num, int base) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.LOG, num, ConstantImpl.create(base));
    }

    /**
     * Create a {@code max(left, right)} expression
     *
     * <p>Return the greater of the given values</p>
     *
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> max(Expression<A> left, Expression<A> right) {
            return NumberExpression.max(left, right);
    }
    /**
     * Return the greater one of the given values
     *
     * @return max(left, right)
     */
    public static Date max(Date left, Date right) throws ParseException {
        if (left ==null || right ==null) return null;
        if (left.getTime()>right.getTime()){
            return left;
        }else{
            return right;
        }
    }

    /**
     * Create a {@code min(left, right)} expression
     *
     * <p>Return the smaller of the given values</p>
     *
     * @return min(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> min(Expression<A> left, Expression<A> right) {
        return NumberExpression.min(left, right);
    }
    /**
     * Return the smaller one  of the given date
     * @return min(left, right)
     * date input as date
     */
    public static Date min(Date left, Date right) throws ParseException {
        if (left ==null || right ==null) return null;
        if (left.getTime()>right.getTime()){
            return right;
        }else{
            return left;
        }
    }

    /**
     * Create a {@code power(num, exponent)} expression
     *
     * <p>Returns num raised to the power exponent</p>
     *
     * @param num  numeric expression
     * @param exponent exponent
     * @return power(num, exponent)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> power(Expression<A> num, int exponent) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.POWER, num, ConstantImpl.create(exponent));
    }

    /**
     * Create a {@code rad(num)} expression
     *
     * <p>Converts degrees to radians</p>
     *
     * @param num numeric expression
     * @return rad(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> radians(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.RAD, num);
    }

    /**
     * Returns the random expression
     *
     * @return random()
     */
    public static NumberExpression<Double> random() {
        return NumberExpression.random();
    }

    /**
     * Return a random number expression with the given seed
     *
     * @param seed seed
     * @return random(seed)
     */
    public static NumberExpression<Double> random(int seed) {
        return Expressions.numberOperation(Double.class, MathOps.RANDOM2, ConstantImpl.create(seed));
    }

    /**
     * Round to nearest integer
     *
     * @param num numeric expression
     * @return round(this)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> round(Expression<A> num) {
        return Expressions.numberOperation(num.getType(), MathOps.ROUND, num);
    }

    /**
     * Round to s decimal places
     *
     * @param num numeric expression
     * @param s decimal places
     * @return round(num, s)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> round(Expression<A> num, int s) {
        return Expressions.numberOperation(num.getType(), MathOps.ROUND2, num, ConstantImpl.create(s));
    }

    /**
     * Create a {@code sign(num)} expression
     *
     * <p>Returns the positive (+1), zero (0), or negative (-1) sign of num.</p>
     *
     * @param num numeric expression
     * @return sign(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Integer> sign(Expression<A> num) {
        return Expressions.numberOperation(Integer.class, Ops.MathOps.SIGN, num);
    }

    /**
     * Create a {@code sin(num)} expression
     *
     * <p>Returns the sine of an angle of num radians.</p>
     *
     * @param num numeric expression
     * @return sin(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> sin(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.SIN, num);
    }

    /**
     * Create a {@code sinh(num)} expression
     *
     * <p>Returns the hyperbolic sine of num radians.</p>
     *
     * @param num numeric expression
     * @return sinh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> sinh(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.SINH, num);
    }

    /**
     * Create a {@code tan(num)} expression
     *
     * <p>Returns the tangent of an angle of num radians.</p>
     *
     * @param num numeric expression
     * @return tan(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> tan(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.TAN, num);
    }

    /**
     * Create a {@code tanh(num)} expression
     *
     * <p>Returns the hyperbolic tangent of num radians.</p>
     *
     * @param num numeric expression
     * @return tanh(num)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<Double> tanh(Expression<A> num) {
        return Expressions.numberOperation(Double.class, Ops.MathOps.TANH, num);
    }

    private MathExpressions() { }
}
