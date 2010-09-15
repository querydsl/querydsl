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
 * DateTimeExpression represents Date / Time expressions
 * The date representation is compatible with the Gregorian calendar.
 *
 * @param <D>
 *
 * @author tiwe
 * @see <a href="http://en.wikipedia.org/wiki/Gregorian_calendar">Gregorian calendar</a>
 */
@SuppressWarnings({"unchecked"})
public abstract class DateTimeExpression<D extends Comparable> extends TemporalExpression<D> {

    private static final DateTimeExpression<Date> CURRENT_DATE = currentDate(Date.class);

    private static final DateTimeExpression<Date> CURRENT_TIMESTAMP = currentTimestamp(Date.class);

    private static final long serialVersionUID = -6879277113694148047L;

    /**
     * Get an expression representing the current date as a EDateTime instance
     *
     * @return
     */
    public static DateTimeExpression<Date> currentDate() {
        return CURRENT_DATE;
    }

    /**
     * Get an expression representing the current date as a EDateTime instance
     *
     * @return
     */
    public static <T extends Comparable> DateTimeExpression<T> currentDate(Class<T> cl) {
        return DateTimeOperation.<T>create(cl, Ops.DateTimeOps.CURRENT_DATE);
    }

    /**
     * Get an expression representing the current time instant as a EDateTime instance
     *
     * @return
     */
    public static DateTimeExpression<Date> currentTimestamp() {
        return CURRENT_TIMESTAMP;
    }

    /**
     * Get an expression representing the current time instant as a EDateTime instance
     *
     * @return
     */
    public static <T extends Comparable> DateTimeExpression<T> currentTimestamp(Class<T> cl) {
        return DateTimeOperation.create(cl, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }
    

    @Nullable
    private volatile NumberExpression<Integer> dayOfMonth, dayOfWeek, dayOfYear;

    @Nullable
    private volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    
    @Nullable
    private volatile DateTimeExpression<D> min, max;

    @Nullable
    private volatile NumberExpression<Integer> week, month, year, yearMonth;

    public DateTimeExpression(Class<? extends D> type) {
        super(type);
    }

    @Override
    public DateTimeExpression<D> as(Path<D> alias) {
        return DateTimeOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }

    @Override
    public DateTimeExpression as(String alias) {
        return DateTimeOperation.create(getType(), (Operator)Ops.ALIAS, this, new PathImpl<D>(getType(), alias));
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
     * Get a hours expression (range 0-23)
     *
     * @return
     */
    public NumberExpression<Integer> hour(){
        if (hours == null){
            hours = NumberOperation.create(Integer.class, Ops.DateTimeOps.HOUR, this);
        }
        return hours;
    }

    /**
     * Get the maximum value of this expression (aggregation)
     *
     * @return max(this)
     */
    public DateTimeExpression<D> max(){
        if (max == null){
            max = DateTimeOperation.create((Class<D>)getType(), Ops.AggOps.MAX_AGG, this);
        }
        return max;
    }

    /**
     * Get a milliseconds expression (range 0-999)
     * <p>Is always 0 in HQL and JDOQL modules</p>
     *
     * @return
     */
    public NumberExpression<Integer> milliSecond(){
        if (milliseconds == null){
            milliseconds = NumberOperation.create(Integer.class, Ops.DateTimeOps.MILLISECOND, this);
        }
        return milliseconds;
    }
    
    /**
     * Get the minimum value of this expression (aggregation)
     *
     * @return min(this)
     */
    public DateTimeExpression<D> min(){
        if (min == null){
            min = DateTimeOperation.create((Class<D>)getType(), Ops.AggOps.MIN_AGG, this);
        }
        return min;
    }

    /**
     * Get a minutes expression (range 0-59)
     *
     * @return
     */
    public NumberExpression<Integer> minute(){
        if (minutes == null){
            minutes = NumberOperation.create(Integer.class, Ops.DateTimeOps.MINUTE, this);
        }
        return minutes;
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
     * Get a seconds expression (range 0-59)
     *
     * @return
     */
    public NumberExpression<Integer> second(){
        if (seconds == null){
            seconds = NumberOperation.create(Integer.class, Ops.DateTimeOps.SECOND, this);
        }
        return seconds;
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
