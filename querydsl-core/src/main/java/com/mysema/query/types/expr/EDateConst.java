/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Calendar;

import com.mysema.query.types.Constant;
import com.mysema.query.types.Visitor;

/**
 * @author tiwe
 *
 */
public final class EDateConst<D extends java.util.Date> extends EDate<D> implements Constant<D>{

    private static final long serialVersionUID = -5745611667058255826L;

    public static <D extends java.util.Date> EDate<D> create(D date){
        return new EDateConst<D>(date);
    }

    private final D date;

    private final Calendar calendar;

    @SuppressWarnings("unchecked")
    public EDateConst(D date) {
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
    public ENumber<Integer> dayOfMonth(){
        return ENumberConst.create(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public ENumber<Integer> month(){
        return ENumberConst.create(calendar.get(Calendar.MONTH) + 1);
    }

    @Override
    public ENumber<Integer> year(){
        return ENumberConst.create(calendar.get(Calendar.YEAR));
    }

    @Override
    public ENumber<Integer> yearMonth(){
        return ENumberConst.create(calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1);
    }

    @Override
    public ENumber<Integer> dayOfWeek() {
        return ENumberConst.create(calendar.get(Calendar.DAY_OF_WEEK));
    }

    @Override
    public ENumber<Integer> dayOfYear() {
        return ENumberConst.create(calendar.get(Calendar.DAY_OF_YEAR));
    }

    @Override
    public ENumber<Integer> week() {
        return ENumberConst.create(calendar.get(Calendar.WEEK_OF_YEAR));
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
