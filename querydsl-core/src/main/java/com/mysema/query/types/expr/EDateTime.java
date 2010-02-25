/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import javax.annotation.Nullable;

import com.mysema.query.types.operation.ODateTime;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * EDateTime represents Date / Time expressions
 * The date representation is compatible with the Gregorian calendar.
 *
 * @param <D>
 *
 * @author tiwe
 * @see http://en.wikipedia.org/wiki/Gregorian_calendar
 */
@SuppressWarnings({"unchecked","serial"})
public abstract class EDateTime<D extends Comparable> extends EDate<D> {

    private static final EDateTime<Date> CURRENT_DATE = currentDate(Date.class);
    
    private static final EDateTime<Date> CURRENT_TIMESTAMP = currentTimestamp(Date.class);
        
    /**
     * Get an expression representing the current date as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentDate() {
        return CURRENT_DATE;
    }
    
    /**
     * Get an expression representing the current date as a EDateTime instance
     * 
     * @return
     */
    public static <T extends Comparable> EDateTime<T> currentDate(Class<T> cl) {
        return ODateTime.create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }
    
    /**
     * Get an expression representing the current time instant as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentTimestamp() {
        return CURRENT_TIMESTAMP;
    }

    /**
     * Get an expression representing the current time instant as a EDateTime instance
     * 
     * @return
     */
    public static <T extends Comparable> EDateTime<T> currentTimestamp(Class<T> cl) {
        return ODateTime.create(cl, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }
    
    @Nullable
    private volatile ENumber<Integer> hours, minutes, seconds, milliseconds;
    
    public EDateTime(Class<? extends D> type) {
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
    
}
