/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Date;

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
@SuppressWarnings({"unchecked","serial"})
public abstract class EDate<D extends Comparable> extends EDateOrTime<D> {
    
    private static final EDate<Date> currentDate = currentDate(Date.class);
        
    /**
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static EDate<Date> currentDate() {
        return currentDate;
    }

    /**
     * Get an expression representing the current date as a EDate instance
     * 
     * @return
     */
    public static <T extends Comparable> EDate<T> currentDate(Class<T> cl) {
        return ODate.create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }
    
    private volatile ENumber<Integer> dayOfMonth, dayOfWeek, dayOfYear;
    
    private volatile ENumber<Integer> week, month, year, yearMonth;
    
    public EDate(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Get a day of month expression (range 1-31)
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
     * Get a day of week expression (range 1-7 / SUN-SAT)     
     * <p>NOT supported in JDOQL and not in Derby</p>
     * 
     * @return
     */
    public ENumber<Integer> getDayOfWeek() {
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
    public ENumber<Integer> getDayOfYear() {
        if (dayOfYear == null){
            dayOfYear = ONumber.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, this); 
        }
        return dayOfYear;
    }
    
    /**
     * Get a month expression (range 1-12)
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
     * Get a week expression
     * 
     * @return
     */
    public ENumber<Integer> getWeek() {
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
    public ENumber<Integer> getYear(){
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
    public ENumber<Integer> getYearMonth(){
        if (yearMonth == null){
            yearMonth = ONumber.create(Integer.class, Ops.DateTimeOps.YEAR_MONTH, this);
        }
        return yearMonth;
    }
}
