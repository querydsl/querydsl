/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Ops.MathOps;
import com.mysema.util.MathUtils;

/**
 * NumberExpression represents a numeric expression
 *
 * @author tiwe
 *
 * @param <D> Java type
 * @see java.lang.Number
 */
public abstract class NumberExpression<D extends Number & Comparable<?>> extends ComparableExpressionBase<D> {

    private static final long serialVersionUID = -5485902768703364888L;

    @Nullable
    private static final NumberExpression<Double> random = NumberOperation.create(Double.class, MathOps.RANDOM);

    /**
     * Return the greater of the given values
     *
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> max(Expression<A> left, Expression<A> right) {
        return NumberOperation.create(left.getType(), MathOps.MAX, left, right);
    }

    /**
     * Return the smaller of the given values
     *
     * @return min(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> min(Expression<A> left, Expression<A> right) {
        return NumberOperation.create(left.getType(), MathOps.MIN, left, right);
    }

    /**
     * Returns the random expression
     * @return random()
     */
    public static NumberExpression<Double> random(){
        return random;
    }

    @Nullable
    private volatile NumberExpression<D> abs, sum, min, max, floor, ceil;

    @Nullable
    private volatile NumberExpression<Double> avg, sqrt;

    @Nullable
    private volatile NumberExpression<D> negation;

    @Nullable
    private volatile NumberExpression<Integer> round;

    public NumberExpression(Class<? extends D> type) {
        super(type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public NumberExpression<D> as(Path<D> alias) {
        return NumberOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public NumberExpression<D> as(String alias) {
        return NumberOperation.create(getType(),(Operator)Ops.ALIAS, this, new PathImpl<D>(getType(), alias));
    }

    /**
     * Get the absolute value of this expression
     *
     * @return abs(this)
     */
    public NumberExpression<D> abs() {
        if (abs == null){
            abs = NumberOperation.create(getType(), MathOps.ABS, this);
        }
        return abs;
    }

    /**
     * Get the sum of this and right
     *
     * @param right
     * @return this + right
     */
    public <N extends Number & Comparable<?>> NumberExpression<D> add(Expression<N> right) {
        return NumberOperation.create(getType(), Ops.ADD, this, right);
    }

    /**
     * Get the sum of this and right
     *
     * @param right
     * @return this + right
     */
    public <N extends Number & Comparable<N>> NumberExpression<D> add(N right) {
        return NumberOperation.create(getType(), Ops.ADD, this, new ConstantImpl<N>(right));
    }

    /**
     * Get the average value of this expression (aggregation)
     *
     *  @return avg(this)
     */
    public NumberExpression<Double> avg(){
        if (avg == null){
            avg = NumberOperation.create(Double.class, Ops.AggOps.AVG_AGG, this);
        }
        return avg;
    }

    /**
     * Get the byte expression of this numeric expression
     *
     * @return this.byteValue()
     * @see java.lang.Number#byteValue()
     */
    public NumberExpression<Byte> byteValue() {
        return castToNum(Byte.class);
    }

    @SuppressWarnings("unchecked")
    private D cast(Number number) {
        Class<D> type = (Class<D>) getType();
        if (type.equals(number.getClass())) {
            return (D) number;
        } else if (Byte.class.equals(type)) {
            return (D) Byte.valueOf(number.byteValue());
        } else if (Double.class.equals(type)) {
            return (D) Double.valueOf(number.doubleValue());
        } else if (Float.class.equals(type)) {
            return (D) Float.valueOf(number.floatValue());
        } else if (Integer.class.equals(type)) {
            return (D) Integer.valueOf(number.intValue());
        } else if (Long.class.equals(type)) {
            return (D) Long.valueOf(number.longValue());
        } else if (Short.class.equals(type)) {
            return (D) Short.valueOf(number.shortValue());
        } else if (BigInteger.class.equals(type)) {
            return (D) new BigInteger(String.valueOf(number.longValue()));
        } else if (BigDecimal.class.equals(type)) {
            return (D) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Unsupported target type : " + type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public <A extends Number & Comparable<? super A>> NumberExpression<A> castToNum(Class<A> type) {
        if (type.equals(getType())){
            return (NumberExpression<A>) this;
        }else{
            return NumberOperation.create(type, Ops.NUMCAST, this, new ConstantImpl<Class<A>>(type));
        }
    }

    /**
     * Returns the smallest (closest to negative infinity)
     * <code>double</code> value that is greater than or equal to the
     * argument and is equal to a mathematical integer
     *
     * @return ceil(this)
     * @see java.lang.Math#ceil(double)
     */
    public NumberExpression<D> ceil() {
        if (ceil == null){
            ceil = NumberOperation.create(getType(), MathOps.CEIL, this);
        }
        return ceil;
    }

    /**
     * Get the result of the operation this / right
     *
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> NumberExpression<Double> divide(Expression<N> right) {
        return NumberOperation.create(Double.class, Ops.DIV, this, right);
    }

    /**
     * Get the result of the operation this / right
     *
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> NumberExpression<Double> divide(N right) {
        return NumberOperation.create(Double.class, Ops.DIV, this, new ConstantImpl<N>(right));
    }

    /**
     * Get the double expression of this numeric expression
     *
     * @return this.doubleValue()
     * @see java.lang.Number#doubleValue()
     */
    public NumberExpression<Double> doubleValue() {
        return castToNum(Double.class);
    }

    /**
     * Get the float expression of this numeric expression
     *
     * @return this.floatValue()
     * @see java.lang.Number#floatValue()
     */
    public NumberExpression<Float> floatValue() {
        return castToNum(Float.class);
    }

    /**
     * Returns the largest (closest to positive infinity)
     * <code>double</code> value that is less than or equal to the
     * argument and is equal to a mathematical integer.
     *
     * @return floor(this)
     * @see java.lang.Math#floor(double)
     */
    public NumberExpression<D> floor() {
        if (floor == null){
            floor = NumberOperation.create(getType(), MathOps.FLOOR, this);
        }
        return floor;
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this >= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression goe(A right) {
        return goe(new ConstantImpl<D>(cast(right)));
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this >= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression goe(Expression<A> right) {
        return BooleanOperation.create(Ops.GOE, this, right);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this > right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression gt(A right) {
        return gt(new ConstantImpl<D>(cast(right)));
    }

    /**
     * Create a <code>from &lt; this &lt; to</code> expression
     *
     * @param <A>
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> BooleanExpression between(A from, A to) {
        return BooleanOperation.create(Ops.BETWEEN, this, new ConstantImpl<A>(from), new ConstantImpl<A>(to));
    }

    /**
     * Create a <code>from &lt; this &lt; to</code> expression
     *
     * @param <A>
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> BooleanExpression between(Expression<A> from, Expression<A> to) {
        return BooleanOperation.create(Ops.BETWEEN, this, from, to);
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> BooleanExpression notBetween(A from, A to) {
        return between(from, to).not();
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> BooleanExpression notBetween(Expression<A> from, Expression<A> to) {
        return between(from, to).not();
    }

    /**
     * Create a <code>this &gt; right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this > right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression gt(Expression<A> right) {
        return BooleanOperation.create(Ops.GT, this, right);
    }

    /**
     * Get the int expression of this numeric expression
     *
     * @return this.intValue()
     * @see java.lang.Number#intValue()
     */
    public NumberExpression<Integer> intValue() {
        return castToNum(Integer.class);
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this <= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression loe(A right) {
        return loe(new ConstantImpl<D>(cast(right)));
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this <= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression loe(Expression<A> right) {
        return BooleanOperation.create(Ops.LOE, this, right);
    }

    /**
     * Get the long expression of this numeric expression
     *
     * @return this.longValue()
     * @see java.lang.Number#longValue()
     */
    public NumberExpression<Long> longValue() {
        return castToNum(Long.class);
    }

    /**
     * Create a <code>this &lt; right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this < right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression lt(A right) {
        return lt(new ConstantImpl<D>(cast(right)));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return this < right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression lt(Expression<A> right) {
        return BooleanOperation.create(Ops.LT, this, right);
    }

    /**
     * Get the maximum value of this expression (aggregation)
     *
     * @return max(this)
     */
    @SuppressWarnings("unchecked")
    public NumberExpression<D> max(){
        if (max == null){
            max = NumberOperation.create(getType(), (Operator)Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }

    /**
     * Get the minimum value of this expression (aggregation)
     *
     * @return min(this)
     */
    @SuppressWarnings("unchecked")
    public NumberExpression<D> min(){
        if (min == null){
            min = NumberOperation.create(getType(), (Operator)Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }

    /**
     * @param num
     * @return
     */
    public NumberExpression<D> mod(NumberExpression<D> num){
        return NumberOperation.create(getType(), Ops.MOD, this, num);
    }

    /**
     * @param num
     * @return
     */
    public NumberExpression<D> mod(D num){
        return NumberOperation.create(getType(), Ops.MOD, this, new ConstantImpl<D>(num));
    }

    /**
     * Get the result of the operation this * right
     *
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<?>> NumberExpression<D> multiply(Expression<N> right) {
        return NumberOperation.create(getType(), Ops.MULT, this, right);
    }

    /**
     * Get the result of the operation this * right
     *
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<N>> NumberExpression<D> multiply(N right) {
        return NumberOperation.create(getType(), Ops.MULT, this, new ConstantImpl<N>(right));
    }

    /**
     * Get the negation of this expression
     *
     * @return this * -1
     */
    public NumberExpression<D> negate(){
        if (negation == null){
            negation = multiply(-1);
        }
        return negation;
    }

    /**
     * Returns the closest <code>int</code> to the argument.
     *
     * @return round(this)
     * @see java.lang.Math#round(double)
     * @see java.lang.Math#round(float)
     */
    public NumberExpression<Integer> round() {
        if (round == null){
            round = NumberOperation.create(Integer.class, MathOps.ROUND, this);
        }
        return round;
    }

    /**
     * Get the short expression of this numeric expression
     *
     * @return this.shortValue()
     * @see java.lang.Number#shortValue()
     */
    public NumberExpression<Short> shortValue() {
        return castToNum(Short.class);
    }

    /**
     * Get the square root of this numeric expressions
     *
     * @return sqrt(this)
     */
    public NumberExpression<Double> sqrt(){
        if (sqrt == null){
            sqrt = NumberOperation.create(Double.class, MathOps.SQRT, this);
        }
        return sqrt;
    }

    /**
     * Get the difference of this and right
     *
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> NumberExpression<D> subtract(Expression<N> right) {
        return NumberOperation.create(getType(), Ops.SUB, this, right);
    }

    /**
     * Get the difference of this and right
     *
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> NumberExpression<D> subtract(N right) {
        return NumberOperation.create(getType(), Ops.SUB, this, new ConstantImpl<N>(right));
    }

    /**
     * Get the sum of this expression (aggregation)
     *
     * @return sum(this)
     */
    public NumberExpression<D> sum(){
        if (sum == null){
            sum = NumberOperation.create(getType(), Ops.AggOps.SUM_AGG, this);
        }
        return sum;
    }

    @Override
    public BooleanExpression in(Number... numbers){
        return super.in(convert(numbers));
    }

    @Override
    public BooleanExpression notIn(Number... numbers){
        return super.notIn(convert(numbers));
    }

    private List<D> convert(Number... numbers){
        List<D> list = new ArrayList<D>(numbers.length);
        for (int i = 0; i < numbers.length; i++){
            list.add(MathUtils.cast(numbers[i], getType()));
        }
        return list;
    }

}
