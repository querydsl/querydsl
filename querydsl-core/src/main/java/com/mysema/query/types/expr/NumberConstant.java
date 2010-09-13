/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Constant;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Visitor;
import com.mysema.util.MathUtils;

/**
 * ENumberConst represents numeric constants
 *
 * @author tiwe
 *
 * @param <D>
 */
public final class NumberConstant<D extends Number & Comparable<?>> extends NumberExpression<D> implements Constant<D>{

    private static final long serialVersionUID = 2958824808974260439L;

    private static final int CACHE_SIZE = 256;

    @SuppressWarnings("unchecked")
    private static final NumberExpression<Byte>[] BYTES = new NumberExpression[CACHE_SIZE];

    @SuppressWarnings("unchecked")
    private static final NumberExpression<Integer>[] INTEGERS = new NumberExpression[CACHE_SIZE];

    @SuppressWarnings("unchecked")
    private static final NumberExpression<Long>[] LONGS = new NumberExpression[CACHE_SIZE];

    @SuppressWarnings("unchecked")
    private static final NumberExpression<Short>[] SHORTS = new NumberExpression[CACHE_SIZE];

    static{
        for (int i = 0; i < CACHE_SIZE; i++){
            INTEGERS[i] = new NumberConstant<Integer>(Integer.class, Integer.valueOf(i));
            SHORTS[i] = new NumberConstant<Short>(Short.class, Short.valueOf((short)i));
            BYTES[i] = new NumberConstant<Byte>(Byte.class, Byte.valueOf((byte)i));
            LONGS[i] = new NumberConstant<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static NumberExpression<Byte> create(byte i){
        if (i >= 0 && i < CACHE_SIZE){
            return BYTES[i];
        }else{
            return new NumberConstant<Byte>(Byte.class, Byte.valueOf(i));
        }
    }

    public static NumberExpression<Integer> create(int i){
        if (i >= 0 && i < CACHE_SIZE){
            return INTEGERS[i];
        }else{
            return new NumberConstant<Integer>(Integer.class, Integer.valueOf(i));
        }
    }

    public static NumberExpression<Long> create(long i){
        if (i >= 0 && i < CACHE_SIZE){
            return LONGS[(int)i];
        }else{
            return new NumberConstant<Long>(Long.class, Long.valueOf(i));
        }
    }

    public static NumberExpression<Short> create(short i){
        if (i >= 0 && i < CACHE_SIZE){
            return SHORTS[i];
        }else{
            return new NumberConstant<Short>(Short.class, Short.valueOf(i));
        }
    }

    /**
     * Factory method
     *
     * @param <T>
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<?>> NumberExpression<T> create(T val){
        return new NumberConstant<T>((Class<T>)val.getClass(), Assert.notNull(val,"val"));
    }

    private final D constant;

    NumberConstant(Class<? extends D> type, D constant) {
        super(type);
        this.constant = constant;
    }

    @Override
    public BooleanExpression eq(D b){
        return BooleanConstant.create(constant.equals(b));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Constant ? ((Constant<?>) o).getConstant().equals(constant) : false;
    }

    @Override
    public D getConstant() {
        return constant;
    }

    @Override
    public int hashCode() {
        return constant.hashCode();
    }

    @Override
    public BooleanExpression ne(D b){
        return BooleanConstant.create(!constant.equals(b));
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<D> add(Number right) {
        return NumberConstant.create(MathUtils.sum(constant, right));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <N extends Number & Comparable<?>> NumberExpression<D> add(Expression<N> right) {
        if (right instanceof Constant){
            return add(((Constant<Number>)right).getConstant());
        }else{
            return super.add(right);
        }
    }

    @Override
    public NumberExpression<D> subtract(Number right) {
        return NumberConstant.create(MathUtils.difference(constant, right));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <N extends Number & Comparable<?>> NumberExpression<D> subtract(Expression<N> right) {
        if (right instanceof Constant){
            return subtract(((Constant<Number>)right).getConstant());
        }else{
            return super.subtract(right);
        }
    }

    public NumberExpression<Byte> byteValue() {
        return NumberConstant.create(constant.byteValue());
    }

    public NumberExpression<Double> doubleValue() {
        return NumberConstant.create(constant.doubleValue());
    }

    public NumberExpression<Float> floatValue() {
        return NumberConstant.create(constant.floatValue());
    }

    public NumberExpression<Long> longValue() {
        return NumberConstant.create(constant.longValue());
    }

    public NumberExpression<Short> shortValue() {
        return NumberConstant.create(constant.shortValue());
    }

}
