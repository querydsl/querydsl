/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;


/**
 * ENumber represents a numeric expression
 * 
 * @author tiwe
 * 
 * @param <D> Java type
 * @see java.lang.Number
 */
public abstract class ENumber<D extends Number & Comparable<?>> extends EComparable<D> {

    public ENumber(Class<? extends D> type) {
        super(type);
    }

    /**
     * Get the byte expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#byteValue()
     */
    public final ENumber<Byte> byteValue() {
        return castToNum(Byte.class);
    }

    /**
     * Get the double expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#doubleValue()
     */
    public final ENumber<Double> doubleValue() {
        return castToNum(Double.class);
    }

    /**
     * Get the float expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#floatValue()
     */
    public final ENumber<Float> floatValue() {
        return castToNum(Float.class);
    }

    /**
     * Get the int expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#intValue()
     */
    public final ENumber<Integer> intValue() {
        return castToNum(Integer.class);
    }

    /**
     * Get the long expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#longValue()
     */
    public final ENumber<Long> longValue() {
        return castToNum(Long.class);
    }

    /**
     * Get the short expression of this numeric expression
     * 
     * @return
     * @see java.lang.Number#shortValue()
     */
    public final ENumber<Short> shortValue() {
        return castToNum(Short.class);
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean goe(A right) {
        return goe(EConstant.create(cast(right)));
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {
        return new OBoolean(Ops.GOE, this, right);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean gt(A right) {
        return gt(EConstant.create(cast(right)));
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {
        return new OBoolean(Ops.GT, this, right);
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean loe(A right) {
        return loe(EConstant.create(cast(right)));
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {
        return new OBoolean(Ops.LOE, this, right);
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean lt(A right) {
        return lt(EConstant.create(cast(right)));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {
        return new OBoolean(Ops.LT, this, right);
    }

    @Override
    public <A extends Number & Comparable<? super A>> ENumber<A> castToNum(Class<A> type) {
        if (type.equals(getType())){
            return (ENumber<A>) this;
        }else{
            return super.castToNum(type);
        }
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
}