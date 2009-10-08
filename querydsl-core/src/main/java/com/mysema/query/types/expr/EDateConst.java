package com.mysema.query.types.expr;

import java.sql.Date;
import java.util.Calendar;

import com.mysema.query.types.Visitor;

@SuppressWarnings("serial")
public class EDateConst extends EDate<java.sql.Date> implements Constant<java.sql.Date>{
    
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
    public ENumber<Integer> getDayOfMonth(){
        return ENumber.create(calendar.get(Calendar.DAY_OF_MONTH));
    }
    
    @Override
    public ENumber<Integer> getMonth(){
        return ENumber.create(calendar.get(Calendar.MONTH) + 1);
    }
    
    @Override
    public ENumber<Integer> getYear(){
        return ENumber.create(calendar.get(Calendar.YEAR));
    }
    
    @Override
    public ENumber<Integer> getDayOfWeek() {
        return ENumber.create(calendar.get(Calendar.DAY_OF_WEEK));
    }
    
    @Override
    public ENumber<Integer> getDayOfYear() {
        return ENumber.create(calendar.get(Calendar.DAY_OF_YEAR));
    }
    
    @Override
    public ENumber<Integer> getWeek() {
        return ENumber.create(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    @Override
    public Date getConstant() {
        return date;
    }

}
