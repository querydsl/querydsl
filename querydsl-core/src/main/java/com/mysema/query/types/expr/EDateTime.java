/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

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
            dayOfMonth = DateTimeFunctions.dayOfMonth(this);
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
            hours = DateTimeFunctions.hours(this);
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
            minutes = DateTimeFunctions.minutes(this);
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
            month = DateTimeFunctions.month(this);
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
            seconds = DateTimeFunctions.seconds(this);
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
            year = DateTimeFunctions.year(this);
        }
        return year;
    }

}
