/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OTime;
import com.mysema.query.types.operation.Ops;

/**
 * ETime represents Time expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class ETime<D extends Comparable> extends EDateOrTime<D> {
    
    private volatile ENumber<Integer> hours, minutes, seconds;
    
    public ETime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Create a hours expression
     * 
     * @return
     * @see java.util.Date#getHours()
     */
    public ENumber<Integer> getHours(){
        if (hours == null){
            hours = ONumber.create(Integer.class, Ops.DateTimeOps.HOUR, this);
        }
        return hours;
    }
    
    /**
     * Create a minutes expression
     * 
     * @return
     * @see java.util.Date#getMinutes()
     */
    public ENumber<Integer> getMinutes(){
        if (minutes == null){
            minutes = ONumber.create(Integer.class, Ops.DateTimeOps.MINUTE, this);
        }
        return minutes;
    }
    
    /**
     * Create a seconds expression
     * 
     * @return
     * @see java.util.Date#getSeconds()
     */
    public ENumber<Integer> getSeconds(){
        if (seconds == null){
            seconds = ONumber.create(Integer.class, Ops.DateTimeOps.SECOND, this);
        }
        return seconds;
    }
    
    /**
     * Get an expression representing the current time as a ETime instance
     * 
     * @return
     */
    public static ETime<Date> currentTime() {
        return currentTime(Date.class);
    }

    /**
     * Get an expression representing the current time as a ETime instance
     * 
     * @return
     */
    public static <T extends Comparable> ETime<T> currentTime(Class<T> cl) {
        return OTime.create(cl, Ops.DateTimeOps.CURRENT_TIME);
    }
    
}
