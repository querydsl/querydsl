/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Calendar;

import com.mysema.query.types.Visitor;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class EDateTimeConst<D extends java.util.Date> extends EDateTime<D> implements Constant<D>{
  
    public static <D extends java.util.Date> EDateTime<D> create(D date){
        return new EDateTimeConst<D>(date);
    }
    
    private final Calendar calendar;
    
    private final D date;
    
    @SuppressWarnings("unchecked")
    public EDateTimeConst(D date) {
        super((Class<D>)date.getClass());
        this.calendar = Calendar.getInstance();
        this.date = date;
        calendar.setTime(date);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
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
    public ENumber<Integer> hour() {
        return ENumberConst.create(calendar.get(Calendar.HOUR_OF_DAY));
    }
    
    @Override
    public ENumber<Integer> minute() {
        return ENumberConst.create(calendar.get(Calendar.MINUTE));
    }
    
    @Override
    public ENumber<Integer> second() {
        return ENumberConst.create(calendar.get(Calendar.SECOND));
    }
    
    @Override
    public ENumber<Integer> milliSecond() {
        return ENumberConst.create(calendar.get(Calendar.MILLISECOND));
    }

    @Override
    public D getConstant() {
        return date;
    }

}
