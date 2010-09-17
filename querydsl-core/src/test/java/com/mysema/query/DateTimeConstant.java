/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;

/**
 * @author tiwe
 *
 */
final class DateTimeConstant<D extends java.util.Date> extends DateTimeExpression<D> implements Constant<D>{

    private static final long serialVersionUID = 4578416585568476532L;

    public static <D extends java.util.Date> DateTimeExpression<D> create(D date){
        return new DateTimeConstant<D>(date);
    }

    private final DateConstant<D> date;

    private final TimeConstant<D> time;

    @SuppressWarnings("unchecked")
    public DateTimeConstant(D date) {
        super((Class<D>)date.getClass());
        this.date = new DateConstant<D>(date);
        this.time = new TimeConstant<D>(date);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> dayOfMonth(){
        return date.dayOfMonth();
    }

    @Override
    public NumberExpression<Integer> month(){
        return date.month();
    }

    @Override
    public NumberExpression<Integer> year(){
        return date.year();
    }

    @Override
    public NumberExpression<Integer> yearMonth(){
        return date.yearMonth();
    }

    @Override
    public NumberExpression<Integer> dayOfWeek() {
        return date.dayOfWeek();
    }

    @Override
    public NumberExpression<Integer> dayOfYear() {
        return date.dayOfYear();
    }

    @Override
    public NumberExpression<Integer> week() {
        return date.week();
    }

    @Override
    public NumberExpression<Integer> hour() {
        return time.hour();
    }

    @Override
    public NumberExpression<Integer> minute() {
        return time.minute();
    }

    @Override
    public NumberExpression<Integer> second() {
        return time.second();
    }

    @Override
    public NumberExpression<Integer> milliSecond() {
        return time.milliSecond();
    }

    @Override
    public D getConstant() {
        return date.getConstant();
    }

    @Override
    public int hashCode(){
        return date.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Constant){
            return ((Constant)o).getConstant().equals(date);
        }else{
            return false;
        }
    }

}
