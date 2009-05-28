/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.mysema.query.types.Grammar;

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
     */
    public final <A extends Number & Comparable<?>> EBoolean goe(A right) {
        return Grammar.goe(this, castTo(right, getType()));
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {
        return Grammar.goe(this, (Expr<D>) right);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean gt(A right) {
        return Grammar.gt(this, castTo(right, getType()));
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {
        return Grammar.gt(this, (Expr<D>) right);
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean loe(A right) {
        return Grammar.loe(this, castTo(right, getType()));
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {
        return Grammar.loe(this, (Expr<D>) right);
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    public final <A extends Number & Comparable<?>> EBoolean lt(A right) {
        return Grammar.lt(this, castTo(right, getType()));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param <A>
     * @param right rhs of the comparison
     * @return
     */
    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {
        return Grammar.lt(this, (Expr<D>) right);
    }

    /**
     * Cast the given number to the given type
     * 
     * @param <A>
     * @param number
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    private <A extends Number> A castTo(Number number, Class<A> type) {
        if (type.equals(number.getClass())) {
            return (A) number;
        } else if (Byte.class.equals(type)) {
            return (A) Byte.valueOf(number.byteValue());
        } else if (Double.class.equals(type)) {
            return (A) Double.valueOf(number.doubleValue());
        } else if (Float.class.equals(type)) {
            return (A) Float.valueOf(number.floatValue());
        } else if (Integer.class.equals(type)) {
            return (A) Integer.valueOf(number.intValue());
        } else if (Long.class.equals(type)) {
            return (A) Long.valueOf(number.longValue());
        } else if (Short.class.equals(type)) {
            return (A) Short.valueOf(number.shortValue());
        } else if (BigInteger.class.equals(type)) {
            return (A) new BigInteger(String.valueOf(number.longValue()));
        } else if (BigDecimal.class.equals(type)) {
            return (A) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Unsupported target type : "
                    + type.getName());
        }
    }
}