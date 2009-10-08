/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import com.mysema.query.types.operation.ODateTime;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * EDateTime represents Date / Time expressions
 * <p>The representation aims to be ISO 8601 compliant</p>
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked","serial"})
public abstract class EDateTime<D extends Comparable> extends EDateOrTime<D> {

    private volatile ENumber<Integer> dayOfMonth, month, year, hours, minutes, seconds, milliseconds;
    
    public static EDateTime<java.util.Date> create(java.util.Date date){
        return new EDateTimeConst(date);
    }
    
    public EDateTime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Create a day of month expression (range 1-31)
     * 
     * @return
     */
    public ENumber<Integer> getDayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, this);
        }
        return dayOfMonth;
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
     * Create a month expression (range 1-12)
     * 
     * @return
     */
    public ENumber<Integer> getMonth(){
        if (month == null){
            month = ONumber.create(Integer.class, Ops.DateTimeOps.MONTH, this);
        }
        return month;
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
     * Create a year expression
     * 
     * @return
     */
    public ENumber<Integer> getYear(){
        if (year == null){
            year = ONumber.create(Integer.class, Ops.DateTimeOps.YEAR, this);
        }
        return year;
    }
    
    /**
     * Create a day of week expression (range 1-7 /  MON-SUN)
     * <p>NOT supported in JDOQL and not in Derby</p>
     * 
     * @return
     */
    public ENumber<Integer> getDayOfWeek() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK, this);
    }
    
    /**
     * Create a day of year expression (range 1-356)
     * <p>NOT supported in JDOQL and not in Derby</p>
     * 
     * @return
     */
    public ENumber<Integer> getDayOfYear() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, this);
    }

    /**
     * Create a week expression
     * 
     * @return
     */
    public ENumber<Integer> getWeek() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.WEEK,  this);
    }
    
    /**
     * Get an expression representing the current date as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentDate() {
        return currentDate(Date.class);
    }
    
    /**
     * Get an expression representing the current time instant as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentTimestamp() {
        return currentTimestamp(Date.class);
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
    public static <T extends Comparable> EDateTime<T> currentTimestamp(Class<T> cl) {
        return ODateTime.create(cl, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }
}
