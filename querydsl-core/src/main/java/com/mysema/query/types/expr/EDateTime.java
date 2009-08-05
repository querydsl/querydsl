/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import com.mysema.query.annotations.Optional;
import com.mysema.query.types.operation.ODateTime;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * EDateTime represents Date / Time expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class EDateTime<D extends Comparable> extends EComparable<D> {

    private ENumber<Integer> dayOfMonth;
    
    private ENumber<Integer> month;
    
    private ENumber<Integer> year;
    
    private ENumber<Integer> hours;
    
    private ENumber<Integer> minutes;
    
    private ENumber<Integer> seconds;
    
    public EDateTime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Create a day of month expression
     * 
     * @return
     * @see java.util.Date#getDate()
     */
    public ENumber<Integer> getDayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, this);
        }
        return dayOfMonth;
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
     * Create a month expression
     * 
     * @return
     * @see java.util.Date#getMonth()
     */
    public ENumber<Integer> getMonth(){
        if (month == null){
            month = ONumber.create(Integer.class, Ops.DateTimeOps.MONTH, this);
        }
        return month;
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
     * Create a year expression
     * 
     * @return
     * @see java.util.Date#getYear()
     */
    public ENumber<Integer> getYear(){
        if (year == null){
            year = ONumber.create(Integer.class, Ops.DateTimeOps.YEAR, this);
        }
        return year;
    }
    
    @Optional
    public ENumber<Integer> getDayOfWeek() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK, this);
    }

    @Optional
    public ENumber<Integer> getDayOfYear() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, this);
    }

    @Optional
    public ENumber<Integer> getWeek() {
        return ONumber.create(Integer.class, Ops.DateTimeOps.WEEK,  this);
    }
    
    /**
     * Get an expression representing the current date as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentDate() {
        return ODateTime.create(Date.class, Ops.DateTimeOps.CURRENT_DATE);
    }
    
    /**
     * Get an expression representing the current time instant as a EDateTime instance
     * 
     * @return
     */
    public static EDateTime<Date> currentTimestamp() {
        return ODateTime.create(Date.class, Ops.DateTimeOps.CURRENT_TIMESTAMP);
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
