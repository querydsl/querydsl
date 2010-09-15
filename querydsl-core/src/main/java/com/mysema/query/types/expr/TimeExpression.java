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
import com.mysema.query.types.PathMixin;

/**
 * TimeExpression represents Time expressions
 *
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public abstract class TimeExpression<D extends Comparable> extends TemporalExpression<D> {

    private static final long serialVersionUID = 7360552308332457990L;

    private static final TimeExpression<Date> CURRENT_TIME = currentTime(Date.class);

    @Nullable
    private volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    public TimeExpression(Class<? extends D> type) {
        super(type);
    }

    @Override
    public TimeExpression<D> as(Path<D> alias) {
        return TimeOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }
    
    @Override
    public TimeExpression<D> as(String alias) {
        return TimeOperation.create(getType(), (Operator)Ops.ALIAS, this, new PathMixin<D>(getType(), alias));
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
     * Get an expression representing the current time as a ETime instance
     *
     * @return
     */
    public static TimeExpression<Date> currentTime() {
        return CURRENT_TIME;
    }

    /**
     * Get an expression representing the current time as a ETime instance
     *
     * @return
     */
    public static <T extends Comparable> TimeExpression<T> currentTime(Class<T> cl) {
        return TimeOperation.create(cl, Ops.DateTimeOps.CURRENT_TIME);
    }

}
