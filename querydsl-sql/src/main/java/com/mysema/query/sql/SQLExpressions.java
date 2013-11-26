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
package com.mysema.query.sql;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateOperation;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringOperation;
import com.mysema.query.types.expr.Wildcard;

/**
 * Common SQL expressions
 *
 * @author tiwe
 *
 */
@SuppressWarnings("rawtypes")
public final class SQLExpressions {

    private static final Map<DatePart, Operator> DATE_ADD_OPS = new HashMap<DatePart, Operator>();

    private static final Map<DatePart, Operator> DATE_DIFF_OPS = new HashMap<DatePart, Operator>();

    private static final Map<DatePart, Operator> DATE_TRUNC_OPS = new HashMap<DatePart, Operator>();

    static {
        DATE_ADD_OPS.put(DatePart.year, Ops.DateTimeOps.ADD_YEARS);
        DATE_ADD_OPS.put(DatePart.month, Ops.DateTimeOps.ADD_MONTHS);
        DATE_ADD_OPS.put(DatePart.week, Ops.DateTimeOps.ADD_WEEKS);
        DATE_ADD_OPS.put(DatePart.day, Ops.DateTimeOps.ADD_DAYS);
        DATE_ADD_OPS.put(DatePart.hour, Ops.DateTimeOps.ADD_HOURS);
        DATE_ADD_OPS.put(DatePart.minute, Ops.DateTimeOps.ADD_MINUTES);
        DATE_ADD_OPS.put(DatePart.second, Ops.DateTimeOps.ADD_SECONDS);
        DATE_ADD_OPS.put(DatePart.millisecond, null); // TODO

        DATE_DIFF_OPS.put(DatePart.year, Ops.DateTimeOps.DIFF_YEARS);
        DATE_DIFF_OPS.put(DatePart.month, Ops.DateTimeOps.DIFF_MONTHS);
        DATE_DIFF_OPS.put(DatePart.week, Ops.DateTimeOps.DIFF_WEEKS);
        DATE_DIFF_OPS.put(DatePart.day, Ops.DateTimeOps.DIFF_DAYS);
        DATE_DIFF_OPS.put(DatePart.hour, Ops.DateTimeOps.DIFF_HOURS);
        DATE_DIFF_OPS.put(DatePart.minute, Ops.DateTimeOps.DIFF_MINUTES);
        DATE_DIFF_OPS.put(DatePart.second, Ops.DateTimeOps.DIFF_SECONDS);
        DATE_DIFF_OPS.put(DatePart.millisecond, null); // TODO

        DATE_TRUNC_OPS.put(DatePart.year, Ops.DateTimeOps.TRUNC_YEAR);
        DATE_TRUNC_OPS.put(DatePart.month, Ops.DateTimeOps.TRUNC_MONTH);
        DATE_TRUNC_OPS.put(DatePart.week, Ops.DateTimeOps.TRUNC_WEEK);
        DATE_TRUNC_OPS.put(DatePart.day, Ops.DateTimeOps.TRUNC_DAY);
        DATE_TRUNC_OPS.put(DatePart.hour, Ops.DateTimeOps.TRUNC_HOUR);
        DATE_TRUNC_OPS.put(DatePart.minute, Ops.DateTimeOps.TRUNC_MINUTE);
        DATE_TRUNC_OPS.put(DatePart.second, Ops.DateTimeOps.TRUNC_SECOND);

    }

    private static final WindowOver<Long> rank = new WindowOver<Long>(Long.class, SQLOps.RANK);

    private static final WindowOver<Long> denseRank = new WindowOver<Long>(Long.class, SQLOps.DENSERANK);

    private static final WindowOver<Long> rowNumber = new WindowOver<Long>(Long.class, SQLOps.ROWNUMBER);

    /**
     * Wildcard expression
     */
    public static final Expression<Object[]> all = Wildcard.all;

    /**
     * Wilcard count expression
     */
    public static final Expression<Long> countAll = Wildcard.count;

    /**
     * Get an aggregate any expression for the given boolean expression
     */
    public static BooleanExpression any(BooleanExpression expr) {
        return BooleanOperation.create(Ops.AggOps.BOOLEAN_ANY, expr);
    }

    /**
     * Get an aggregate all expression for the given boolean expression
     */
    public static BooleanExpression all(BooleanExpression expr) {
        return BooleanOperation.create(Ops.AggOps.BOOLEAN_ALL, expr);
    }

    /**
     * Get a nextval(sequence) expression
     *
     * @param sequence
     * @return
     */
    public static SimpleExpression<Long> nextval(String sequence) {
        return nextval(Long.class, sequence);
    }

    /**
     * Get a nextval(sequence) expression of the given type
     *
     * @param type
     * @param sequence
     * @return
     */
    public static <T extends Number> SimpleExpression<T> nextval(Class<T> type, String sequence) {
        return SimpleOperation.create(type, SQLOps.NEXTVAL, ConstantImpl.create(sequence));
    }

    /**
     * Convert timestamp to date
     *
     * @param dateTime
     * @return
     */
    public static <D extends Comparable> DateExpression<D> date(DateTimeExpression<D> dateTime) {
        return DateOperation.create((Class)dateTime.getType(), Ops.DateTimeOps.DATE, dateTime);
    }

    /**
     * Convert timestamp to date
     *
     * @param type
     * @param dateTime
     * @return
     */
    public static <D extends Comparable> DateExpression<D> date(Class<D> type, DateTimeExpression<?> dateTime) {
        return DateOperation.create(type, Ops.DateTimeOps.DATE, dateTime);
    }

    /**
     * Get a dateadd(unit, date, amount) expression
     *
     * @param unit
     * @param date
     * @param amount
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> dateadd(DatePart unit, DateTimeExpression<D> date, int amount) {
        return DateTimeOperation.create((Class)date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }

    /**
     * Get a dateadd(unit, date, amount) expression
     *
     * @param unit
     * @param date
     * @param amount
     * @return
     */
    public static <D extends Comparable> DateExpression<D> dateadd(DatePart unit, DateExpression<D> date, int amount) {
        return DateOperation.create((Class)date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateExpression<D> start, DateExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            D start, DateExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), new ConstantImpl<D>(start), end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateExpression<D> start, D end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, new ConstantImpl<D>(end));
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateTimeExpression<D> start, DateTimeExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            D start, DateTimeExpression<D> end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), new ConstantImpl<D>(start), end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit
     * @param start
     * @param end
     * @return
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateTimeExpression<D> start, D end) {
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, new ConstantImpl<D>(end));
    }

    /**
     * Truncate the given date expression
     *
     * @param unit
     * @param expr
     */
    public static <D extends Comparable> DateExpression<D> datetrunc(DatePart unit, DateExpression<D> expr) {
        return DateOperation.create(expr.getType(), DATE_TRUNC_OPS.get(unit), expr);
    }

    /**
     * Truncate the given datetime expression
     *
     * @param unit
     * @param expr
     */
    public static <D extends Comparable> DateTimeExpression<D> datetrunc(DatePart unit, DateTimeExpression<D> expr) {
        return DateTimeOperation.create(expr.getType(), DATE_TRUNC_OPS.get(unit), expr);
    }

    /**
     * Add the given amount of years to the date
     *
     * @param date
     * @param years
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addYears(DateTimeExpression<D> date, int years) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }

    /**
     * Add the given amount of months to the date
     *
     * @param date
     * @param months
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMonths(DateTimeExpression<D> date, int months) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }

    /**
     * Add the given amount of weeks to the date
     *
     * @param date
     * @param weeks
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addWeeks(DateTimeExpression<D> date, int weeks) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }

    /**
     * Add the given amount of days to the date
     *
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addDays(DateTimeExpression<D> date, int days) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }

    /**
     * Add the given amount of hours to the date
     *
     * @param date
     * @param hours
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addHours(DateTimeExpression<D> date, int hours) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_HOURS, date, ConstantImpl.create(hours));
    }

    /**
     * Add the given amount of minutes to the date
     *
     * @param date
     * @param minutes
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addMinutes(DateTimeExpression<D> date, int minutes) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MINUTES, date, ConstantImpl.create(minutes));
    }

    /**
     * Add the given amount of seconds to the date
     *
     * @param date
     * @param seconds
     * @return
     */
    public static <D extends Comparable> DateTimeExpression<D> addSeconds(DateTimeExpression<D> date, int seconds) {
        return DateTimeOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_SECONDS, date, ConstantImpl.create(seconds));
    }

    /**
     * Add the given amount of years to the date
     *
     * @param date
     * @param years
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addYears(DateExpression<D> date, int years) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }

    /**
     * Add the given amount of months to the date
     *
     * @param date
     * @param months
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addMonths(DateExpression<D> date, int months) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }

    /**
     * Add the given amount of weeks to the date
     *
     * @param date
     * @param weeks
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addWeeks(DateExpression<D> date, int weeks) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }

    /**
     * Add the given amount of days to the date
     *
     * @param date
     * @param days
     * @return
     */
    public static <D extends Comparable> DateExpression<D> addDays(DateExpression<D> date, int days) {
        return DateOperation.create((Class)date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }

    /**
     * Start a window function expression
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> sum(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), Ops.AggOps.SUM_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr
     * @return
     */
    public static WindowOver<Long> count(Expression<?> expr) {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> avg(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), Ops.AggOps.AVG_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr
     * @return
     */
    public static <T extends Comparable> WindowOver<T> min(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), Ops.AggOps.MIN_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr
     * @return
     */
    public static <T extends Comparable> WindowOver<T> max(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), Ops.AggOps.MAX_AGG, expr);
    }

    /**
     * expr evaluated at the row that is one row after the current row within the partition;
     *
     * @param expr
     * @return
     */
    public static <T> WindowOver<T> lead(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.LEAD, expr);
    }

    /**
     * expr evaluated at the row that is one row before the current row within the partition
     *
     * @param expr
     * @return
     */
    public static <T> WindowOver<T> lag(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.LAG, expr);
    }

    /**
     * rank of the current row with gaps; same as row_number of its first peer
     *
     * @return
     */
    public static WindowOver<Long> rank() {
        return rank;
    }

    /**
     * rank of the current row without gaps; this function counts peer groups
     *
     * @return
     */
    public static WindowOver<Long> denseRank() {
        return denseRank;
    }

    /**
     * number of the current row within its partition, counting from 1
     *
     * @return
     */
    public static WindowOver<Long> rowNumber() {
        return rowNumber;
    }

    /**
     * returns value evaluated at the row that is the first row of the window frame
     *
     * @param expr
     * @return
     */
    public static <T> WindowOver<T> firstValue(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.FIRSTVALUE, expr);
    }

    /**
     * returns value evaluated at the row that is the last row of the window frame
     *
     * @param expr
     * @return
     */
    public static <T> WindowOver<T> lastValue(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.LASTVALUE, expr);
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static StringExpression left(Expression<String> lhs, int rhs) {
        return left(lhs, ConstantImpl.create(rhs));
    }

    /**
     * Get the rhs rightmost characters of lhs
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static StringExpression right(Expression<String> lhs, int rhs) {
        return right(lhs, ConstantImpl.create(rhs));
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static StringExpression left(Expression<String> lhs, Expression<Integer> rhs) {
        return StringOperation.create(Ops.StringOps.LEFT, lhs, rhs);
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs
     * @param rhs
     * @return
     */
    public static StringExpression right(Expression<String> lhs, Expression<Integer> rhs) {
        return StringOperation.create(Ops.StringOps.RIGHT, lhs, rhs);
    }

    private SQLExpressions() {}

}
