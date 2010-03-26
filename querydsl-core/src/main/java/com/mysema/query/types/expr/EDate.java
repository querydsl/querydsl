/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

import javax.annotation.Nullable;

import com.mysema.query.types.Ops;

/**
 * EDate represents Date expressions
 * The date representation is compatible with the Gregorian calendar.
 * 
 * @param <D>
 * 
 * @author tiwe
 * @see <a href="http://en.wikipedia.org/wiki/Gregorian_calendar">Gregorian calendar</a>
 */
@SuppressWarnings({"unchecked"})
public abstract class EDate<D extends Comparable> extends EDateOrTime<D> {
    
    private static final EDate<Date> CURRENT_DATE = currentDate(Date.class);

    private static final long serialVersionUID = 6054664454254721302L;
        
    /**
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static EDate<Date> currentDate() {
        return CURRENT_DATE;
    }

    /**
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static <T extends Comparable> EDate<T> currentDate(Class<T> cl) {
        return ODate.create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }
    
    @Nullable
    private volatile ENumber<Integer> dayOfMonth, dayOfWeek, dayOfYear;
    
    @Nullable
    private volatile EDate min, max;
    
    @Nullable
    private volatile ENumber<Integer> week, month, year, yearMonth;
    
    public EDate(Class<? extends D> type) {
        super(type);
    }

    /**
     * Get a day of month expression (range 1-31)
     * 
     * @return
     */
    public ENumber<Integer> dayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, this);
        }
        return dayOfMonth;
    }

    /**
     * Get a day of week expression (range 1-7 / SUN-SAT)     
     * <p>NOT supported in JDOQL and not in Derby</p>
     * 
     * @return
     */
    public ENumber<Integer> dayOfWeek() {
        if (dayOfWeek == null){
            dayOfWeek = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK, this); 
        }
        return dayOfWeek; 
    }
    
    /**
     * Get a day of year expression (range 1-356)
     * <p>NOT supported in JDOQL and not in Derby</p>
     * 
     * @return
     */
    public ENumber<Integer> dayOfYear() {
        if (dayOfYear == null){
            dayOfYear = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, this); 
        }
        return dayOfYear;
    }
    
    /**
     * Get the maximum value of this expression (aggregation)
     * 
     * @return max(this)
     */
    public EDate<D> max(){
        if (max == null){
            max = ODate.create(getType(), Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }
    
    /**
     * Get the minimum value of this expression (aggregation)
     * 
     * @return min(this)
     */
    public EDate<D> min(){
        if (min == null){
            min = ODate.create(getType(), Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }
    
    /**
     * Get a month expression (range 1-12 / JAN-DEC)
     * 
     * @return
     */
    public ENumber<Integer> month(){
        if (month == null){
            month = ONumber.create(Integer.class, Ops.DateTimeOps.MONTH, this);
        }
        return month;
    }
    
    /**
     * Get a week expression
     * 
     * @return
     */
    public ENumber<Integer> week() {
        if (week == null){
            week = ONumber.create(Integer.class, Ops.DateTimeOps.WEEK,  this); 
        }
        return week; 
    }
    
    /**
     * Get a year expression 
     * 
     * @return
     */
    public ENumber<Integer> year(){
        if (year == null){
            year = ONumber.create(Integer.class, Ops.DateTimeOps.YEAR, this);
        }
        return year;
    }

    /**
     * Get a year / month expression 
     * 
     * @return
     */
    public ENumber<Integer> yearMonth(){
        if (yearMonth == null){
            yearMonth = ONumber.create(Integer.class, Ops.DateTimeOps.YEAR_MONTH, this);
        }
        return yearMonth;
    }
}
