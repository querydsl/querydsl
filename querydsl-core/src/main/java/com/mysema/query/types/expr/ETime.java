/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

/**
 * ETime represents Time expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class ETime<D extends Comparable> extends EComparable<D> {
    
    private ENumber<Integer> hours;
    
    private ENumber<Integer> minutes;
    
    private ENumber<Integer> seconds;

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

}
