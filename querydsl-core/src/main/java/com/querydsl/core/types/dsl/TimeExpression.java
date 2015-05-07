/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.dsl;

import java.sql.Time;

import javax.annotation.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code TimeExpression} represents Time expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public abstract class TimeExpression<T extends Comparable> extends TemporalExpression<T> {

    private static final long serialVersionUID = 7360552308332457990L;

    private static final TimeExpression<Time> CURRENT_TIME = currentTime(Time.class);

    @Nullable
    private transient volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    public TimeExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public TimeExpression<T> as(Path<T> alias) {
        return Expressions.timeOperation(getType(),Ops.ALIAS, mixin, alias);
    }

    @Override
    public TimeExpression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }
    
    /**
     * Create a hours expression (range 0-23)
     *
     * @return hour
     */
    public NumberExpression<Integer> hour() {
        if (hours == null) {
            hours = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.HOUR, mixin);
        }
        return hours;
    }

    /**
     * Create a minutes expression (range 0-59)
     *
     * @return minute
     */
    public NumberExpression<Integer> minute() {
        if (minutes == null) {
            minutes = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.MINUTE, mixin);
        }
        return minutes;
    }

    /**
     * Create a seconds expression (range 0-59)
     *
     * @return second
     */
    public NumberExpression<Integer> second() {
        if (seconds == null) {
            seconds = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.SECOND, mixin);
        }
        return seconds;
    }

    /**
     * Create a milliseconds expression (range 0-999)
     * <p>Is always 0 in JPA and JDO modules</p>
     *
     * @return milli second
     */
    public NumberExpression<Integer> milliSecond() {
        if (milliseconds == null) { 
            milliseconds = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.MILLISECOND, mixin);
        }
        return milliseconds;
    }

    /**
     * Create an expression representing the current time as a TimeExpression instance
     *
     * @return current time
     */
    public static TimeExpression<Time> currentTime() {
        return CURRENT_TIME;
    }

    /**
     * Create an expression representing the current time as a TimeExpression instance
     *
     * @return current time
     */
    public static <T extends Comparable> TimeExpression<T> currentTime(Class<T> cl) {
        return Expressions.timeOperation(cl, Ops.DateTimeOps.CURRENT_TIME);
    }

}
