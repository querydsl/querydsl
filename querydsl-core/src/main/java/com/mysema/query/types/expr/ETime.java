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
@SuppressWarnings({"unchecked","serial"})
public abstract class ETime<D extends Comparable> extends EDateOrTime<D> {
    
    private volatile ENumber<Integer> hours, minutes, seconds, milliseconds;
    
    public static ETime<java.sql.Time> create(java.sql.Time time){
        return new ETimeConst(time);
    }
    
    public ETime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Create a hours expression (range 0-23)
     * 
     * @return
     */
    public ENumber<Integer> getHour(){
        if (hours == null){
            hours = ONumber.create(Integer.class, Ops.DateTimeOps.HOUR, this);
        }
        return hours;
    }
    
    /**
     * Create a minutes expression (range 0-59)
     * 
     * @return
     */
    public ENumber<Integer> getMinute(){
        if (minutes == null){
            minutes = ONumber.create(Integer.class, Ops.DateTimeOps.MINUTE, this);
        }
        return minutes;
    }
    
    /**
     * Create a seconds expression (range 0-59)
     * 
     * @return
     */
    public ENumber<Integer> getSecond(){
        if (seconds == null){
            seconds = ONumber.create(Integer.class, Ops.DateTimeOps.SECOND, this);
        }
        return seconds;
    }
    
    
    /**
     * Create a milliseconds expression (range 0-999)
     * <p>Is always 0 in HQL and JDOQL modules</p>
     * 
     * @return
     */
    public ENumber<Integer> getMilliSecond(){
        if (milliseconds == null){
            milliseconds = ONumber.create(Integer.class, Ops.DateTimeOps.MILLISECOND, this);
        }
        return milliseconds;
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
