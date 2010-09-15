/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Date;

import javax.annotation.Nullable;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;

/**
 * DateExpression represents Date expressions
 * The date representation is compatible with the Gregorian calendar.
 *
 * @param <D>
 *
 * @author tiwe
 * @see <a href="http://en.wikipedia.org/wiki/Gregorian_calendar">Gregorian calendar</a>
 */
@SuppressWarnings({"unchecked"})
public abstract class DateExpression<D extends Comparable> extends TemporalExpression<D> {

    private static final DateExpression<Date> CURRENT_DATE = currentDate(Date.class);

    private static final long serialVersionUID = 6054664454254721302L;

    /**
     * Get an expression representing the current date as a EDate instance
     *
     * @return
     */
    public static DateExpression<Date> currentDate() {
        return CURRENT_DATE;
    }

    /**
     * Get an expression representing the current date as a EDate instance
     *
     * @return
     */
    public static <T extends Comparable> DateExpression<T> currentDate(Class<T> cl) {
        return DateOperation.create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }

    @Nullable
    private volatile NumberExpression<Integer> dayOfMonth, dayOfWeek, dayOfYear;

    @Nullable
    private volatile DateExpression min, max;

    @Nullable
    private volatile NumberExpression<Integer> week, month, year, yearMonth;

    public DateExpression(Class<? extends D> type) {
        super(type);
    }

    @Override
    public DateExpression<D> as(Path<D> alias) {
        return DateOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }

    @Override
    public DateExpression as(String alias) {
        return DateOperation.create(getType(), (Operator)Ops.ALIAS, this, new PathImpl<D>(getType(), alias));
    }
    
    /**
     * Get a day of month expression (range 1-31)
     *
     * @return
     */
    public NumberExpression<Integer> dayOfMonth(){
        if (dayOfMonth == null){
            dayOfMonth = NumberOperation.create(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, this);
        }
        return dayOfMonth;
    }

    /**
     * Get a day of week expression (range 1-7 / SUN-SAT)
     * <p>NOT supported in JDOQL and not in Derby</p>
     *
     * @return
     */
    public NumberExpression<Integer> dayOfWeek() {
        if (dayOfWeek == null){
            dayOfWeek = NumberOperation.create(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK, this);
        }
        return dayOfWeek;
    }

    /**
     * Get a day of year expression (range 1-356)
     * <p>NOT supported in JDOQL and not in Derby</p>
     *
     * @return
     */
    public NumberExpression<Integer> dayOfYear() {
        if (dayOfYear == null){
            dayOfYear = NumberOperation.create(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, this);
        }
        return dayOfYear;
    }

    /**
     * Get the maximum value of this expression (aggregation)
     *
     * @return max(this)
     */
    public DateExpression<D> max(){
        if (max == null){
            max = DateOperation.create(getType(), Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }

    /**
     * Get the minimum value of this expression (aggregation)
     *
     * @return min(this)
     */
    public DateExpression<D> min(){
        if (min == null){
            min = DateOperation.create(getType(), Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }

    /**
     * Get a month expression (range 1-12 / JAN-DEC)
     *
     * @return
     */
    public NumberExpression<Integer> month(){
        if (month == null){
            month = NumberOperation.create(Integer.class, Ops.DateTimeOps.MONTH, this);
        }
        return month;
    }

    /**
     * Get a week expression
     *
     * @return
     */
    public NumberExpression<Integer> week() {
        if (week == null){
            week = NumberOperation.create(Integer.class, Ops.DateTimeOps.WEEK,  this);
        }
        return week;
    }

    /**
     * Get a year expression
     *
     * @return
     */
    public NumberExpression<Integer> year(){
        if (year == null){
            year = NumberOperation.create(Integer.class, Ops.DateTimeOps.YEAR, this);
        }
        return year;
    }

    /**
     * Get a year / month expression
     *
     * @return
     */
    public NumberExpression<Integer> yearMonth(){
        if (yearMonth == null){
            yearMonth = NumberOperation.create(Integer.class, Ops.DateTimeOps.YEAR_MONTH, this);
        }
        return yearMonth;
    }
}
