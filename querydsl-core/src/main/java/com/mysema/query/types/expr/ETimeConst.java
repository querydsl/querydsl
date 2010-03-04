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
public final class ETimeConst<D extends java.util.Date> extends ETime<D> implements Constant<D>{
    
    private static final long serialVersionUID = -7835941761930555480L;

    public static <D extends java.util.Date> ETime<D> create(D time){
        return new ETimeConst<D>(time);
    }
    
    private final Calendar calendar;
    
    private final D time;
    
    @SuppressWarnings("unchecked")
    public ETimeConst(D time) {
        super((Class<D>)time.getClass());
        this.calendar = Calendar.getInstance();
        this.time = (D) time.clone();
        calendar.setTime(time);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
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
