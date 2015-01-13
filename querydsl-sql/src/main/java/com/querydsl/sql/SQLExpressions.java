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
package com.querydsl.sql;

import java.util.EnumMap;
import java.util.Map;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.DateOperation;
import com.querydsl.core.types.expr.DateTimeExpression;
import com.querydsl.core.types.expr.DateTimeOperation;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.NumberOperation;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.expr.SimpleOperation;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.expr.StringOperation;
import com.querydsl.core.types.expr.Wildcard;

/**
 * Common SQL expressions
 *
 * @author tiwe
 *
 */
@SuppressWarnings("rawtypes")
public final class SQLExpressions {

    private static final Map<DatePart, Operator> DATE_ADD_OPS
            = new EnumMap<DatePart, Operator>(DatePart.class);

    private static final Map<DatePart, Operator> DATE_DIFF_OPS
            = new EnumMap<DatePart, Operator>(DatePart.class);

    private static final Map<DatePart, Operator> DATE_TRUNC_OPS
            = new EnumMap<DatePart, Operator>(DatePart.class);

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

    private static final WindowOver<Double> cumeDist = new WindowOver<Double>(Double.class, SQLOps.CUMEDIST);

    private static final WindowOver<Long> rank = new WindowOver<Long>(Long.class, SQLOps.RANK);

    private static final WindowOver<Long> denseRank = new WindowOver<Long>(Long.class, SQLOps.DENSERANK);

    private static final WindowOver<Double> percentRank = new WindowOver<Double>(Double.class, SQLOps.PERCENTRANK);

    private static final WindowOver<Long> rowNumber = new WindowOver<Long>(Long.class, SQLOps.ROWNUMBER);

    private static Expression[] convertToExpressions(Object... args) {
        Expression<?>[] exprs = new Expression<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Expression) {
                exprs[i] = (Expression)args[i];
            } else {
                exprs[i] = new ConstantImpl(args[i]);
            }
        }
        return exprs;
    }

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
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), ConstantImpl.create(start), end);
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
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, ConstantImpl.create(end));
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
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), ConstantImpl.create(start), end);
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
        return NumberOperation.create(Integer.class, DATE_DIFF_OPS.get(unit), start, ConstantImpl.create(end));
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
     * @return
     */
    public static WindowOver<Long> count() {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_ALL_AGG);
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
    public static WindowOver<Long> countDistinct(Expression<?> expr) {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, expr);
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
     * @param expr
     * @param delimiter
     * @return
     */
    public static WithinGroup<Object> listagg(Expression<?> expr, String delimiter) {
        return new WithinGroup<Object>(Object.class, SQLOps.LISTAGG, expr, ConstantImpl.create(delimiter));
    }

    /**
     * @param measureExpr
     * @param n
     * @return
     */
    public static <T> WindowOver<T> nthValue(Expression<T> measureExpr, Number n) {
        return nthValue(measureExpr, new ConstantImpl<Number>(n));
    }

    /**
     * @param measureExpr
     * @param n
     * @return
     */
    public static <T> WindowOver<T> nthValue(Expression<T> measureExpr, Expression<? extends Number> n) {
        return new WindowOver<T>((Class<T>)measureExpr.getType(), SQLOps.NTHVALUE, measureExpr, n);
    }

    /**
     * divides an ordered data set into a number of buckets indicated by expr and assigns the
     * appropriate bucket number to each row
     *
     * @param num
     * @return
     */
    public static <T extends Number & Comparable> WindowOver<T> ntile(T num) {
        return new WindowOver<T>((Class<T>)num.getClass(), SQLOps.NTILE, new ConstantImpl<T>(num));
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
     * @param args
     * @return
     */
    public static WithinGroup<Long> rank(Object... args) {
        return rank(convertToExpressions(args));
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Long> rank(Expression<?>... args) {
        return new WithinGroup<Long>(Long.class, SQLOps.RANK2, args);
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
     * @param args
     * @return
     */
    public static WithinGroup<Long> denseRank(Object... args) {
        return denseRank(convertToExpressions(args));
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Long> denseRank(Expression<?>... args) {
        return new WithinGroup<Long>(Long.class, SQLOps.DENSERANK2, args);
    }

    /**
     * @return
     */
    public static WindowOver<Double> percentRank() {
        return percentRank;
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Double> percentRank(Object... args) {
        return percentRank(convertToExpressions(args));
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Double> percentRank(Expression<?>... args) {
        return new WithinGroup<Double>(Double.class, SQLOps.PERCENTRANK2, args);
    }

    /**
     * @param arg
     * @return
     */
    public static <T extends Number> WithinGroup<T> percentileCont(T arg) {
        if (arg.doubleValue() < 0.0 || arg.doubleValue() > 1.0) {
            throw new IllegalArgumentException("The percentile value should be a number between 0 and 1");
        }
        return percentileCont(new ConstantImpl<T>(arg));
    }

    /**
     * @param arg
     * @return
     */
    public static <T extends Number> WithinGroup<T> percentileCont(Expression<T> arg) {
        return new WithinGroup<T>((Class)arg.getType(), SQLOps.PERCENTILECONT, arg);
    }

    /**
     * @param arg
     * @return
     */
    public static <T extends Number> WithinGroup<T> percentileDisc(T arg) {
        if (arg.doubleValue() < 0.0 || arg.doubleValue() > 1.0) {
            throw new IllegalArgumentException("The percentile value should be a number between 0 and 1");
        }
        return percentileDisc(new ConstantImpl<T>(arg));
    }

    /**
     * @param arg
     * @return
     */
    public static <T extends Number> WithinGroup<T> percentileDisc(Expression<T> arg) {
        return new WithinGroup<T>((Class)arg.getType(), SQLOps.PERCENTILEDISC, arg);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrSlope(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SLOPE, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrIntercept(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_INTERCEPT, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrCount(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_COUNT, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrR2(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_R2, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrAvgx(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_AVGX, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrAvgy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_AVGY, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrSxx(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SXX, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrSyy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SYY, arg1, arg2);
    }

    /**
     * @param arg1
     * @param arg2
     * @return
     */
    public static WindowOver<Double> regrSxy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SXY, arg1, arg2);
    }

    /**
     * @return
     */
    public static WindowOver<Double> cumeDist() {
        return cumeDist;
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Double> cumeDist(Object... args) {
        return cumeDist(convertToExpressions(args));
    }

    /**
     * @param args
     * @return
     */
    public static WithinGroup<Double> cumeDist(Expression<?>... args) {
        return new WithinGroup<Double>(Double.class, SQLOps.CUMEDIST2, args);
    }

    /**
     * @param expr1
     * @param expr2
     * @return
     */
    public static WindowOver<Double> corr(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.CORR, expr1, expr2);
    }

    /**
     * @param expr1
     * @param expr2
     * @return
     */
    public static WindowOver<Double> covarPop(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.COVARPOP, expr1, expr2);
    }

    /**
     * @param expr1
     * @param expr2
     * @return
     */
    public static WindowOver<Double> covarSamp(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.COVARSAMP, expr1, expr2);
    }

    /**
     * computes the ratio of a value to the sum of a set of values. If expr evaluates to null,
     * then the ratio-to-report value also evaluates to null.
     *
     * @return
     */
    public static <T> WindowOver<T> ratioToReport(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.RATIOTOREPORT, expr);
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
     * returns the sample standard deviation of expr, a set of numbers.
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> stddev(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.STDDEV, expr);
    }

    /**
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> stddevDistinct(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.STDDEV_DISTINCT, expr);
    }

    /**
     * returns the population standard deviation and returns the square root of the population variance.
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> stddevPop(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.STDDEVPOP, expr);
    }

    /**
     * returns the cumulative sample standard deviation and returns the square root of the sample variance.
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> stddevSamp(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.STDDEVSAMP, expr);
    }

    /**
     * returns the variance of expr
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> variance(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.VARIANCE, expr);
    }

    /**
     * returns the population variance of a set of numbers after discarding the nulls in this set.
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> varPop(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.VARPOP, expr);
    }

    /**
     * returns the sample variance of a set of numbers after discarding the nulls in this set.
     *
     * @param expr
     * @return
     */
    public static <T extends Number> WindowOver<T> varSamp(Expression<T> expr) {
        return new WindowOver<T>((Class<T>)expr.getType(), SQLOps.VARSAMP, expr);
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
