/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Visitor;
import com.mysema.util.MathUtils;

/**
 * ENumberConst represents numeric constants
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("serial")
public class ENumberConst<D extends Number & Comparable<?>> extends ENumber<D> implements Constant<D>{

    private final D constant;
    
    ENumberConst(Class<? extends D> type, D constant) {
        super(type);
        this.constant = constant;
    }

    @Override
    public EBoolean eq(D b){
        return EBoolean.create(constant.equals(b));
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
        return EBoolean.create(!constant.equals(b));
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public ENumber<D> add(Number right) {
        return ENumber.create(MathUtils.sum(constant, right));
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
        return ENumber.create(MathUtils.difference(constant, right));
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
        return ENumber.create(constant.byteValue());
    }
    
    public ENumber<Double> doubleValue() {
        return ENumber.create(constant.doubleValue());
    }
    
    public ENumber<Float> floatValue() {
        return ENumber.create(constant.floatValue());
    }
    
    public ENumber<Long> longValue() {
        return ENumber.create(constant.longValue());
    }
    
    public ENumber<Short> shortValue() {
        return ENumber.create(constant.shortValue());
    }
    
    
}
