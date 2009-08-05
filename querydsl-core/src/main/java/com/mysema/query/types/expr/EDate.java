/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import com.mysema.query.annotations.Optional;
import com.mysema.query.types.operation.ODate;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

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
            dayOfMonth = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, this);
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
            month = ONumber.create(Integer.class, Ops.DateTimeOps.MONTH, this);
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
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static EDate<Date> currentDate() {
        return ODate.create(Date.class, Ops.DateTimeOps.CURRENT_DATE);
    }
    
    /**
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static <T extends Comparable> EDate<T> currentDate(Class<T> cl) {
        return ODate.create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }
}
