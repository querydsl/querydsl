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
package com.querydsl.sql;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;

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
                exprs[i] = ConstantImpl.create(args[i]);
            }
        }
        return exprs;
    }

    /**
     * Wildcard expression
     */
    public static final Expression<Object[]> all = Wildcard.all;

    /**
     * Wildcard count expression
     */
    public static final Expression<Long> countAll = Wildcard.count;

    /**
     * Create a new detached SQLQuery instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
     */
    public static <T> SQLQuery<T> select(Expression<T> expr) {
        return new SQLQuery<Void>().select(expr);
    }

    /**
     * Create a new detached SQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public static SQLQuery<Tuple> select(Expression<?>... exprs) {
        return new SQLQuery<Void>().select(exprs);
    }

    /**
     * Create a new detached SQLQuery instance with the given projection
     *
     * @param expr distinct projection
     * @param <T>
     * @return select(distinct expr)
     */
    public static <T> SQLQuery<T> selectDistinct(Expression<T> expr) {
        return new SQLQuery<Void>().select(expr).distinct();
    }

    /**
     * Create a new detached SQLQuery instance with the given projection
     *
     * @param exprs distinct projection
     * @return select(distinct exprs)
     */
    public static SQLQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return new SQLQuery<Void>().select(exprs).distinct();
    }

    /**
     * Create a new detached SQLQuery instance with zero as the projection
     *
     * @return select(0)
     */
    public static SQLQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    /**
     * Create a new detached SQLQuery isntance with one as the projection
     *
     * @return select(1)
     */
    public static SQLQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    /**
     * Create a new detached SQLQuery instance with the given projection
     *
     * @param expr query source and projection
     * @param <T>
     * @return select(expr).from(expr)
     */
    public static <T> SQLQuery<T> selectFrom(RelationalPath<T> expr) {
        return select(expr).from(expr);
    }

    /**
     * Create a new UNION clause
     *
     * @param sq subqueries
     * @param <T>
     * @return union
     */
    public static <T> Union<T> union(SubQueryExpression<T>... sq) {
        return new SQLQuery<Void>().union(sq);
    }

    /**
     * Create a new UNION clause
     *
     * @param sq subqueries
     * @param <T>
     * @return union
     */
    public static <T> Union<T> union(List<SubQueryExpression<T>> sq) {
        return new SQLQuery<Void>().union(sq);
    }

    /**
     * Create a new UNION ALL clause
     *
     * @param sq subqueries
     * @param <T>
     * @return union
     */
    public static <T> Union<T> unionAll(SubQueryExpression<T>... sq) {
        return new SQLQuery<Void>().unionAll(sq);
    }

    /**
     * Create a new UNION ALL clause
     *
     * @param sq subqueries
     * @param <T>
     * @return union
     */
    public static <T> Union<T> unionAll(List<SubQueryExpression<T>> sq) {
        return new SQLQuery<Void>().unionAll(sq);
    }

    /**
     * Get an aggregate any expression for the given boolean expression
     */
    public static BooleanExpression any(BooleanExpression expr) {
        return Expressions.booleanOperation(Ops.AggOps.BOOLEAN_ANY, expr);
    }

    /**
     * Get an aggregate all expression for the given boolean expression
     */
    public static BooleanExpression all(BooleanExpression expr) {
        return Expressions.booleanOperation(Ops.AggOps.BOOLEAN_ALL, expr);
    }

    /**
     * Create a new RelationalFunctionCall for the given function and arguments
     *
     * @param type type
     * @param function function name
     * @param args arguments
     * @param <T>
     * @return relational function call
     */
    public static <T> RelationalFunctionCall<T> relationalFunctionCall(Class<? extends T> type, String function, Object... args) {
        return new RelationalFunctionCall<T>(type, function, args);
    }

    /**
     * Create a nextval(sequence) expression
     *
     * <p>Returns the next value from the give sequence</p>
     *
     * @param sequence sequence name
     * @return nextval(sequence)
     */
    public static SimpleExpression<Long> nextval(String sequence) {
        return nextval(Long.class, sequence);
    }

    /**
     * Create a nextval(sequence) expression of the given type
     *
     * <p>Returns the next value from the given sequence</p>
     *
     * @param type type of call
     * @param sequence sequence name
     * @return nextval(sequence)
     */
    public static <T extends Number> SimpleExpression<T> nextval(Class<T> type, String sequence) {
        return Expressions.operation(type, SQLOps.NEXTVAL, ConstantImpl.create(sequence));
    }

    /**
     * Convert timestamp to date
     *
     * @param dateTime timestamp
     * @return date
     */
    public static <D extends Comparable> DateExpression<D> date(DateTimeExpression<D> dateTime) {
        return Expressions.dateOperation(dateTime.getType(), Ops.DateTimeOps.DATE, dateTime);
    }

    /**
     * Convert timestamp to date
     *
     * @param type type
     * @param dateTime timestamp
     * @return date
     */
    public static <D extends Comparable> DateExpression<D> date(Class<D> type, DateTimeExpression<?> dateTime) {
        return Expressions.dateOperation(type, Ops.DateTimeOps.DATE, dateTime);
    }

    /**
     * Create a dateadd(unit, date, amount) expression
     *
     * @param unit date part
     * @param date date
     * @param amount amount
     * @return converted date
     */
    public static <D extends Comparable> DateTimeExpression<D> dateadd(DatePart unit, DateTimeExpression<D> date, int amount) {
        return Expressions.dateTimeOperation(date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }

    /**
     * Create a dateadd(unit, date, amount) expression
     *
     * @param unit date part
     * @param date date
     * @param amount amount
     * @return converted date
     */
    public static <D extends Comparable> DateExpression<D> dateadd(DatePart unit, DateExpression<D> date, int amount) {
        return Expressions.dateOperation(date.getType(), DATE_ADD_OPS.get(unit), date, ConstantImpl.create(amount));
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateExpression<D> start, DateExpression<D> end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            D start, DateExpression<D> end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), ConstantImpl.create(start), end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateExpression<D> start, D end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), start, ConstantImpl.create(end));
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateTimeExpression<D> start, DateTimeExpression<D> end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), start, end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            D start, DateTimeExpression<D> end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), ConstantImpl.create(start), end);
    }

    /**
     * Get a datediff(unit, start, end) expression
     *
     * @param unit date part
     * @param start start
     * @param end end
     * @return difference in units
     */
    public static <D extends Comparable> NumberExpression<Integer> datediff(DatePart unit,
            DateTimeExpression<D> start, D end) {
        return Expressions.numberOperation(Integer.class, DATE_DIFF_OPS.get(unit), start, ConstantImpl.create(end));
    }

    /**
     * Truncate the given date expression
     *
     * @param unit date part to truncate to
     * @param expr truncated date
     */
    public static <D extends Comparable> DateExpression<D> datetrunc(DatePart unit, DateExpression<D> expr) {
        return Expressions.dateOperation(expr.getType(), DATE_TRUNC_OPS.get(unit), expr);
    }

    /**
     * Truncate the given datetime expression
     *
     * @param unit datepart to truncate to
     * @param expr truncated datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> datetrunc(DatePart unit, DateTimeExpression<D> expr) {
        return Expressions.dateTimeOperation(expr.getType(), DATE_TRUNC_OPS.get(unit), expr);
    }

    /**
     * Add the given amount of years to the date
     *
     * @param date datetime
     * @param years years to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addYears(DateTimeExpression<D> date, int years) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }

    /**
     * Add the given amount of months to the date
     *
     * @param date datetime
     * @param months months to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addMonths(DateTimeExpression<D> date, int months) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }

    /**
     * Add the given amount of weeks to the date
     *
     * @param date datetime
     * @param weeks weeks to add
     * @return converted date
     */
    public static <D extends Comparable> DateTimeExpression<D> addWeeks(DateTimeExpression<D> date, int weeks) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }

    /**
     * Add the given amount of days to the date
     *
     * @param date datetime
     * @param days days to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addDays(DateTimeExpression<D> date, int days) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }

    /**
     * Add the given amount of hours to the date
     *
     * @param date datetime
     * @param hours hours to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addHours(DateTimeExpression<D> date, int hours) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_HOURS, date, ConstantImpl.create(hours));
    }

    /**
     * Add the given amount of minutes to the date
     *
     * @param date datetime
     * @param minutes minues to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addMinutes(DateTimeExpression<D> date, int minutes) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_MINUTES, date, ConstantImpl.create(minutes));
    }

    /**
     * Add the given amount of seconds to the date
     *
     * @param date datetime
     * @param seconds seconds to add
     * @return converted datetime
     */
    public static <D extends Comparable> DateTimeExpression<D> addSeconds(DateTimeExpression<D> date, int seconds) {
        return Expressions.dateTimeOperation(date.getType(), Ops.DateTimeOps.ADD_SECONDS, date, ConstantImpl.create(seconds));
    }

    /**
     * Add the given amount of years to the date
     *
     * @param date date
     * @param years years to add
     * @return converted date
     */
    public static <D extends Comparable> DateExpression<D> addYears(DateExpression<D> date, int years) {
        return Expressions.dateOperation(date.getType(), Ops.DateTimeOps.ADD_YEARS, date, ConstantImpl.create(years));
    }

    /**
     * Add the given amount of months to the date
     *
     * @param date date
     * @param months months to add
     * @return converted date
     */
    public static <D extends Comparable> DateExpression<D> addMonths(DateExpression<D> date, int months) {
        return Expressions.dateOperation(date.getType(), Ops.DateTimeOps.ADD_MONTHS, date, ConstantImpl.create(months));
    }

    /**
     * Add the given amount of weeks to the date
     *
     * @param date date
     * @param weeks weeks to add
     * @return converted date
     */
    public static <D extends Comparable> DateExpression<D> addWeeks(DateExpression<D> date, int weeks) {
        return Expressions.dateOperation(date.getType(), Ops.DateTimeOps.ADD_WEEKS, date, ConstantImpl.create(weeks));
    }

    /**
     * Add the given amount of days to the date
     *
     * @param date date
     * @param days days to add
     * @return converted date
     */
    public static <D extends Comparable> DateExpression<D> addDays(DateExpression<D> date, int days) {
        return Expressions.dateOperation(date.getType(), Ops.DateTimeOps.ADD_DAYS, date, ConstantImpl.create(days));
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return sum(expr)
     */
    public static <T extends Number> WindowOver<T> sum(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), Ops.AggOps.SUM_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @return count()
     */
    public static WindowOver<Long> count() {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_ALL_AGG);
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return count(expr)
     */
    public static WindowOver<Long> count(Expression<?> expr) {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return count(distinct expr)
     */
    public static WindowOver<Long> countDistinct(Expression<?> expr) {
        return new WindowOver<Long>(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return avg(expr)
     */
    public static <T extends Number> WindowOver<T> avg(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), Ops.AggOps.AVG_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return min(expr)
     */
    public static <T extends Comparable> WindowOver<T> min(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), Ops.AggOps.MIN_AGG, expr);
    }

    /**
     * Start a window function expression
     *
     * @param expr expression
     * @return max(expr)
     */
    public static <T extends Comparable> WindowOver<T> max(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), Ops.AggOps.MAX_AGG, expr);
    }

    /**
     * expr evaluated at the row that is one row after the current row within the partition;
     *
     * @param expr expression
     * @return lead(expr)
     */
    public static <T> WindowOver<T> lead(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.LEAD, expr);
    }

    /**
     * expr evaluated at the row that is one row before the current row within the partition
     *
     * @param expr expression
     * @return lag(expr)
     */
    public static <T> WindowOver<T> lag(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.LAG, expr);
    }

    /**
     * LISTAGG orders data within each group specified in the ORDER BY clause and then concatenates
     * the values of the measure column.
     *
     * @param expr measure column
     * @param delimiter delimiter
     * @return listagg(expr, delimiter)
     */
    public static WithinGroup<Object> listagg(Expression<?> expr, String delimiter) {
        return new WithinGroup<Object>(Object.class, SQLOps.LISTAGG, expr, ConstantImpl.create(delimiter));
    }

    /**
     * NTH_VALUE returns the expr value of the nth row in the window defined by the analytic clause.
     * The returned value has the data type of the expr.
     *
     * @param expr measure expression
     * @param n one based row index
     * @return nth_value(expr, n)
     */
    public static <T> WindowOver<T> nthValue(Expression<T> expr, Number n) {
        return nthValue(expr, new ConstantImpl<Number>(n));
    }

    /**
     * NTH_VALUE returns the expr value of the nth row in the window defined by the analytic clause.
     * The returned value has the data type of the expr
     *
     * @param expr measure expression
     * @param n one based row index
     * @return nth_value(expr, n)
     */
    public static <T> WindowOver<T> nthValue(Expression<T> expr, Expression<? extends Number> n) {
        return new WindowOver<T>(expr.getType(), SQLOps.NTHVALUE, expr, n);
    }

    /**
     * divides an ordered data set into a number of buckets indicated by expr and assigns the
     * appropriate bucket number to each row
     *
     * @param num bucket size
     * @return ntile(num)
     */
    public static <T extends Number & Comparable> WindowOver<T> ntile(T num) {
        return new WindowOver<T>((Class<T>)num.getClass(), SQLOps.NTILE, new ConstantImpl<T>(num));
    }

    /**
     * rank of the current row with gaps; same as row_number of its first peer
     *
     * @return rank()
     */
    public static WindowOver<Long> rank() {
        return rank;
    }

    /**
     * As an aggregate function, RANK calculates the rank of a hypothetical row identified by the
     * arguments of the function with respect to a given sort specification. The arguments of the
     * function must all evaluate to constant expressions within each aggregate group, because they
     * identify a single row within each group. The constant argument expressions and the expressions
     * in the ORDER BY clause of the aggregate match by position. Therefore, the number of arguments
     * must be the same and their types must be compatible.
     *
     * @param args arguments
     * @return rank(args)
     */
    public static WithinGroup<Long> rank(Object... args) {
        return rank(convertToExpressions(args));
    }

    /**
     * As an aggregate function, RANK calculates the rank of a hypothetical row identified by the
     * arguments of the function with respect to a given sort specification. The arguments of the
     * function must all evaluate to constant expressions within each aggregate group, because they
     * identify a single row within each group. The constant argument expressions and the expressions
     * in the ORDER BY clause of the aggregate match by position. Therefore, the number of arguments
     * must be the same and their types must be compatible.
     *
     * @param args arguments
     * @return rank(args)
     */
    public static WithinGroup<Long> rank(Expression<?>... args) {
        return new WithinGroup<Long>(Long.class, SQLOps.RANK2, args);
    }

    /**
     * rank of the current row without gaps; this function counts peer groups
     *
     * @return dense_rank()
     */
    public static WindowOver<Long> denseRank() {
        return denseRank;
    }

    /**
     * As an aggregate function, DENSE_RANK calculates the dense rank of a hypothetical row identified
     * by the arguments of the function with respect to a given sort specification. The arguments of
     * the function must all evaluate to constant expressions within each aggregate group, because they
     * identify a single row within each group. The constant argument expressions and the expressions
     * in the order_by_clause of the aggregate match by position. Therefore, the number of arguments
     * must be the same and types must be compatible.
     *
     * @param args arguments
     * @return dense_rank(args)
     */
    public static WithinGroup<Long> denseRank(Object... args) {
        return denseRank(convertToExpressions(args));
    }

    /**
     * As an aggregate function, DENSE_RANK calculates the dense rank of a hypothetical row identified
     * by the arguments of the function with respect to a given sort specification. The arguments of
     * the function must all evaluate to constant expressions within each aggregate group, because they
     * identify a single row within each group. The constant argument expressions and the expressions
     * in the order_by_clause of the aggregate match by position. Therefore, the number of arguments
     * must be the same and types must be compatible.
     *
     * @param args arguments
     * @return dense_rank(args)
     */
    public static WithinGroup<Long> denseRank(Expression<?>... args) {
        return new WithinGroup<Long>(Long.class, SQLOps.DENSERANK2, args);
    }

    /**
     * As an analytic function, for a row r, PERCENT_RANK calculates the rank of r minus 1, divided by
     * 1 less than the number of rows being evaluated (the entire query result set or a partition).
     *
     * @return percent_rank()
     */
    public static WindowOver<Double> percentRank() {
        return percentRank;
    }

    /**
     * As an aggregate function, PERCENT_RANK calculates, for a hypothetical row r identified by the
     * arguments of the function and a corresponding sort specification, the rank of row r minus 1
     * divided by the number of rows in the aggregate group. This calculation is made as if the
     * hypothetical row r were inserted into the group of rows over which Oracle Database is to
     * aggregate. The arguments of the function identify a single hypothetical row within each aggregate
     * group. Therefore, they must all evaluate to constant expressions within each aggregate group.
     * The constant argument expressions and the expressions in the ORDER BY clause of the aggregate
     * match by position. Therefore the number of arguments must be the same and their types must be
     * compatible.
     *
     * @param args arguments
     * @return percent_rank(args)
     */
    public static WithinGroup<Double> percentRank(Object... args) {
        return percentRank(convertToExpressions(args));
    }

    /**
     * As an aggregate function, PERCENT_RANK calculates, for a hypothetical row r identified by the
     * arguments of the function and a corresponding sort specification, the rank of row r minus 1
     * divided by the number of rows in the aggregate group. This calculation is made as if the
     * hypothetical row r were inserted into the group of rows over which Oracle Database is to aggregate.
     * The arguments of the function identify a single hypothetical row within each aggregate group.
     * Therefore, they must all evaluate to constant expressions within each aggregate group. The
     * constant argument expressions and the expressions in the ORDER BY clause of the aggregate match
     * by position. Therefore the number of arguments must be the same and their types must be compatible.
     *
     * @param args arguments
     * @return percent_rank(args)
     */
    public static WithinGroup<Double> percentRank(Expression<?>... args) {
        return new WithinGroup<Double>(Double.class, SQLOps.PERCENTRANK2, args);
    }

    /**
     * Calculates a percentile based on a continuous distribution of the column value
     *
     * @param arg argument
     * @return percentile_cont(arg)
     */
    public static <T extends Number> WithinGroup<T> percentileCont(T arg) {
        if (arg.doubleValue() < 0.0 || arg.doubleValue() > 1.0) {
            throw new IllegalArgumentException("The percentile value should be a number between 0 and 1");
        }
        return percentileCont(new ConstantImpl<T>(arg));
    }

    /**
     * Calculates a percentile based on a continuous distribution of the column value
     *
     * @param arg argument
     * @return percentile_cont(arg)
     */
    public static <T extends Number> WithinGroup<T> percentileCont(Expression<T> arg) {
        return new WithinGroup<T>(arg.getType(), SQLOps.PERCENTILECONT, arg);
    }

    /**
     * PERCENTILE_DISC is an inverse distribution function that assumes a discrete distribution model.
     * It takes a percentile value and a sort specification and returns an element from the set.
     * Nulls are ignored in the calculation.
     *
     * <p>This function takes as an argument any numeric datatype or any nonnumeric datatype that can be
     * implicitly converted to a numeric datatype. The function returns the same datatype as the numeric
     * datatype of the argument.</p>
     *
     * @param arg argument
     * @return percentile_disc(arg)
     */
    public static <T extends Number> WithinGroup<T> percentileDisc(T arg) {
        if (arg.doubleValue() < 0.0 || arg.doubleValue() > 1.0) {
            throw new IllegalArgumentException("The percentile value should be a number between 0 and 1");
        }
        return percentileDisc(new ConstantImpl<T>(arg));
    }

    /**
     * PERCENTILE_DISC is an inverse distribution function that assumes a discrete distribution model.
     * It takes a percentile value and a sort specification and returns an element from the set.
     * Nulls are ignored in the calculation.
     *
     * <p>This function takes as an argument any numeric datatype or any nonnumeric datatype that can be
     * implicitly converted to a numeric datatype. The function returns the same datatype as the numeric
     * datatype of the argument.</p>
     *
     * @param arg argument
     * @return percentile_disc(arg)
     */
    public static <T extends Number> WithinGroup<T> percentileDisc(Expression<T> arg) {
        return new WithinGroup<T>(arg.getType(), SQLOps.PERCENTILEDISC, arg);
    }

    /**
     * REGR_SLOPE returns the slope of the line
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_slope(arg1, arg2)
     */
    public static WindowOver<Double> regrSlope(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SLOPE, arg1, arg2);
    }

    /**
     * REGR_INTERCEPT returns the y-intercept of the regression line.
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_intercept(arg1, arg2)
     */
    public static WindowOver<Double> regrIntercept(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_INTERCEPT, arg1, arg2);
    }

    /**
     * REGR_COUNT returns an integer that is the number of non-null number pairs used to fit the regression line.
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_count(arg1, arg2)
     */
    public static WindowOver<Double> regrCount(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_COUNT, arg1, arg2);
    }

    /**
     * REGR_R2 returns the coefficient of determination (also called R-squared or goodness of fit) for the regression.
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_r2(arg1, arg2)
     */
    public static WindowOver<Double> regrR2(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_R2, arg1, arg2);
    }

    /**
     * REGR_AVGX evaluates the average of the independent variable (arg2) of the regression line.
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_avgx(arg1, arg2)
     */
    public static WindowOver<Double> regrAvgx(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_AVGX, arg1, arg2);
    }

    /**
     * REGR_AVGY evaluates the average of the dependent variable (arg1) of the regression line.
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_avgy(arg1, arg2)
     */
    public static WindowOver<Double> regrAvgy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_AVGY, arg1, arg2);
    }

    /**
     * REGR_SXX makes the following computation after the elimination of null (arg1, arg2) pairs:
     *
     * <p>{@code REGR_COUNT(arg1, arg2) * VAR_POP(arg2)}</p>
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_sxx(arg1, arg2)
     */
    public static WindowOver<Double> regrSxx(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SXX, arg1, arg2);
    }

    /**
     * REGR_SYY makes the following computation after the elimination of null (arg1, arg2) pairs:
     *
     * <p>{@code REGR_COUNT(arg1, arg2) * VAR_POP(arg1)}</p>
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_syy(arg1, arg2)
     */
    public static WindowOver<Double> regrSyy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SYY, arg1, arg2);
    }

    /**
     * REGR_SXY makes the following computation after the elimination of null (arg1, arg2) pairs:
     *
     * <p>REGR_COUNT(arg1, arg2) * COVAR_POP(arg1, arg2)</p>
     *
     * @param arg1 first arg
     * @param arg2 second arg
     * @return regr_sxy(arg1, arg2)
     */
    public static WindowOver<Double> regrSxy(Expression<? extends Number> arg1, Expression<? extends Number> arg2) {
        return new WindowOver<Double>(Double.class, SQLOps.REGR_SXY, arg1, arg2);
    }

    /**
     * CUME_DIST calculates the cumulative distribution of a value in a group of values.
     *
     * @return cume_dist()
     */
    public static WindowOver<Double> cumeDist() {
        return cumeDist;
    }

    /**
     * As an aggregate function, CUME_DIST calculates, for a hypothetical row r identified by the
     * arguments of the function and a corresponding sort specification, the relative position of row
     * r among the rows in the aggregation group. Oracle makes this calculation as if the hypothetical
     * row r were inserted into the group of rows to be aggregated over. The arguments of the function
     * identify a single hypothetical row within each aggregate group. Therefore, they must all
     * evaluate to constant expressions within each aggregate group. The constant argument expressions
     * and the expressions in the ORDER BY clause of the aggregate match by position. Therefore,
     * the number of arguments must be the same and their types must be compatible.
     *
     * @param args arguments
     * @return cume_dist(args)
     */
    public static WithinGroup<Double> cumeDist(Object... args) {
        return cumeDist(convertToExpressions(args));
    }

    /**
     * As an aggregate function, CUME_DIST calculates, for a hypothetical row r identified by the
     * arguments of the function and a corresponding sort specification, the relative position of row
     * r among the rows in the aggregation group. Oracle makes this calculation as if the hypothetical
     * row r were inserted into the group of rows to be aggregated over. The arguments of the function
     * identify a single hypothetical row within each aggregate group. Therefore, they must all
     * evaluate to constant expressions within each aggregate group. The constant argument expressions
     * and the expressions in the ORDER BY clause of the aggregate match by position. Therefore,
     * the number of arguments must be the same and their types must be compatible.
     *
     * @param args arguments
     * @return cume_dist(args)
     */
    public static WithinGroup<Double> cumeDist(Expression<?>... args) {
        return new WithinGroup<Double>(Double.class, SQLOps.CUMEDIST2, args);
    }

    /**
     * CORR returns the coefficient of correlation of a set of number pairs.
     *
     * @param expr1 first arg
     * @param expr2 second arg
     * @return corr(expr1, expr2)
     */
    public static WindowOver<Double> corr(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.CORR, expr1, expr2);
    }

    /**
     * CORR returns the coefficient of correlation of a set of number pairs.
     *
     * @param expr1 first arg
     * @param expr2 second arg
     * @return corr(expr1, expr2)
     */
    public static WindowOver<Double> covarPop(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.COVARPOP, expr1, expr2);
    }

    /**
     * CORR returns the coefficient of correlation of a set of number pairs.
     *
     * @param expr1 first arg
     * @param expr2 second arg
     * @return corr(expr1, expr2)
     */
    public static WindowOver<Double> covarSamp(Expression<? extends Number> expr1, Expression<? extends Number> expr2) {
        return new WindowOver<Double>(Double.class, SQLOps.COVARSAMP, expr1, expr2);
    }

    /**
     * computes the ratio of a value to the sum of a set of values. If expr evaluates to null,
     * then the ratio-to-report value also evaluates to null.
     *
     * @return ratio_to_report(expr)
     */
    public static <T> WindowOver<T> ratioToReport(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.RATIOTOREPORT, expr);
    }

    /**
     * number of the current row within its partition, counting from 1
     *
     * @return row_number()
     */
    public static WindowOver<Long> rowNumber() {
        return rowNumber;
    }

    /**
     * returns the sample standard deviation of expr, a set of numbers.
     *
     * @param expr argument
     * @return stddev(expr)
     */
    public static <T extends Number> WindowOver<T> stddev(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.STDDEV, expr);
    }

    /**
     * returns the sample standard deviation of expr, a set of numbers.
     *
     * @param expr argument
     * @return stddev(distinct expr)
     */
    public static <T extends Number> WindowOver<T> stddevDistinct(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.STDDEV_DISTINCT, expr);
    }

    /**
     * returns the population standard deviation and returns the square root of the population variance.
     *
     * @param expr argument
     * @return stddev_pop(expr)
     */
    public static <T extends Number> WindowOver<T> stddevPop(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.STDDEVPOP, expr);
    }

    /**
     * returns the cumulative sample standard deviation and returns the square root of the sample variance.
     *
     * @param expr argument
     * @return stddev_samp(expr)
     */
    public static <T extends Number> WindowOver<T> stddevSamp(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.STDDEVSAMP, expr);
    }

    /**
     * returns the variance of expr
     *
     * @param expr argument
     * @return variance(expr)
     */
    public static <T extends Number> WindowOver<T> variance(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.VARIANCE, expr);
    }

    /**
     * returns the population variance of a set of numbers after discarding the nulls in this set.
     *
     * @param expr argument
     * @return var_pop(expr)
     */
    public static <T extends Number> WindowOver<T> varPop(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.VARPOP, expr);
    }

    /**
     * returns the sample variance of a set of numbers after discarding the nulls in this set.
     *
     * @param expr argument
     * @return var_samp(expr)
     */
    public static <T extends Number> WindowOver<T> varSamp(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.VARSAMP, expr);
    }

    /**
     * returns value evaluated at the row that is the first row of the window frame
     *
     * @param expr argument
     * @return first_value(expr)
     */
    public static <T> WindowOver<T> firstValue(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.FIRSTVALUE, expr);
    }

    /**
     * returns value evaluated at the row that is the last row of the window frame
     *
     * @param expr argument
     * @return last_value(expr)
     */
    public static <T> WindowOver<T> lastValue(Expression<T> expr) {
        return new WindowOver<T>(expr.getType(), SQLOps.LASTVALUE, expr);
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs string
     * @param rhs character amount
     * @return rhs leftmost characters
     */
    public static StringExpression left(Expression<String> lhs, int rhs) {
        return left(lhs, ConstantImpl.create(rhs));
    }

    /**
     * Get the rhs rightmost characters of lhs
     *
     * @param lhs string
     * @param rhs character amount
     * @return rhs rightmost characters
     */
    public static StringExpression right(Expression<String> lhs, int rhs) {
        return right(lhs, ConstantImpl.create(rhs));
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs string
     * @param rhs character amount
     * @return rhs leftmost characters
     */
    public static StringExpression left(Expression<String> lhs, Expression<Integer> rhs) {
        return Expressions.stringOperation(Ops.StringOps.LEFT, lhs, rhs);
    }

    /**
     * Get the rhs leftmost characters of lhs
     *
     * @param lhs string
     * @param rhs character amount
     * @return rhs rightmost characters
     */
    public static StringExpression right(Expression<String> lhs, Expression<Integer> rhs) {
        return Expressions.stringOperation(Ops.StringOps.RIGHT, lhs, rhs);
    }

    private SQLExpressions() {}

}
