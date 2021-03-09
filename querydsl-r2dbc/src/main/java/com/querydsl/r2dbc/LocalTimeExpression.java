/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.TemporalExpression;
import org.jetbrains.annotations.Nullable;

import java.time.LocalTime;

/**
 * {@code LocalTimeExpression} represents LocalTime expressions
 *
 * @param <T> expression type
 * @author mc_fish
 */
public abstract class LocalTimeExpression<T extends Comparable> extends TemporalExpression<T> {

    private static final long serialVersionUID = 7360552308332457990L;

    private static class Constants {
        private static final LocalTimeExpression<LocalTime> CURRENT_TIME = currentLocalTime(LocalTime.class);
    }

    @Nullable
    private transient volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    public LocalTimeExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public LocalTimeExpression<T> as(Path<T> alias) {
        return R2DBCExpressions.localTimeOperation(getType(), Ops.ALIAS, mixin, alias);
    }

    @Override
    public LocalTimeExpression<T> as(String alias) {
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
    public static LocalTimeExpression<LocalTime> currentLocalTime() {
        return Constants.CURRENT_TIME;
    }

    /**
     * Create an expression representing the current time as a TimeExpression instance
     *
     * @return current time
     */
    public static <T extends Comparable> LocalTimeExpression<T> currentLocalTime(Class<T> cl) {
        return R2DBCExpressions.localTimeOperation(cl, Ops.DateTimeOps.CURRENT_TIME);
    }

}
