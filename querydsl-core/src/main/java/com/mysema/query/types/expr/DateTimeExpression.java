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
import com.mysema.query.types.path.SimplePath;

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
public abstract class DateTimeExpression<D extends Comparable> extends DateExpression<D> {

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
    private volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    @Nullable
    private volatile DateTimeExpression<D> min, max;

    public DateTimeExpression(Class<? extends D> type) {
        super(type);
    }

    @Override
    public DateTimeExpression<D> as(Path<D> alias) {
        return DateTimeOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }
    
    @Override
    public DateTimeExpression as(String alias) {
        return DateTimeOperation.create(getType(), (Operator)Ops.ALIAS, this, new SimplePath(getType(), alias));
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
    @Override
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
    @Override
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

}
