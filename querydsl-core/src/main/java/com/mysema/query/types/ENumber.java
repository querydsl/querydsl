/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.operation.Ops.MathOps;

/**
 * ENumber represents a numeric expression
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 * @see java.lang.Number
 */
public abstract class ENumber<D extends Number & Comparable<?>> extends EComparableBase<D> {
   
    private static final long serialVersionUID = -5485902768703364888L;

    @Nullable
    private static final ENumber<Double> random = ONumber.create(Double.class, MathOps.RANDOM);
        
    /**
     * Return the greater of the given values
     * 
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> ENumber<A> max(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), MathOps.MAX, left, right);
    }
    
    /**
     * Return the smaller of the given values
     * 
     * @return min(left, right)
     */
    public static <A extends Number & Comparable<?>> ENumber<A> min(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), MathOps.MIN, left, right);
    }
    
    /**
     * Returns the random expression
     * @return random()
     */
    public static ENumber<Double> random(){
        return random;
    }
    
    @Nullable
    private volatile ENumber<D> abs, sum, min, max, floor, ceil;
    
    @Nullable
    private volatile ENumber<Double> avg, sqrt;
    
    @Nullable
    private volatile ENumber<D> negation;
    
    @Nullable
    private volatile ENumber<Integer> round;
    
    public ENumber(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Get the absolute value of this expression
     * 
     * @return abs(this)
     */
    public ENumber<D> abs() {
        if (abs == null){
            abs = ONumber.create(getType(), MathOps.ABS, this);
        }
        return abs;
    }
    
    /**
     * Get the sum of this and right
     * 
     * @param right
     * @return this + right
     */
    public <N extends Number & Comparable<?>> ENumber<D> add(Expr<N> right) {
        return ONumber.create(getType(), Ops.ADD, this, right);
    }
    
    /**
     * Get the sum of this and right
     * 
     * @param right
     * @return this + right
     */
    public <N extends Number & Comparable<N>> ENumber<D> add(N right) {
        return ONumber.create(getType(), Ops.ADD, this, ENumberConst.create(right));
    }

    /**
     * Get the average value of this expression (aggregation)
     * 
     *  @return avg(this)
     */
    public ENumber<Double> avg(){
        if (avg == null){
            avg = ONumber.create(Double.class, Ops.AggOps.AVG_AGG, this);            
        }
        return avg;
    }

    /**
     * Get the byte expression of this numeric expression
     * 
     * @return this.byteValue()
     * @see java.lang.Number#byteValue()
     */
    public ENumber<Byte> byteValue() {
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
    public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type) {
        if (type.equals(getType())){
            return (ENumber<A>) this;
        }else{
//            return super.castToNum(type);
            return ONumber.create(type, Ops.NUMCAST, this, ExprConst.create(type));
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
    public ENumber<D> ceil() {
        if (ceil == null){
            ceil = ONumber.create(getType(), MathOps.CEIL, this); 
        }
        return ceil;
    }

    /**
     * Get the result of the operation this / right
     * 
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> ENumber<Double> divide(Expr<N> right) {
        return ONumber.create(Double.class, Ops.DIV, this, right);
    }

    /**
     * Get the result of the operation this / right
     * 
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> ENumber<Double> divide(N right) {
        return ONumber.create(Double.class, Ops.DIV, this, ENumberConst.create(right));
    }


    /**
     * Get the double expression of this numeric expression
     * 
     * @return this.doubleValue()
     * @see java.lang.Number#doubleValue()
     */
    public ENumber<Double> doubleValue() {
        return castToNum(Double.class);
    }

    /**
     * Get the float expression of this numeric expression
     * 
     * @return this.floatValue()
     * @see java.lang.Number#floatValue()
     */
    public ENumber<Float> floatValue() {
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
    public ENumber<D> floor() {
        if (floor == null){
            floor = ONumber.create(getType(), MathOps.FLOOR, this);
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
    public final <A extends Number & Comparable<?>> EBoolean goe(A right) {
        return goe(ENumberConst.create(cast(right)));
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return this >= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {
        return OBoolean.create(Ops.GOE, this, right);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return this > right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean gt(A right) {
        return gt(ENumberConst.create(cast(right)));
    }
    
    /**
     * Create a <code>from &lt; this &lt; to</code> expression
     * 
     * @param <A>
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean between(A from, A to) {
        return OBoolean.create(Ops.BETWEEN, this, ExprConst.create(from), ExprConst.create(to));
    }

    /**
     * Create a <code>from &lt; this &lt; to</code> expression
     * 
     * @param <A>
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean between(Expr<A> from, Expr<A> to) {
        return OBoolean.create(Ops.BETWEEN, this, from, to);
    }
    
    /**
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean notBetween(A from, A to) {
        return between(from, to).not();
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean notBetween(Expr<A> from, Expr<A> to) {
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
    public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {
        return OBoolean.create(Ops.GT, this, right);
    }

    /**
     * Get the int expression of this numeric expression
     * 
     * @return this.intValue()
     * @see java.lang.Number#intValue()
     */
    public ENumber<Integer> intValue() {
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
    public final <A extends Number & Comparable<?>> EBoolean loe(A right) {
        return loe(ENumberConst.create(cast(right)));
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return this <= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {
        return OBoolean.create(Ops.LOE, this, right);
    }

    /**
     * Get the long expression of this numeric expression
     * 
     * @return this.longValue()
     * @see java.lang.Number#longValue()
     */
    public ENumber<Long> longValue() {
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
    public final <A extends Number & Comparable<?>> EBoolean lt(A right) {
        return lt(ENumberConst.create(cast(right)));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return this < right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {
        return OBoolean.create(Ops.LT, this, right);
    }

    /**
     * Get the maximum value of this expression (aggregation)
     * 
     * @return max(this)
     */
    @SuppressWarnings("unchecked")
    public ENumber<D> max(){
        if (max == null){
            max = ONumber.create(getType(), (Operator)Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }

    /**
     * Get the minimum value of this expression (aggregation)
     * 
     * @return min(this)
     */
    @SuppressWarnings("unchecked")
    public ENumber<D> min(){
        if (min == null){
            min = ONumber.create(getType(), (Operator)Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }
    
    /**
     * Get the result of the operation this * right
     * 
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<?>> ENumber<D> multiply(Expr<N> right) {
        return ONumber.create(getType(), Ops.MULT, this, right);
    }

    /**
     * Get the result of the operation this * right
     * 
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<N>> ENumber<D> multiply(N right) {
        return ONumber.create(getType(), Ops.MULT, this, ENumberConst.create(right));
    }
    
    /**
     * Get the negation of this expression
     * 
     * @return this * -1
     */
    public ENumber<D> negate(){
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
    public ENumber<Integer> round() {
        if (round == null){
            round = ONumber.create(Integer.class, MathOps.ROUND, this); 
        }
        return round;
    }
    
    /**
     * Get the short expression of this numeric expression
     * 
     * @return this.shortValue()
     * @see java.lang.Number#shortValue()
     */
    public ENumber<Short> shortValue() {
        return castToNum(Short.class);
    }
    
    /**
     * Get the square root of this numeric expressions
     * 
     * @return sqrt(this)
     */
    public ENumber<Double> sqrt(){
        if (sqrt == null){
            sqrt = ONumber.create(Double.class, MathOps.SQRT, this);
        }
        return sqrt;
    }

    /**
     * Get the difference of this and right
     * 
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> ENumber<D> subtract(Expr<N> right) {
        return ONumber.create(getType(), Ops.SUB, this, right);
    }
       
    /**
     * Get the difference of this and right
     * 
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> ENumber<D> subtract(N right) {
        return ONumber.create(getType(), Ops.SUB, this, ENumberConst.create(right));
    }
    
    /**
     * Get the sum of this expression (aggregation)
     * 
     * @return sum(this)
     */
    public ENumber<D> sum(){
        if (sum == null){
            sum = ONumber.create(getType(), Ops.AggOps.SUM_AGG, this); 
        }
        return sum;
    }
    
}