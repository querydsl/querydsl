/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.Calendar;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.TimeExpression;

/**
 * @author tiwe
 *
 */
public final class TimeConstant<D extends java.util.Date> extends TimeExpression<D> implements Constant<D>{

    private static final long serialVersionUID = -7835941761930555480L;

    public static <D extends java.util.Date> TimeExpression<D> create(D time){
        return new TimeConstant<D>(time);
    }

    private final Calendar calendar;

    private final D time;

    @SuppressWarnings("unchecked")
    public TimeConstant(D time) {
        super((Class<D>)time.getClass());
        this.calendar = Calendar.getInstance();
        this.time = (D) time.clone();
        calendar.setTime(time);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public NumberExpression<Integer> hour() {
        return NumberConstant.create(calendar.get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public NumberExpression<Integer> minute() {
        return NumberConstant.create(calendar.get(Calendar.MINUTE));
    }

    @Override
    public NumberExpression<Integer> second() {
        return NumberConstant.create(calendar.get(Calendar.SECOND));
    }

    @Override
    public NumberExpression<Integer> milliSecond() {
        return NumberConstant.create(calendar.get(Calendar.MILLISECOND));
    }

    @Override
    public D getConstant() {
        return time;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Constant){
            return ((Constant)o).getConstant().equals(time);
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return time.hashCode();
    }

}
