/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.functions.DateTimeFunctions;

/**
 * EDate represents Date expressions
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings("unchecked")
public abstract class EDate<D extends Comparable> extends EComparable<D> {
    
    private ENumber<Integer> dayOfMonth;
    
    private ENumber<Integer> month;
    
    private ENumber<Integer> year;

    public EDate(Class<? extends D> type) {
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
