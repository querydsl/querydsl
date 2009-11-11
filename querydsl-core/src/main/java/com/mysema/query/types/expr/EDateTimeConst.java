/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Calendar;
import java.util.Date;

import com.mysema.query.types.Visitor;

@SuppressWarnings("serial")
public class EDateTimeConst extends EDateTime<java.util.Date> implements Constant<java.util.Date>{
    
    private final Calendar calendar;
    
    private final java.util.Date date;
    
    public EDateTimeConst(java.util.Date date) {
        super(java.util.Date.class);
        this.calendar = Calendar.getInstance();
        this.date = date;
        calendar.setTime(date);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public ENumber<Integer> getDayOfMonth(){
        return ENumber.__create(calendar.get(Calendar.DAY_OF_MONTH));
    }
    
    @Override
    public ENumber<Integer> getMonth(){
        return ENumber.__create(calendar.get(Calendar.MONTH) + 1);
    }
    
    @Override
    public ENumber<Integer> getYear(){
        return ENumber.__create(calendar.get(Calendar.YEAR));
    }
    
    @Override
    public ENumber<Integer> getDayOfWeek() {
        return ENumber.__create(calendar.get(Calendar.DAY_OF_WEEK));
    }
    
    @Override
    public ENumber<Integer> getDayOfYear() {
        return ENumber.__create(calendar.get(Calendar.DAY_OF_YEAR));
    }
    
    @Override
    public ENumber<Integer> getWeek() {
        return ENumber.__create(calendar.get(Calendar.WEEK_OF_YEAR));
    }
    
    @Override
    public ENumber<Integer> getHour() {
        return ENumber.__create(calendar.get(Calendar.HOUR_OF_DAY));
    }
    
    @Override
    public ENumber<Integer> getMinute() {
        return ENumber.__create(calendar.get(Calendar.MINUTE));
    }
    
    @Override
    public ENumber<Integer> getSecond() {
        return ENumber.__create(calendar.get(Calendar.SECOND));
    }
    
    @Override
    public ENumber<Integer> getMilliSecond() {
        return ENumber.__create(calendar.get(Calendar.MILLISECOND));
    }

    @Override
    public Date getConstant() {
        return date;
    }

}
