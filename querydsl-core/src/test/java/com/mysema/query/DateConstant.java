/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.Calendar;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.NumberExpression;

/**
 * @author tiwe
 *
 */
public final class DateConstant<D extends java.util.Date> extends DateExpression<D> implements Constant<D>{

    private static final long serialVersionUID = -5745611667058255826L;

    public static <D extends java.util.Date> DateExpression<D> create(D date){
        return new DateConstant<D>(date);
    }

    private final D date;

    private final Calendar calendar;

    @SuppressWarnings("unchecked")
    public DateConstant(D date) {
        super((Class<D>)date.getClass());
        this.date = (D) date.clone();
        this.calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> dayOfMonth(){
        return NumberConstant.create(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public NumberExpression<Integer> month(){
        return NumberConstant.create(calendar.get(Calendar.MONTH) + 1);
    }

    @Override
    public NumberExpression<Integer> year(){
        return NumberConstant.create(calendar.get(Calendar.YEAR));
    }

    @Override
    public NumberExpression<Integer> yearMonth(){
        return NumberConstant.create(calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1);
    }

    @Override
    public NumberExpression<Integer> dayOfWeek() {
        return NumberConstant.create(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public NumberExpression<Integer> dayOfYear() {
        return NumberConstant.create(calendar.get(Calendar.DAY_OF_YEAR));
    }

    @Override
    public NumberExpression<Integer> week() {
        return NumberConstant.create(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    @Override
    public D getConstant() {
        return date;
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

    @Override
    public int hashCode(){
        return date.hashCode();
    }

}
