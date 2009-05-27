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
 * 
 * @author tiwe
 * 
 * @param <D>
 */
public abstract class ENumber<D extends Number & Comparable<?>> extends EComparable<D> {

    public ENumber(Class<? extends D> type) {
        super(type);
    }

    public final ENumber<Byte> byteValue() {
        return castToNum(Byte.class);
    }

    public final ENumber<Double> doubleValue() {
        return castToNum(Double.class);
    }

    public final ENumber<Float> floatValue() {
        return castToNum(Float.class);
    }

    public final ENumber<Integer> intValue() {
        return castToNum(Integer.class);
    }

    public final ENumber<Long> longValue() {
        return castToNum(Long.class);
    }

    public final ENumber<Short> shortValue() {
        return castToNum(Short.class);
    }

    public final <A extends Number & Comparable<?>> EBoolean goe(A right) {
        return Grammar.goe(this, castTo(right, getType()));
    }

    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean goe(Expr<A> right) {
        return Grammar.goe(this, (Expr<D>) right);
    }

    public final <A extends Number & Comparable<?>> EBoolean gt(A right) {
        return Grammar.gt(this, castTo(right, getType()));
    }

    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean gt(Expr<A> right) {
        return Grammar.gt(this, (Expr<D>) right);
    }

    public final <A extends Number & Comparable<?>> EBoolean loe(A right) {
        return Grammar.loe(this, castTo(right, getType()));
    }

    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean loe(Expr<A> right) {
        return Grammar.loe(this, (Expr<D>) right);
    }

    public final <A extends Number & Comparable<?>> EBoolean lt(A right) {
        return Grammar.lt(this, castTo(right, getType()));
    }

    @SuppressWarnings("unchecked")
    public final <A extends Number & Comparable<?>> EBoolean lt(Expr<A> right) {
        return Grammar.lt(this, (Expr<D>) right);
    }

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