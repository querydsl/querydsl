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

import javax.annotation.Nullable;
import java.time.LocalDate;

/**
 * {@code LocalDateExpression} represents LocalDate expressions
 * The date representation is compatible with the Gregorian calendar.
 *
 * @param <T> expression type
 * @author mc_fish
 */
public abstract class LocalDateExpression<T extends Comparable> extends TemporalExpression<T> {

    private static class Constants {
        private static final LocalDateExpression<LocalDate> CURRENT_DATE = currentLocalDate(LocalDate.class);
    }

    private static final long serialVersionUID = 6054664454254721302L;

    /**
     * Create an expression representing the current date as a {@code DateExpression} instance
     *
     * @return current date
     */
    public static LocalDateExpression<LocalDate> currentLocalDate() {
        return Constants.CURRENT_DATE;
    }

    /**
     * Create an expression representing the current date as a {@code DateExpression} instance
     *
     * @param cl type of expression
     * @return current date
     */
    public static <T extends Comparable> LocalDateExpression<T> currentLocalDate(Class<T> cl) {
        return R2DBCExpressions.localDateOperation(cl, Ops.DateTimeOps.CURRENT_DATE);
    }

    @Nullable
    private transient volatile NumberExpression<Integer> dayOfMonth, dayOfWeek, dayOfYear;

    @Nullable
    private transient volatile LocalDateExpression<T> min, max;

    @Nullable
    private transient volatile NumberExpression<Integer> week, month, year, yearMonth, yearWeek;

    public LocalDateExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public LocalDateExpression<T> as(Path<T> alias) {
        return R2DBCExpressions.localDateOperation(getType(), Ops.ALIAS, mixin, alias);
    }

    @Override
    public LocalDateExpression<T> as(String alias) {
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
     * Get the maximum value of this expression (aggregation)
     *
     * @return max(this)
     */
    public LocalDateExpression<T> max() {
        if (max == null) {
            max = R2DBCExpressions.localDateOperation(getType(), Ops.AggOps.MAX_AGG, mixin);
        }
        return max;
    }

    /**
     * Get the minimum value of this expression (aggregation)
     *
     * @return min(this)
     */
    public LocalDateExpression<T> min() {
        if (min == null) {
            min = R2DBCExpressions.localDateOperation(getType(), Ops.AggOps.MIN_AGG, mixin);
        }
        return min;
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
     * Create a week expression
     *
     * @return week
     */
    public NumberExpression<Integer> week() {
        if (week == null) {
            week = Expressions.numberOperation(Integer.class, Ops.DateTimeOps.WEEK, mixin);
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
