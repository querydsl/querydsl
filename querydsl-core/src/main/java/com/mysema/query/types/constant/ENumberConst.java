/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.constant;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Constant;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.ENumber;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Visitor;
import com.mysema.util.MathUtils;

/**
 * ENumberConst represents numeric constants
 * 
 * @author tiwe
 *
 * @param <D>
 */
public final class ENumberConst<D extends Number & Comparable<?>> extends ENumber<D> implements Constant<D>{
    
    private static final long serialVersionUID = 2958824808974260439L;

    private static final int CACHE_SIZE = 256;
    
    @SuppressWarnings("unchecked")
    private static final ENumber<Byte>[] BYTES = new ENumber[CACHE_SIZE];
    
    @SuppressWarnings("unchecked")
    private static final ENumber<Integer>[] INTEGERS = new ENumber[CACHE_SIZE];
    
    @SuppressWarnings("unchecked")
    private static final ENumber<Long>[] LONGS = new ENumber[CACHE_SIZE];
    
    @SuppressWarnings("unchecked")
    private static final ENumber<Short>[] SHORTS = new ENumber[CACHE_SIZE];
    
    static{
        for (int i = 0; i < CACHE_SIZE; i++){
            INTEGERS[i] = new ENumberConst<Integer>(Integer.class, Integer.valueOf(i));
            SHORTS[i] = new ENumberConst<Short>(Short.class, Short.valueOf((short)i));
            BYTES[i] = new ENumberConst<Byte>(Byte.class, Byte.valueOf((byte)i));
            LONGS[i] = new ENumberConst<Long>(Long.class, Long.valueOf(i));
        }
    }
        
    public static ENumber<Byte> create(byte i){
        if (i >= 0 && i < CACHE_SIZE){
            return BYTES[i];
        }else{
            return new ENumberConst<Byte>(Byte.class, Byte.valueOf(i));
        }
    }
    
    public static ENumber<Integer> create(int i){
        if (i >= 0 && i < CACHE_SIZE){
            return INTEGERS[i];
        }else{
            return new ENumberConst<Integer>(Integer.class, Integer.valueOf(i));
        }
    }
    
    public static ENumber<Long> create(long i){
        if (i >= 0 && i < CACHE_SIZE){
            return LONGS[(int)i];
        }else{
            return new ENumberConst<Long>(Long.class, Long.valueOf(i));
        }
    }
    
    public static ENumber<Short> create(short i){
        if (i >= 0 && i < CACHE_SIZE){
            return SHORTS[i];
        }else{
            return new ENumberConst<Short>(Short.class, Short.valueOf(i));
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
    public static <T extends Number & Comparable<?>> ENumber<T> create(T val){
        return new ENumberConst<T>((Class<T>)val.getClass(), Assert.notNull(val,"val is null"));
    }
    
    private final D constant;
    
    ENumberConst(Class<? extends D> type, D constant) {
        super(type);
        this.constant = constant;
    }

    @Override
    public EBoolean eq(D b){
        return EBooleanConst.create(constant.equals(b));
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
    public EBoolean ne(D b){
        return EBooleanConst.create(!constant.equals(b));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public ENumber<D> add(Number right) {
        return ENumberConst.create(MathUtils.sum(constant, right));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <N extends Number & Comparable<?>> ENumber<D> add(Expr<N> right) {
        if (right instanceof Constant){
            return add(((Constant<Number>)right).getConstant());
        }else{
            return super.add(right);
        }
    }
    
    @Override
    public ENumber<D> subtract(Number right) {
        return ENumberConst.create(MathUtils.difference(constant, right));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <N extends Number & Comparable<?>> ENumber<D> subtract(Expr<N> right) {
        if (right instanceof Constant){
            return subtract(((Constant<Number>)right).getConstant());
        }else{
            return super.subtract(right);
        }
    }
    
    public ENumber<Byte> byteValue() {
        return ENumberConst.create(constant.byteValue());
    }
    
    public ENumber<Double> doubleValue() {
        return ENumberConst.create(constant.doubleValue());
    }
    
    public ENumber<Float> floatValue() {
        return ENumberConst.create(constant.floatValue());
    }
    
    public ENumber<Long> longValue() {
        return ENumberConst.create(constant.longValue());
    }
    
    public ENumber<Short> shortValue() {
        return ENumberConst.create(constant.shortValue());
    }
    
    
}
