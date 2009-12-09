/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.sql.Date;
import java.util.Calendar;

import com.mysema.query.types.Visitor;

/**
 * @author tiwe
 *
 */
@SuppressWarnings("serial")
public class EDateConst extends EDate<java.sql.Date> implements Constant<java.sql.Date>{
 
    public static EDate<java.sql.Date> create(java.sql.Date date){
        return new EDateConst(date);
    }
    
    private final Date date;
    
    private final Calendar calendar;
    
    public EDateConst(java.sql.Date date) {
        super(java.sql.Date.class);
        this.date = date;
        this.calendar = Calendar.getInstance();
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
    public Date getConstant() {
        return date;
    }

}
