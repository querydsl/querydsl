/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.commons.lang.Assert;
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
@SuppressWarnings("serial")
public abstract class ENumber<D extends Number & Comparable<?>> extends EComparableBase<D> {
    
    private static ENumber<Double> random;
        
    /**
     * Factory method
     * 
     * @param <T>
     * @param val
     * @return
     */    
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<?>> ENumber<T> create(T val){
        return new ENumberConst<T>((Class<T>)val.getClass(),Assert.notNull(val,"val is null"));
    }
    
    /**
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> ENumber<A> max(Expr<A> left, Expr<A> right) {
        return ONumber.create(left.getType(), MathOps.MAX, left, right);
    }
    
    /**
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
        if (random == null){
            random = ONumber.create(Double.class, MathOps.RANDOM);
        }
        return random;
    }
    
    private ENumber<D> abs, sum, min, max;
    
    private ENumber<Double> avg, sqrt;
    
    private ENumber<D> round, floor, ceil;
    
    public ENumber(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * @return abs(this)
     */
    public ENumber<D> abs() {
        if (abs == null){
            abs = ONumber.create(getType(), MathOps.ABS, this);
        }
        return abs;
    }
    
    /**
     * @param right
     * @return this + right
     */
    public ENumber<D> add(D right) {
        return ONumber.create(getType(), Ops.ADD, this, ENumber.create(right));
    }
    
    /**
     * @param right
     * @return this + right
     */
    public ENumber<D> add(Expr<D> right) {
        return ONumber.create(getType(), Ops.ADD, this, right);
    }
    
    /**
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
     * @param right
     * @return this / right
     */
    public ENumber<Double> div(D right) {
        return ONumber.create(Double.class, Ops.DIV, this, ENumber.create(right));
    }

    /**
     * @param right
     * @return this / right
     */
    public ENumber<Double> div(Expr<D> right) {
        return ONumber.create(Double.class, Ops.DIV, this, right);
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
        return goe(ENumber.create(cast(right)));
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
        return gt(ENumber.create(cast(right)));
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
        return loe(ENumber.create(cast(right)));
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
        return lt(ENumber.create(cast(right)));
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
     * @return max(this)
     */
    public ENumber<D> max(){
        if (max == null){
            max = ONumber.create(getType(), Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }

    /**
     * @return min(this)
     */
    public ENumber<D> min(){
        if (min == null){
            min = ONumber.create(getType(), Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }

    /**
     * @param right
     * @return this * right
     */
    public ENumber<D> mult(D right) {
        return ONumber.create(getType(), Ops.MULT, this, ENumber.create(right));
    }
    
    /**
     * @param right
     * @return this * right
     */
    public ENumber<D> mult(Expr<D> right) {
        return ONumber.create(getType(), Ops.MULT, this, right);
    }

    /**
     * @return round(this)
     * @see java.lang.Math#round(double)
     * @see java.lang.Math#round(float)
     */
    public ENumber<D> round() {
        if (round == null){
            round = ONumber.create(getType(), MathOps.ROUND, this); 
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
     * Returns the square root of this numeric expressions
     * @return sqrt(this)
     */
    public ENumber<Double> sqrt(){
        if (sqrt == null){
            sqrt = ONumber.create(Double.class, MathOps.SQRT, this);
        }
        return sqrt;
    }
    
    /**
     * @param right
     * @return this - right
     */
    public ENumber<D> sub(D right) {
        return ONumber.create(getType(), Ops.SUB, this, ENumber.create(right));
    }
    
    /**
     * @param right
     * @return this - right
     */
    public ENumber<D> sub(Expr<D> right) {
        return ONumber.create(getType(), Ops.SUB, this, right);
    }

    /**
     * @return sum(this)
     */
    public ENumber<D> sum(){
        if (sum == null){
            sum = ONumber.create(getType(), Ops.AggOps.SUM_AGG, this); 
        }
        return sum;
    }
    
}