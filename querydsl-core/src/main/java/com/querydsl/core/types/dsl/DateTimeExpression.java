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

import java.util.Date;

import javax.annotation.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code DateTimeExpression} represents Date / Time expressions
 * The date representation is compatible with the Gregorian calendar.
 *
 * @param <T> expression type
 *
 * @author tiwe
 * @see <a href="http://en.wikipedia.org/wiki/Gregorian_calendar">Gregorian calendar</a>
 */
public abstract class DateTimeExpression<T extends Comparable> extends TemporalExpression<T> {

    private static final DateTimeExpression<Date> CURRENT_DATE = currentDate(Date.class);

    private static final DateTimeExpression<Date> CURRENT_TIMESTAMP = currentTimestamp(Date.class);

    private static final long serialVersionUID = -6879277113694148047L;

    /**
     * Create an expression representing the current date as a DateTimeExpression instance
     *
     * @return current date
     */
    public static DateTimeExpression<Date> currentDate() {
        return CURRENT_DATE;
    }

    /**
     * Create an expression representing the current date as a DateTimeExpression instance
     *
     * @return current date
     */
    public static <T extends Comparable> DateTimeExpression<T> currentDate(Class<T> cl) {
        return Expressions.dateTimeOperation(cl, Ops.DateTimeOps.CURRENT_DATE);
    }

    /**
     * Create an expression representing the current time instant as a DateTimeExpression instance
     *
     * @return current timestamp
     */
    public static DateTimeExpression<Date> currentTimestamp() {
        return CURRENT_TIMESTAMP;
    }

    /**
     * Create an expression representing the current time instant as a DateTimeExpression instance
     *
     * @return current timestamp
     */
    public static <T extends Comparable> DateTimeExpression<T> currentTimestamp(Class<T> cl) {
        return Expressions.dateTimeOperation(cl, Ops.DateTimeOps.CURRENT_TIMESTAMP);
    }

    @Nullable
    private volatile NumberExpression<Integer> dayOfMonth, dayOfWeek, dayOfYear;

    @Nullable
    private volatile NumberExpression<Integer> hours, minutes, seconds, milliseconds;

    @Nullable
    private volatile DateTimeExpression<T> min, max;

    @Nullable
    private volatile NumberExpression<Integer> week, month, year, yearMonth, yearWeek;

    public DateTimeExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public DateTimeExpression<T> as(Path<T> alias) {
        return Expressions.dateTimeOperation(getType(), Ops.ALIAS, mixin, alias);
    }

    @Override
    public DateTimeExpression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }

    /**
     * Create a day of month expression (range 1-31)
     *
     * @return day of month
     */
    public NumberExpression<Integer> dayOfMonth() {
        if (dayOfMonth == null) {
            dayOfMonth = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.DAY_OF_MONTH, mixin);
        }
        return dayOfMonth;
    }

    /**
     * Create a day of week expression (range 1-7 / SUN-SAT)
     * <p>NOT supported in JDOQL and not in Derby</p>
     *
     * @return day of week
     */
    public NumberExpression<Integer> dayOfWeek() {
        if (dayOfWeek == null) {
            dayOfWeek = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.DAY_OF_WEEK, mixin);
        }
        return dayOfWeek;
    }

    /**
     * Create a day of year expression (range 1-356)
     * <p>NOT supported in JDOQL and not in Derby</p>
     *
     * @return day of year
     */
    public NumberExpression<Integer> dayOfYear() {
        if (dayOfYear == null) {
            dayOfYear = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.DAY_OF_YEAR, mixin);
        }
        return dayOfYear;
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
     * Get the maximum value of this expression (aggregation)
     *
     * @return max(this)
     */
    public DateTimeExpression<T> max() {
        if (max == null) {
            max = Expressions.dateTimeOperation(getType(), Ops.AggOps.MAX_AGG, mixin);
        }
        return max;
    }

    /**
     * Create a milliseconds expression (range 0-999)
     * <p>Is always 0 in HQL and JDOQL modules</p>
     *
     * @return milli seconds
     */
    public NumberExpression<Integer> milliSecond() {
        if (milliseconds == null) {
            milliseconds = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.MILLISECOND, mixin);
        }
        return milliseconds;
    }

    /**
     * Get the minimum value of this expression (aggregation)
     *
     * @return min(this)
     */
    public DateTimeExpression<T> min() {
        if (min == null) {
            min = Expressions.dateTimeOperation(getType(), Ops.AggOps.MIN_AGG, mixin);
        }
        return min;
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
     * Create a month expression (range 1-12 / JAN-DEC)
     *
     * @return month
     */
    public NumberExpression<Integer> month() {
        if (month == null) {
            month = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.MONTH, mixin);
        }
        return month;
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
     * Create a week expression
     *
     * @return week
     */
    public NumberExpression<Integer> week() {
        if (week == null) {
            week = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.WEEK,  mixin);
        }
        return week;
    }

    /**
     * Create a year expression
     *
     * @return year
     */
    public NumberExpression<Integer> year() {
        if (year == null) {
            year = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.YEAR, mixin);
        }
        return year;
    }

    /**
     * Create a year / month expression
     *
     * @return year month
     */
    public NumberExpression<Integer> yearMonth() {
        if (yearMonth == null) {
            yearMonth = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.YEAR_MONTH, mixin);
        }
        return yearMonth;
    }

    /**
     * Create a ISO yearweek expression
     *
     * @return year week
     */
    public NumberExpression<Integer> yearWeek() {
        if (yearWeek == null) {
            yearWeek = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.YEAR_WEEK, mixin);
        }
        return yearWeek;
    }

}
