/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;


/**
 * @author tiwe
 *
 */
public final class EDateTimeConst<D extends java.util.Date> extends EDateTime<D> implements Constant<D>{
  
    private static final long serialVersionUID = 4578416585568476532L;

    public static <D extends java.util.Date> EDateTime<D> create(D date){
        return new EDateTimeConst<D>(date);
    }
    
    private final EDateConst<D> date;
    
    private final ETimeConst<D> time;
    
    @SuppressWarnings("unchecked")
    public EDateTimeConst(D date) {
        super((Class<D>)date.getClass());
        this.date = new EDateConst<D>(date);
        this.time = new ETimeConst<D>(date);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public ENumber<Integer> dayOfMonth(){
        return date.dayOfMonth();
    }
    
    @Override
    public ENumber<Integer> month(){
        return date.month();
    }
    
    @Override
    public ENumber<Integer> year(){
        return date.year();
    }
    
    @Override
    public ENumber<Integer> yearMonth(){
        return date.yearMonth();
    }
    
    @Override
    public ENumber<Integer> dayOfWeek() {
        return date.dayOfWeek();
    }
    
    @Override
    public ENumber<Integer> dayOfYear() {
        return date.dayOfYear();
    }
    
    @Override
    public ENumber<Integer> week() {
        return date.week();
    }
    
    @Override
    public ENumber<Integer> hour() {
        return time.hour();
    }
    
    @Override
    public ENumber<Integer> minute() {
        return time.minute();
    }
    
    @Override
    public ENumber<Integer> second() {
        return time.second();
    }
    
    @Override
    public ENumber<Integer> milliSecond() {
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
