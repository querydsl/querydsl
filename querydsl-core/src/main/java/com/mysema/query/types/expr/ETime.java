/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import javax.annotation.Nullable;

import com.mysema.query.types.Ops;
import com.mysema.query.types.Ops.DateTimeOps;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.OTime;

/**
 * ETime represents Time expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public abstract class ETime<D extends Comparable> extends EDateOrTime<D> {
    
    private static final long serialVersionUID = 7360552308332457990L;

    private static final ETime<Date> CURRENT_TIME = currentTime(Date.class);
    
    @Nullable
    private volatile ENumber<Integer> hours, minutes, seconds, milliseconds;
        
    public ETime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Get a hours expression (range 0-23)
     * 
     * @return
     */
    public ENumber<Integer> hour(){
        if (hours == null){
            hours = ONumber.create(Integer.class, Ops.DateTimeOps.HOUR, this);
        }
        return hours;
    }
    
    /**
     * Get a minutes expression (range 0-59)
     * 
     * @return
     */
    public ENumber<Integer> minute(){
        if (minutes == null){
            minutes = ONumber.create(Integer.class, Ops.DateTimeOps.MINUTE, this);
        }
        return minutes;
    }
    
    /**
     * Get a seconds expression (range 0-59)
     * 
     * @return
     */
    public ENumber<Integer> second(){
        if (seconds == null){
            seconds = ONumber.create(Integer.class, Ops.DateTimeOps.SECOND, this);
        }
        return seconds;
    }
    
    
    /**
     * Get a milliseconds expression (range 0-999)
     * <p>Is always 0 in HQL and JDOQL modules</p>
     * 
     * @return
     */
    public ENumber<Integer> milliSecond(){
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
        return CURRENT_TIME; 
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
