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

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.querydsl.core.*;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.testutil.Serialization;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.sql.domain.*;

public class SelectBase extends AbstractBaseTest {

    private static final Expression<?>[] NO_EXPRESSIONS = new Expression[0];

    private final QueryExecution standardTest = new QueryExecution(Module.SQL, Connections.getTarget()) {
        @Override
        protected Fetchable<?> createQuery() {
            return testQuery().from(employee, employee2);
        }
        @Override
        protected Fetchable<?> createQuery(Predicate filter) {
            return testQuery().from(employee, employee2).where(filter).select(employee.firstname);
        }
    };

    private <T> T firstResult(Expression<T> expr) {
        return query().select(expr).fetchFirst();
    }

    private Tuple firstResult(Expression<?>... exprs) {
        return query().select(exprs).fetchFirst();
    }

    @Test
    public void Aggregate_List() {
        int min = 30000, avg = 65000, max = 160000;
        // fetch
        assertEquals(min, query().from(employee).select(employee.salary.min()).fetch().get(0).intValue());
        assertEquals(avg, query().from(employee).select(employee.salary.avg()).fetch().get(0).intValue());
        assertEquals(max, query().from(employee).select(employee.salary.max()).fetch().get(0).intValue());
    }

    @Test
    public void Aggregate_UniqueResult() {
        int min = 30000, avg = 65000, max = 160000;
        // fetchOne
        assertEquals(min, query().from(employee).select(employee.salary.min()).fetchOne().intValue());
        assertEquals(avg, query().from(employee).select(employee.salary.avg()).fetchOne().intValue());
        assertEquals(max, query().from(employee).select(employee.salary.max()).fetchOne().intValue());
    }

    @Test
    @ExcludeIn({MYSQL, ORACLE})
    @SkipForQuoted
    public void Alias_Quotes() {
        expectedQuery = "select e.FIRSTNAME as \"First Name\" from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name")).fetch();
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void Alias_Quotes_MySQL() {
        expectedQuery = "select e.FIRSTNAME as `First Name` from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name")).fetch();
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void Alias_Quotes_Oracle() {
        expectedQuery = "select e.FIRSTNAME \"First Name\" from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name"));
    }

    @Test
    public void All() {
        for (Expression<?> expr : survey.all()) {
            Path<?> path = (Path<?>)expr;
            assertEquals(survey, path.getMetadata().getParent());
        }
    }

    private void arithmeticTests(NumberExpression<Integer> one, NumberExpression<Integer> two,
                                 NumberExpression<Integer> three, NumberExpression<Integer> four) {
        assertEquals(1, firstResult(one).intValue());
        assertEquals(2, firstResult(two).intValue());
        assertEquals(4, firstResult(four).intValue());

        assertEquals(3, firstResult(one.subtract(two).add(four)).intValue());
        assertEquals(-5, firstResult(one.subtract(two.add(four))).intValue());
        assertEquals(-1, firstResult(one.add(two).subtract(four)).intValue());
        assertEquals(-1, firstResult(one.add(two.subtract(four))).intValue());

        assertEquals(12, firstResult(one.add(two).multiply(four)).intValue());
        assertEquals(2, firstResult(four.multiply(one).divide(two)).intValue());
        assertEquals(2, firstResult(four.multiply(one.divide(two))).intValue());
        assertEquals(6, firstResult(four.divide(two).multiply(three)).intValue());
        assertEquals(1, firstResult(four.divide(two.multiply(two))).intValue());
    }

    @Test
    public void Arithmetic() {
        NumberExpression<Integer> one = Expressions.numberTemplate(Integer.class, "(1.0)");
        NumberExpression<Integer> two = Expressions.numberTemplate(Integer.class, "(2.0)");
        NumberExpression<Integer> three = Expressions.numberTemplate(Integer.class, "(3.0)");
        NumberExpression<Integer> four = Expressions.numberTemplate(Integer.class, "(4.0)");
        arithmeticTests(one, two, three, four);
    }

    @Test
    public void Arithmetic2() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        NumberExpression<Integer> four = Expressions.FOUR;
        arithmeticTests(one, two, three, four);
    }

    @Test
    public void Arithmetic_Mod() {
        NumberExpression<Integer> one = Expressions.numberTemplate(Integer.class, "(1)");
        NumberExpression<Integer> two = Expressions.numberTemplate(Integer.class, "(2)");
        NumberExpression<Integer> three = Expressions.numberTemplate(Integer.class, "(3)");
        NumberExpression<Integer> four = Expressions.numberTemplate(Integer.class, "(4)");

        assertEquals(4, firstResult(four.mod(three).add(three)).intValue());
        assertEquals(1, firstResult(four.mod(two.add(one))).intValue());
        assertEquals(0, firstResult(four.mod(two.multiply(one))).intValue());
        assertEquals(2, firstResult(four.add(one).mod(three)).intValue());
    }

    @Test
    @IncludeIn(POSTGRESQL) // TODO generalize array literal projections
    public void Array() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "'{1,2,3}'::int[]");
        Integer[] result = firstResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0].intValue());
        assertEquals(2, result[1].intValue());
        assertEquals(3, result[2].intValue());
    }

    @Test
    @IncludeIn(POSTGRESQL) // TODO generalize array literal projections
    public void Array2() {
        Expression<int[]> expr = Expressions.template(int[].class, "'{1,2,3}'::int[]");
        int[] result = firstResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
        assertEquals(3, result[2]);
    }

    @Test
    @ExcludeIn({DERBY, HSQLDB})
    public void Array_Null() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "null");
        assertNull(firstResult(expr));
    }

    @Test
    public void Array_Projection() {
        List<String[]> results = query().from(employee).select(
                new ArrayConstructorExpression<String>(String[].class, employee.firstname)).fetch();
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void Beans() {
        List<Beans> rows = query().from(employee, employee2).select(new QBeans(employee, employee2)).fetch();
        assertFalse(rows.isEmpty());
        for (Beans row : rows) {
            assertEquals(Employee.class, row.get(employee).getClass());
            assertEquals(Employee.class, row.get(employee2).getClass());
        }
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_All() {
        assertTrue(query().from(employee).select(SQLExpressions.all(employee.firstname.isNotNull())).fetchOne());
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_Any() {
        assertTrue(query().from(employee).select(SQLExpressions.any(employee.firstname.isNotNull())).fetchOne());
    }

    @Test
    public void Case() {
        NumberExpression<Float> numExpression = employee.salary.floatValue().divide(employee2.salary.floatValue()).multiply(100.1);
        NumberExpression<Float> numExpression2 = employee.id.when(0).then(0.0F).otherwise(numExpression);
        assertEquals(ImmutableList.of(87, 90, 88, 87, 83, 80, 75),
                query().from(employee, employee2)
                        .where(employee.id.eq(employee2.id.add(1)))
                        .orderBy(employee.id.asc(), employee2.id.asc())
                        .select(numExpression2.floor().intValue()).fetch());
    }

    @Test
    public void Casts() throws SQLException {
        NumberExpression<?> num = employee.id;
        List<Expression<?>> exprs = Lists.newArrayList();

        add(exprs, num.byteValue(), MYSQL);
        add(exprs, num.doubleValue());
        add(exprs, num.floatValue());
        add(exprs, num.intValue());
        add(exprs, num.longValue(), MYSQL);
        add(exprs, num.shortValue(), MYSQL);
        add(exprs, num.stringValue(), DERBY);

        for (Expression<?> expr : exprs) {
            for (Object o : query().from(employee).select(expr).fetch()) {
                assertEquals(expr.getType(), o.getClass());
            }
        }
    }

    @Test
    public void Coalesce() {
        Coalesce<String> c = new Coalesce<String>(employee.firstname, employee.lastname).add("xxx");
        query().from(employee).where(c.getValue().eq("xxx")).select(employee.id).fetch();
    }

    @Test
    public void Compact_Join() {
        // verbose
        query().from(employee)
            .innerJoin(employee2)
            .on(employee.superiorId.eq(employee2.id))
            .select(employee.id, employee2.id).fetch();

        // compact
        query().from(employee)
            .innerJoin(employee.superiorIdKey, employee2)
            .select(employee.id, employee2.id).fetch();

    }

    @Test
    public void Complex_Boolean() {
        BooleanExpression first = employee.firstname.eq("Mike").and(employee.lastname.eq("Smith"));
        BooleanExpression second = employee.firstname.eq("Joe").and(employee.lastname.eq("Divis"));
        assertEquals(2, query().from(employee).where(first.or(second)).fetchCount());

        assertEquals(0, query().from(employee).where(
                employee.firstname.eq("Mike"),
                employee.lastname.eq("Smith").or(employee.firstname.eq("Joe")),
                employee.lastname.eq("Divis")
        ).fetchCount());
    }

    @Test
    public void Complex_SubQuery() {
        // alias for the salary
        NumberPath<BigDecimal> sal = Expressions.numberPath(BigDecimal.class, "sal");
        // alias for the subquery
        PathBuilder<BigDecimal> sq = new PathBuilder<BigDecimal>(BigDecimal.class, "sq");
        // query execution
        query().from(
                query().from(employee)
                        .select(employee.salary.add(employee.salary).add(employee.salary).as(sal)).as(sq)
        ).select(sq.get(sal).avg(), sq.get(sal).min(), sq.get(sal).max()).fetch();
    }

    @Test
    public void Constructor() throws Exception {
        for (IdName idName : query().from(survey).select(new QIdName(survey.id, survey.name)).fetch()) {
            System.out.println("id and name : " + idName.getId() + "," + idName.getName());
        }
    }

    @Test
    public void Constructor_Projection() {
        // constructor projection
        for (IdName idAndName : query().from(survey).select(new QIdName(survey.id, survey.name)).fetch()) {
            assertNotNull(idAndName);
            assertNotNull(idAndName.getId());
            assertNotNull(idAndName.getName());
        }
    }

    @Test
    public void Constructor_Projection2() {

        List<SimpleProjection> projections =query().from(employee).select(
                Projections.constructor(SimpleProjection.class,
                        employee.firstname, employee.lastname)).fetch();
        assertFalse(projections.isEmpty());
        for (SimpleProjection projection : projections) {
            assertNotNull(projection);
        }
    }

    private double cot(double x) {
        return Math.cos(x) / Math.sin(x);
    }

    private double coth(double x) {
        return Math.cosh(x) / Math.sinh(x);
    }

    @Test
    public void Count_With_PK() {
        query().from(employee).fetchCount();
    }

    @Test
    public void Count_Without_PK() {
        query().from(QEmployeeNoPK.employee).fetchCount();
    }

    @Test
    public void Count2() {
        query().from(employee).select(employee.count()).fetchFirst();
    }

    @Test
    @SkipForQuoted
    @ExcludeIn(ORACLE)
    public void Count_All() {
        expectedQuery = "select count(*) as rc from EMPLOYEE e";
        NumberPath<Long> rowCount = Expressions.numberPath(Long.class, "rc");
        query().from(employee).select(Wildcard.count.as(rowCount)).fetchOne();
    }

    @Test
    @SkipForQuoted
    @IncludeIn(ORACLE)
    public void Count_All_Oracle() {
        expectedQuery = "select count(*) rc from EMPLOYEE e";
        NumberPath<Long> rowCount = Expressions.numberPath(Long.class, "rc");
        query().from(employee).select(Wildcard.count.as(rowCount)).fetchOne();
    }

    @Test
    public void Count_Distinct_With_PK() {
        query().from(employee).distinct().fetchCount();
    }

    @Test
    public void Count_Distinct_Without_PK() {
        query().from(QEmployeeNoPK.employee).distinct().fetchCount();
    }

    @Test
    public void Count_Distinct2() {
        query().from(employee).select(employee.countDistinct()).fetchFirst();
    }

    @Test
    public void Custom_Projection() {
        List<Projection> tuples = query().from(employee).select(
                new QProjection(employee.firstname, employee.lastname)).fetch();
        assertFalse(tuples.isEmpty());
        for (Projection tuple : tuples) {
            assertNotNull(tuple.get(employee.firstname));
            assertNotNull(tuple.get(employee.lastname));
            assertNotNull(tuple.getExpr(employee.firstname));
            assertNotNull(tuple.getExpr(employee.lastname));
        }
    }

    @Test
    @ExcludeIn({CUBRID, DB2, DERBY, HSQLDB, POSTGRESQL, SQLITE, TERADATA})
    public void Dates() {
        long ts = ((long)Math.floor(System.currentTimeMillis() / 1000)) * 1000;
        long tsDate = new org.joda.time.LocalDate(ts).toDateMidnight().getMillis();
        long tsTime = new org.joda.time.LocalTime(ts).getMillisOfDay();

        List<Object> data = Lists.newArrayList();
        data.add(Constants.date);
        data.add(Constants.time);
        data.add(new java.util.Date(ts));
        data.add(new java.util.Date(tsDate));
        data.add(new java.util.Date(tsTime));
        data.add(new java.sql.Timestamp(ts));
        data.add(new java.sql.Timestamp(tsDate));
        data.add(new java.sql.Date(110, 0, 1));
        data.add(new java.sql.Date(tsDate));
        data.add(new java.sql.Time(0, 0, 0));
        data.add(new java.sql.Time(12, 30, 0));
        data.add(new java.sql.Time(23, 59, 59));
        //data.add(new java.sql.Time(tsTime));
        data.add(new DateTime(ts));
        data.add(new DateTime(tsDate));
        data.add(new DateTime(tsTime));
        data.add(new LocalDateTime(ts));
        data.add(new LocalDateTime(tsDate));
        data.add(new LocalDateTime(2014, 3, 30, 2, 0));
        data.add(new LocalDate(2010, 1, 1));
        data.add(new LocalDate(ts));
        data.add(new LocalDate(tsDate));
        data.add(new LocalTime(0, 0, 0));
        data.add(new LocalTime(12, 30, 0));
        data.add(new LocalTime(23, 59, 59));
        data.add(new LocalTime(ts));
        data.add(new LocalTime(tsTime));

        Map<Object, Object> failures = Maps.newIdentityHashMap();
        for (Object dt : data) {
            Object dt2 = firstResult(Expressions.constant(dt));
            if (!dt.equals(dt2)) {
                failures.put(dt, dt2);
            }
        }
        if (!failures.isEmpty()) {
            for (Map.Entry<Object, Object> entry : failures.entrySet()) {
                System.out.println(entry.getKey().getClass().getName()
                        + ": " + entry.getKey() + " != " + entry.getValue());
            }
            Assert.fail("Failed with " + failures);
        }
    }

    @Test
    @ExcludeIn({CUBRID, SQLITE, TERADATA})
    public void Dates_Literals() {
        if (configuration.getUseLiterals()) {
            Dates();
        }
    }

    @Test
    @ExcludeIn({SQLITE})
    public void Date_Add() {
        SQLQuery<?> query = query().from(employee);
        Date date1 = query.select(employee.datefield).fetchFirst();
        Date date2 = query.select(SQLExpressions.addYears(employee.datefield, 1)).fetchFirst();
        Date date3 = query.select(SQLExpressions.addMonths(employee.datefield, 1)).fetchFirst();
        Date date4 = query.select(SQLExpressions.addDays(employee.datefield, 1)).fetchFirst();

        assertTrue(date2.getTime() > date1.getTime());
        assertTrue(date3.getTime() > date1.getTime());
        assertTrue(date4.getTime() > date1.getTime());
    }

    @Test
    @ExcludeIn({SQLITE})
    public void Date_Add_Timestamp() {
        List<Expression<?>> exprs = Lists.newArrayList();
        DateTimeExpression<java.util.Date> dt = Expressions.currentTimestamp();

        add(exprs, SQLExpressions.addYears(dt, 1));
        add(exprs, SQLExpressions.addMonths(dt, 1));
        add(exprs, SQLExpressions.addDays(dt, 1));
        add(exprs, SQLExpressions.addHours(dt, 1), TERADATA);
        add(exprs, SQLExpressions.addMinutes(dt, 1), TERADATA);
        add(exprs, SQLExpressions.addSeconds(dt, 1), TERADATA);

        for (Expression<?> expr : exprs) {
            assertNotNull(firstResult(expr));
        }
    }

    @Test
    @ExcludeIn({DB2, SQLITE, TERADATA})
    public void Date_Diff() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        SQLQuery<?> query2 = query().from(employee, employee2)
                .orderBy(employee.id.asc(), employee2.id.desc());

        List<DatePart> dps = Lists.newArrayList();
        add(dps, DatePart.year);
        add(dps, DatePart.month);
        add(dps, DatePart.week);
        add(dps, DatePart.day);
        add(dps, DatePart.hour, HSQLDB);
        add(dps, DatePart.minute, HSQLDB);
        add(dps, DatePart.second, HSQLDB);

        LocalDate localDate = new LocalDate(1970, 1, 10);
        Date date = new Date(localDate.toDateMidnight().getMillis());

        for (DatePart dp : dps) {
            int diff1 = query.select(SQLExpressions.datediff(dp, date, employee.datefield)).fetchFirst();
            int diff2 = query.select(SQLExpressions.datediff(dp, employee.datefield, date)).fetchFirst();
            int diff3 = query2.select(SQLExpressions.datediff(dp, employee.datefield, employee2.datefield)).fetchFirst();
            assertEquals(diff1, -diff2);
        }
    }

    // TDO Date_Diff with timestamps

    @Test
    @ExcludeIn({DB2, HSQLDB, SQLITE, TERADATA})
    public void Date_Diff2() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());

        LocalDate localDate = new LocalDate(1970, 1, 10);
        Date date = new Date(localDate.toDateMidnight().getMillis());

        int years = query.select(SQLExpressions.datediff(DatePart.year, date, employee.datefield)).fetchFirst();
        int months = query.select(SQLExpressions.datediff(DatePart.month, date, employee.datefield)).fetchFirst();
        // weeks
        int days = query.select(SQLExpressions.datediff(DatePart.day, date, employee.datefield)).fetchFirst();
        int hours = query.select(SQLExpressions.datediff(DatePart.hour, date, employee.datefield)).fetchFirst();
        int minutes = query.select(SQLExpressions.datediff(DatePart.minute, date, employee.datefield)).fetchFirst();
        int seconds = query.select(SQLExpressions.datediff(DatePart.second, date, employee.datefield)).fetchFirst();

        assertEquals(949363200, seconds);
        assertEquals(15822720,  minutes);
        assertEquals(263712,    hours);
        assertEquals(10988,     days);
        assertEquals(361,       months);
        assertEquals(30,        years);
    }

    @Test
    @ExcludeIn({SQLITE}) // FIXME
    public void Date_Trunc() {
        DateTimeExpression<java.util.Date> expr = DateTimeExpression.currentTimestamp();

        List<DatePart> dps = Lists.newArrayList();
        add(dps, DatePart.year);
        add(dps, DatePart.month);
        add(dps, DatePart.week, DERBY, FIREBIRD, SQLSERVER);
        add(dps, DatePart.day);
        add(dps, DatePart.hour);
        add(dps, DatePart.minute);
        add(dps, DatePart.second);

        for (DatePart dp : dps) {
            firstResult(SQLExpressions.datetrunc(dp, expr));
        }
    }

    @Test
    @ExcludeIn({SQLITE, TERADATA}) // FIXME
    public void Date_Trunc2() {
        DateTimeExpression<DateTime> expr = DateTimeExpression.currentTimestamp(DateTime.class);

        Tuple tuple = firstResult(
                expr,
                SQLExpressions.datetrunc(DatePart.year, expr),
                SQLExpressions.datetrunc(DatePart.month, expr),
                SQLExpressions.datetrunc(DatePart.day, expr),
                SQLExpressions.datetrunc(DatePart.hour, expr),
                SQLExpressions.datetrunc(DatePart.minute, expr),
                SQLExpressions.datetrunc(DatePart.second, expr));
        DateTime date = tuple.get(expr);
        DateTime toYear = tuple.get(SQLExpressions.datetrunc(DatePart.year, expr));
        DateTime toMonth = tuple.get(SQLExpressions.datetrunc(DatePart.month, expr));
        DateTime toDay = tuple.get(SQLExpressions.datetrunc(DatePart.day, expr));
        DateTime toHour = tuple.get(SQLExpressions.datetrunc(DatePart.hour, expr));
        DateTime toMinute = tuple.get(SQLExpressions.datetrunc(DatePart.minute, expr));
        DateTime toSecond = tuple.get(SQLExpressions.datetrunc(DatePart.second, expr));

        assertEquals(date.getZone(), toYear.getZone());
        assertEquals(date.getZone(), toMonth.getZone());
        assertEquals(date.getZone(), toDay.getZone());
        assertEquals(date.getZone(), toHour.getZone());
        assertEquals(date.getZone(), toMinute.getZone());
        assertEquals(date.getZone(), toSecond.getZone());

        // year
        assertEquals(date.getYear(), toYear.getYear());
        assertEquals(date.getYear(), toMonth.getYear());
        assertEquals(date.getYear(), toDay.getYear());
        assertEquals(date.getYear(), toHour.getYear());
        assertEquals(date.getYear(), toMinute.getYear());
        assertEquals(date.getYear(), toSecond.getYear());

        // month
        assertEquals(1,                     toYear.getMonthOfYear());
        assertEquals(date.getMonthOfYear(), toMonth.getMonthOfYear());
        assertEquals(date.getMonthOfYear(), toDay.getMonthOfYear());
        assertEquals(date.getMonthOfYear(), toHour.getMonthOfYear());
        assertEquals(date.getMonthOfYear(), toMinute.getMonthOfYear());
        assertEquals(date.getMonthOfYear(), toSecond.getMonthOfYear());

        // day
        assertEquals(1, toYear.getDayOfMonth());
        assertEquals(1, toMonth.getDayOfMonth());
        assertEquals(date.getDayOfMonth(), toDay.getDayOfMonth());
        assertEquals(date.getDayOfMonth(), toHour.getDayOfMonth());
        assertEquals(date.getDayOfMonth(), toMinute.getDayOfMonth());
        assertEquals(date.getDayOfMonth(), toSecond.getDayOfMonth());

        // hour
        assertEquals(0, toYear.getHourOfDay());
        assertEquals(0, toMonth.getHourOfDay());
        assertEquals(0, toDay.getHourOfDay());
        assertEquals(date.getHourOfDay(), toHour.getHourOfDay());
        assertEquals(date.getHourOfDay(), toMinute.getHourOfDay());
        assertEquals(date.getHourOfDay(), toSecond.getHourOfDay());

        // minute
        assertEquals(0, toYear.getMinuteOfHour());
        assertEquals(0, toMonth.getMinuteOfHour());
        assertEquals(0, toDay.getMinuteOfHour());
        assertEquals(0, toHour.getMinuteOfHour());
        assertEquals(date.getMinuteOfHour(), toMinute.getMinuteOfHour());
        assertEquals(date.getMinuteOfHour(), toSecond.getMinuteOfHour());

        // second
        assertEquals(0, toYear.getSecondOfMinute());
        assertEquals(0, toMonth.getSecondOfMinute());
        assertEquals(0, toDay.getSecondOfMinute());
        assertEquals(0, toHour.getSecondOfMinute());
        assertEquals(0, toMinute.getSecondOfMinute());
        assertEquals(date.getSecondOfMinute(), toSecond.getSecondOfMinute());
    }

    @Test
    public void DateTime() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(10),     query.select(employee.datefield.dayOfMonth()).fetchFirst());
        assertEquals(Integer.valueOf(2),      query.select(employee.datefield.month()).fetchFirst());
        assertEquals(Integer.valueOf(2000),   query.select(employee.datefield.year()).fetchFirst());
        assertEquals(Integer.valueOf(200002), query.select(employee.datefield.yearMonth()).fetchFirst());
    }

    @Test
    public void DateTime_To_Date() {
        firstResult(SQLExpressions.date(DateTimeExpression.currentTimestamp()));
    }

    private double degrees(double x) {
        return x * 180.0 / Math.PI;
    }

    @Test
    public void Distinct_Count() {
        long count1 = query().from(employee).distinct().fetchCount();
        long count2 = query().from(employee).distinct().fetchCount();
        assertEquals(count1, count2);
    }

    @Test
    public void Distinct_List() {
        List<Integer> lengths1 = query().from(employee).distinct().select(employee.firstname.length()).fetch();
        List<Integer> lengths2 = query().from(employee).distinct().select(employee.firstname.length()).fetch();
        assertEquals(lengths1, lengths2);
    }

    @Test
    public void FactoryExpression_In_GroupBy() {
        Expression<Employee> empBean = Projections.bean(Employee.class, employee.id, employee.superiorId);
        assertTrue(query().from(employee).groupBy(empBean).select(empBean).fetchFirst() != null);
    }

    @Test
    @ExcludeIn({H2, SQLITE, DERBY, CUBRID, MYSQL})
    public void Full_Join() throws SQLException {
        query().from(employee).fullJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void GetResultSet() throws IOException, SQLException{
        ResultSet results = query().from(survey).getResults(survey.id, survey.name);
        while(results.next()) {
            System.out.println(results.getInt(1) +","+results.getString(2));
        }
        results.close();
    }

    @Test
    public void GroupBy_Superior() {
        SQLQuery<?> qry = query()
            .from(employee)
            .innerJoin(employee._superiorIdKey, employee2);

        QTuple subordinates = Projections.tuple(employee2.id, employee2.firstname, employee2.lastname);

        Map<Integer, Group> results = qry.transform(
            GroupBy.groupBy(employee.id).as(employee.firstname, employee.lastname,
            GroupBy.map(employee2.id, subordinates)));

        assertEquals(2, results.size());

        // Mike Smith
        Group group = results.get(1);
        assertEquals("Mike", group.getOne(employee.firstname));
        assertEquals("Smith", group.getOne(employee.lastname));

        Map<Integer, Tuple> emps = group.getMap(employee2.id, subordinates);
        assertEquals(4, emps.size());
        assertEquals("Steve", emps.get(12).get(employee2.firstname));

        // Mary Smith
        group = results.get(2);
        assertEquals("Mary", group.getOne(employee.firstname));
        assertEquals("Smith", group.getOne(employee.lastname));

        emps = group.getMap(employee2.id, subordinates);
        assertEquals(4, emps.size());
        assertEquals("Mason", emps.get(21).get(employee2.lastname));
    }

    @Test
    public void GroupBy_YearMonth() {
        query().from(employee)
               .groupBy(employee.datefield.yearMonth())
               .orderBy(employee.datefield.yearMonth().asc())
               .select(employee.id.count()).fetch();
    }

    @Test
    @ExcludeIn({H2, DB2, DERBY, ORACLE, SQLSERVER})
    public void GroupBy_Validate() {
        NumberPath<BigDecimal> alias = Expressions.numberPath(BigDecimal.class, "alias");
        query().from(employee)
               .groupBy(alias)
                .select(employee.salary.multiply(100).as(alias),
                        employee.salary.avg()).fetch();
    }

    @Test
    @ExcludeIn({FIREBIRD})
    public void GroupBy_Count() {
        List<Integer> ids = query().from(employee).groupBy(employee.id).select(employee.id).fetch();
        long count = query().from(employee).groupBy(employee.id).fetchCount();
        QueryResults<Integer> results = query().from(employee).groupBy(employee.id)
                .limit(1).select(employee.id).fetchResults();

        assertEquals(10, ids.size());
        assertEquals(10, count);
        assertEquals(1, results.getResults().size());
        assertEquals(10, results.getTotal());
    }

    @Test
    @ExcludeIn({FIREBIRD, SQLSERVER, TERADATA})
    public void GroupBy_Distinct_Count() {
        List<Integer> ids = query().from(employee).groupBy(employee.id).distinct().select(Expressions.ONE).fetch();
        QueryResults<Integer> results = query().from(employee).groupBy(employee.id)
                .limit(1).distinct().select(Expressions.ONE).fetchResults();

        assertEquals(1, ids.size());
        assertEquals(1, results.getResults().size());
        assertEquals(1, results.getTotal());
    }

    @Test
    @ExcludeIn({FIREBIRD})
    public void Having_Count() {
        //Produces empty resultset https://github.com/querydsl/querydsl/issues/1055
        query().from(employee)
                .innerJoin(employee2).on(employee.id.eq(employee2.id))
                .groupBy(employee.id)
                .having(Wildcard.count.eq(4L))
                .select(employee.id, employee.firstname).fetchResults();
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void IllegalUnion() throws SQLException {
        SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
        SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.max());
        query().from(employee).union(sq1, sq2).list();
    }

    @Test
    public void In() {
        query().from(employee).where(employee.id.in(Arrays.asList(1, 2))).select(employee);
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER, TERADATA})
    public void In_Long_List() {
        List<Integer> ids = Lists.newArrayList();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(
                query().from(employee).fetchCount(),
                query().from(employee).where(employee.id.in(ids)).fetchCount());
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER, TERADATA})
    public void NotIn_Long_List() {
        List<Integer> ids = Lists.newArrayList();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(0, query().from(employee).where(employee.id.notIn(ids)).fetchCount());
    }

    @Test
    public void In_Empty() {
        assertEquals(0, query().from(employee).where(employee.id.in(ImmutableList.<Integer>of())).fetchCount());
    }

    @Test
    public void NotIn_Empty() {
        long count = query().from(employee).fetchCount();
        assertEquals(count, query().from(employee).where(employee.id.notIn(ImmutableList.<Integer>of())).fetchCount());
    }

    @Test
    public void Inner_Join() throws SQLException {
        query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void Inner_Join_2Conditions() {
        query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .on(employee2.firstname.isNotNull())
            .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void Join() throws Exception {
        for (String name : query().from(survey, survey2)
                .where(survey.id.eq(survey2.id)).select(survey.name).fetch()) {
            System.out.println(name);
        }
    }

    @Test
    public void Joins() throws SQLException {
        for (Tuple row : query().from(employee).innerJoin(employee2)
                .on(employee.superiorId.eq(employee2.superiorId))
                .where(employee2.id.eq(10))
                .select(employee.id, employee2.id).fetch()) {
            System.out.println(row.get(employee.id) + ", " + row.get(employee2.id));
        }
    }

    @Test
    public void Left_Join() throws SQLException {
        query().from(employee).leftJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void Like() {
        query().from(employee).where(employee.firstname.like("\\")).fetchCount();
        query().from(employee).where(employee.firstname.like("\\\\")).fetchCount();
    }

    @Test
    @ExcludeIn(FIREBIRD)
    public void Like_Escape() {
        List<String> strs = ImmutableList.of("%a", "a%", "%a%", "_a", "a_", "_a_", "[C-P]arsen", "a\nb");

        for (String str : strs) {
            assertTrue(str, query()
                .from(employee)
                .where(Expressions.predicate(Ops.STRING_CONTAINS,
                       Expressions.constant(str),
                       Expressions.constant(str))).fetchCount() > 0);
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void Like_Number() {
        assertEquals(5, query().from(employee)
                .where(employee.id.like("1%")).fetchCount());
    }

    @Test
    public void Limit() throws SQLException {
        query().from(employee)
            .orderBy(employee.firstname.asc())
            .limit(4).select(employee.id).fetch();
    }

    @Test
    public void Limit_and_Offset() throws SQLException {
        assertEquals(Arrays.asList(20, 13, 10, 2),
            query().from(employee)
                   .orderBy(employee.firstname.asc())
                   .limit(4).offset(3)
                   .select(employee.id).fetch());
    }

    @Test
    public void Limit_and_Offset_and_Order() {
        List<String> names2 = Arrays.asList("Helen","Jennifer","Jim","Joe");
        assertEquals(names2, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4).offset(2)
                .select(employee.firstname).fetch());
    }

    @Test
    @IncludeIn(DERBY)
    public void Limit_and_Offset_In_Derby() throws SQLException {
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).select(employee.id).fetch();

        // limit
        expectedQuery = "select e.ID from EMPLOYEE e fetch first 4 rows only";
        query().from(employee).limit(4).select(employee.id).fetch();

        // offset
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows";
        query().from(employee).offset(3).select(employee.id).fetch();

    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void Limit_and_Offset_In_Oracle() throws SQLException {
        if (configuration.getUseLiterals()) {
            return;
        }

        // limit
        expectedQuery = "select * from (   select e.ID from EMPLOYEE e ) where rownum <= ?";
        query().from(employee).limit(4).select(employee.id).fetch();

        // offset
        expectedQuery = "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE e  ) a) where rn > ?";
        query().from(employee).offset(3).select(employee.id).fetch();

        // limit offset
        expectedQuery =  "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE e  ) a) where rn > 3 and rownum <= 4";
        query().from(employee).limit(4).offset(3).select(employee.id).fetch();
    }

    @Test
    @ExcludeIn({ORACLE, DB2, DERBY, FIREBIRD, SQLSERVER, CUBRID, TERADATA})
    @SkipForQuoted
    public void Limit_and_Offset2() throws SQLException {
        // limit
        expectedQuery = "select e.ID from EMPLOYEE e limit ?";
        query().from(employee).limit(4).select(employee.id).fetch();

        // limit offset
        expectedQuery = "select e.ID from EMPLOYEE e limit ? offset ?";
        query().from(employee).limit(4).offset(3).select(employee.id).fetch();

    }

    @Test
    public void Limit_and_Order() {
        List<String> names1 = Arrays.asList("Barbara","Daisy","Helen","Jennifer");
        assertEquals(names1, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4)
                .select(employee.firstname).fetch());
    }

    @Test
    public void ListResults() {
        QueryResults<Integer> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .select(employee.id).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    public void ListResults2() {
        QueryResults<Integer> results = query().from(employee)
                .limit(2).offset(10).orderBy(employee.id.asc())
                .select(employee.id).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    public void ListResults_FactoryExpression() {
        QueryResults<Employee> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .select(employee).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void Literals() {
        assertEquals(1L, firstResult(ConstantImpl.create(1)).intValue());
        assertEquals(2L, firstResult(ConstantImpl.create(2L)).longValue());
        assertEquals(3.0, firstResult(ConstantImpl.create(3.0)), 0.001);
        assertEquals(4.0f, firstResult(ConstantImpl.create(4.0f)), 0.001);
        assertEquals(true, firstResult(ConstantImpl.create(true)));
        assertEquals(false, firstResult(ConstantImpl.create(false)));
        assertEquals("abc", firstResult(ConstantImpl.create("abc")));
        assertEquals("'", firstResult(ConstantImpl.create("'")));
        assertEquals("\"", firstResult(ConstantImpl.create("\"")));
        assertEquals("\n", firstResult(ConstantImpl.create("\n")));
        assertEquals("\r\n", firstResult(ConstantImpl.create("\r\n")));
        assertEquals("\t", firstResult(ConstantImpl.create("\t")));
    }

    @Test
    public void Literals_Literals() {
        if (configuration.getUseLiterals()) {
            Literals();
        }
    }

    private double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void LPad() {
        assertEquals("  ab", firstResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4)));
        assertEquals("!!ab", firstResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4, '!')));
    }

//    @Test
//    public void Map() {
//        Map<Integer, String> idToName = query().from(employee).map(employee.id.as("id"), employee.firstname);
//        for (Map.Entry<Integer, String> entry : idToName.entrySet()) {
//            assertNotNull(entry.getKey());
//            assertNotNull(entry.getValue());
//        }
//    }

    @Test
    @SuppressWarnings("serial")
    public void MappingProjection() {
        List<Pair<String, String>> pairs = query().from(employee)
                .select(new MappingProjection<Pair<String, String>>(Pair.class,
                        employee.firstname, employee.lastname) {
                    @Override
                    protected Pair<String, String> map(Tuple row) {
                        return Pair.of(row.get(employee.firstname), row.get(employee.lastname));
                    }
                }).fetch();

        for (Pair<String, String> pair : pairs) {
            assertNotNull(pair.getFirst());
            assertNotNull(pair.getSecond());
        }
    }

    @Test
    public void Math() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "0.50");

        assertEquals(Math.acos(0.5), firstResult(MathExpressions.acos(expr)), 0.001);
        assertEquals(Math.asin(0.5), firstResult(MathExpressions.asin(expr)), 0.001);
        assertEquals(Math.atan(0.5), firstResult(MathExpressions.atan(expr)), 0.001);
        assertEquals(Math.cos(0.5),  firstResult(MathExpressions.cos(expr)), 0.001);
        assertEquals(Math.cosh(0.5), firstResult(MathExpressions.cosh(expr)), 0.001);
        assertEquals(cot(0.5),       firstResult(MathExpressions.cot(expr)), 0.001);
        assertEquals(coth(0.5),      firstResult(MathExpressions.coth(expr)), 0.001);
        assertEquals(degrees(0.5),   firstResult(MathExpressions.degrees(expr)), 0.001);
        assertEquals(Math.exp(0.5),  firstResult(MathExpressions.exp(expr)), 0.001);
        assertEquals(Math.log(0.5),  firstResult(MathExpressions.ln(expr)), 0.001);
        assertEquals(log(0.5, 10),   firstResult(MathExpressions.log(expr, 10)), 0.001);
        assertEquals(0.25,           firstResult(MathExpressions.power(expr, 2)), 0.001);
        assertEquals(radians(0.5),   firstResult(MathExpressions.radians(expr)), 0.001);
        assertEquals(Integer.valueOf(1),
                firstResult(MathExpressions.sign(expr)));
        assertEquals(Math.sin(0.5),  firstResult(MathExpressions.sin(expr)), 0.001);
        assertEquals(Math.sinh(0.5), firstResult(MathExpressions.sinh(expr)), 0.001);
        assertEquals(Math.tan(0.5),  firstResult(MathExpressions.tan(expr)), 0.001);
        assertEquals(Math.tanh(0.5), firstResult(MathExpressions.tanh(expr)), 0.001);
    }

    @Test
    public void Nested_Tuple_Projection() {
        Concatenation concat = new Concatenation(employee.firstname, employee.lastname);
        List<Tuple> tuples = query().from(employee)
                .select(employee.firstname, employee.lastname, concat).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            String firstName = tuple.get(employee.firstname);
            String lastName = tuple.get(employee.lastname);
            assertEquals(firstName + lastName, tuple.get(concat));
        }
    }


    @Test
    public void No_From() {
        assertNotNull(firstResult(DateExpression.currentDate()));
    }

    @Test
    public void Nullif() {
        query().from(employee).select(employee.firstname.nullif(employee.lastname)).fetch();
    }

    @Test
    public void Nullif_Constant() {
        query().from(employee).select(employee.firstname.nullif("xxx")).fetch();
    }

    @Test
    public void Num_Cast() {
        query().from(employee).select(employee.id.castToNum(Long.class)).fetch();
        query().from(employee).select(employee.id.castToNum(Float.class)).fetch();
        query().from(employee).select(employee.id.castToNum(Double.class)).fetch();
    }

    @Test
    public void Num_Cast2() {
        NumberExpression<Integer> num = Expressions.numberTemplate(Integer.class, "0");
        firstResult(num.castToNum(Byte.class));
        firstResult(num.castToNum(Short.class));
        firstResult(num.castToNum(Integer.class));
        firstResult(num.castToNum(Long.class));
        firstResult(num.castToNum(Float.class));
        firstResult(num.castToNum(Double.class));
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, POSTGRESQL})
    public void Number_As_Boolean() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).set(numberTest.col1Boolean, true).execute();
        insert(numberTest).set(numberTest.col1Number, (byte)1).execute();
        assertEquals(2, query().from(numberTest).select(numberTest.col1Boolean).fetch().size());
        assertEquals(2, query().from(numberTest).select(numberTest.col1Number).fetch().size());
    }

    @Test
    public void Number_As_Boolean_Null() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).setNull(numberTest.col1Boolean).execute();
        insert(numberTest).setNull(numberTest.col1Number).execute();
        assertEquals(2, query().from(numberTest).select(numberTest.col1Boolean).fetch().size());
        assertEquals(2, query().from(numberTest).select(numberTest.col1Number).fetch().size());
    }

    @Test
    public void Offset_Only() {
        query().from(employee)
            .orderBy(employee.firstname.asc())
            .offset(3)
            .select(employee.id).fetch();
    }

    @Test
    public void Operation_in_Constant_list() {
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a'))).fetchCount();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b'))).fetchCount();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b','c'))).fetchCount();
    }

    @Test
    public void Order_NullsFirst() {
        query().from(survey)
            .orderBy(survey.name.asc().nullsFirst())
            .select(survey.name).fetch();
    }

    @Test
    public void Order_NullsLast() {
        query().from(survey)
            .orderBy(survey.name.asc().nullsLast())
            .select(survey.name).fetch();
    }

    @Test
    public void Params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .select(employee.firstname).fetchFirst());
    }

    @Test
    public void Params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .select(employee.firstname).fetchFirst());
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .select(employee.firstname).fetchFirst());
    }

    @Test
    @ExcludeIn({DB2, DERBY, FIREBIRD, HSQLDB, ORACLE, SQLSERVER})
    @SkipForQuoted
    public void Path_Alias() {
        expectedQuery = "select e.LASTNAME, sum(e.SALARY) as salarySum " +
                "from EMPLOYEE e " +
                "group by e.LASTNAME having salarySum > ?";

        NumberExpression<BigDecimal> salarySum = employee.salary.sum().as("salarySum");
        query().from(employee)
            .groupBy(employee.lastname)
            .having(salarySum.gt(10000))
            .select(employee.lastname, salarySum).fetch();
    }

    @Test
    public void Path_in_Constant_list() {
        query().from(survey).where(survey.name.in(Arrays.asList("a"))).fetchCount();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b"))).fetchCount();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b","c"))).fetchCount();
    }

    @Test
    public void Precedence() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith")).or(fn.eq("Joe").and(ln.eq("Divis")));
        assertEquals(2L, query().from(employee).where(where).fetchCount());
    }

    @Test
    public void Precedence2() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith").or(fn.eq("Joe")).and(ln.eq("Divis")));
        assertEquals(0L, query().from(employee).where(where).fetchCount());
    }

    @Test
    public void Projection() throws IOException{
        CloseableIterator<Tuple> results = query().from(survey).select(survey.all()).iterate();
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            assertEquals(3, results.next().size());
        }
        results.close();
    }

    @Test
    public void Projection_and_TwoColumns() {
        // projection and two columns
        for (Tuple row : query().from(survey)
                .select(new QIdName(survey.id, survey.name), survey.id, survey.name).fetch()) {
            assertEquals(3, row.size());
            assertEquals(IdName.class, row.get(0, Object.class).getClass());
            assertEquals(Integer.class, row.get(1, Object.class).getClass());
            assertEquals(String.class, row.get(2, Object.class).getClass());
        }
    }

    @Test
    public void Projection2() throws IOException{
        // TODO : add assertions
        CloseableIterator<Tuple> results = query().from(survey).select(survey.id, survey.name).iterate();
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            assertEquals(2, results.next().size());
        }
        results.close();
    }

    @Test
    public void Projection3() throws IOException{
        CloseableIterator<String> names = query().from(survey).select(survey.name).iterate();
        assertTrue(names.hasNext());
        while (names.hasNext()) {
            System.out.println(names.next());
        }
        names.close();
    }

    @Test
    public void QBeanUsage() {
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        List<Survey> surveys =
            query().from(
                    query().from(survey).select(survey.all()).as("sq"))
                    .select(Projections.bean(Survey.class, Collections.singletonMap("name", sq.get(survey.name)))).fetch();
        assertFalse(surveys.isEmpty());

    }

    @Test
    public void Query_with_Constant() throws Exception {
        for (Tuple row : query().from(survey)
                .where(survey.id.eq(1))
                .select(survey.id, survey.name).fetch()) {
            System.out.println(row.get(survey.id) + ", " + row.get(survey.name));
        }
    }

    @Test
    public void Query1() throws Exception {
        for (String s : query().from(survey).select(survey.name).fetch()) {
            System.out.println(s);
        }
    }

    @Test
    public void Query2() throws Exception {
        for (Tuple row : query().from(survey).select(survey.id, survey.name).fetch()) {
            System.out.println(row.get(survey.id) + ", " + row.get(survey.name));
        }
    }

    private double radians(double x) {
        return x * Math.PI / 180.0;
    }

    @Test
    public void Random() {
        firstResult(MathExpressions.random());
    }

    @Test
    @ExcludeIn({FIREBIRD, ORACLE, POSTGRESQL, SQLITE, TERADATA})
    public void Random2() {
        firstResult(MathExpressions.random(10));
    }

    @Test
    public void RelationalPath_Projection() {
        List<Tuple> results = query().from(employee, employee2).where(employee.id.eq(employee2.id))
                .select(employee, employee2).fetch();
        assertFalse(results.isEmpty());
        for (Tuple row : results) {
            Employee e1 = row.get(employee);
            Employee e2 = row.get(employee2);
            assertEquals(e1.getId(), e2.getId());
        }
    }

    @Test
    public void RelationalPath_Eq() {
        query().from(employee, employee2)
                .where(employee.eq(employee2))
                .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void RelationalPath_Ne() {
        query().from(employee, employee2)
                .where(employee.ne(employee2))
                .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void RelationalPath_Eq2() {
        query().from(survey, survey2)
                .where(survey.eq(survey2))
                .select(survey.id, survey2.id).fetch();
    }

    @Test
    public void RelationalPath_Ne2() {
        query().from(survey, survey2)
                .where(survey.ne(survey2))
                .select(survey.id, survey2.id).fetch();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void Right_Join() throws SQLException {
        query().from(employee).rightJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch();
    }

    @Test
    @ExcludeIn(DERBY)
    public void Round() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "1.32");

        assertEquals(Double.valueOf(1.0), firstResult(MathExpressions.round(expr)));
        assertEquals(Double.valueOf(1.3), firstResult(MathExpressions.round(expr, 1)));
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void Rpad() {
        assertEquals("ab  ", firstResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4)));
        assertEquals("ab!!", firstResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4, '!')));
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).select(survey.id.eq(0)).fetch());
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).select(survey.id.gt(0)).fetch());
    }

    @Test
    public void Select_Concat() throws SQLException {
        System.out.println(query().from(survey).select(survey.name.append("Hello World")).fetch());
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, CUBRID, TERADATA})
    public void Select_For_Update() {
        query().from(survey).forUpdate().select(survey.id).fetch();
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, CUBRID, TERADATA})
    public void Select_For_Update_UniqueResult() {
        query().from(survey).forUpdate().select(survey.id).fetchOne();
    }

    @Test
    @SkipForQuoted
    public void Serialization() {
        SQLQuery query = query();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());
        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void Serialization2() throws Exception {
        List<Tuple> rows = query().from(survey).select(survey.id, survey.name).fetch();
        serialize(rows);
    }

    private void serialize(List<Tuple> rows) throws IOException, ClassNotFoundException{
        rows = Serialization.serialize(rows);
        for (Tuple row : rows) {
            row.hashCode();
        }
    }

    @Test
    public void Single() {
        assertNotNull(query().from(survey).select(survey.name).fetchFirst());
    }

    @Test
    public void Single_Array() {
        assertNotNull(query().from(survey).select(new Expression<?>[]{survey.name}).fetchFirst());
    }

    @Test
    public void Single_Column() {
        // single column
        for (String s : query().from(survey).select(survey.name).fetch()) {
            assertNotNull(s);
        }
    }

    @Test
    public void Single_Column_via_Object_type() {
        for (Object s : query().from(survey)
                .select(ExpressionUtils.path(Object.class, survey.name.getMetadata())).fetch()) {
            assertEquals(String.class, s.getClass());
        }
    }

    @Test
    public void SpecialChars() {
        query().from(survey).where(survey.name.in("\n", "\r", "\\", "\'", "\"")).exists();
    }

    @Test
    public void StandardTest() {
        standardTest.runBooleanTests(employee.firstname.isNull(), employee2.lastname.isNotNull());
        // datetime
        standardTest.runDateTests(employee.datefield, employee2.datefield, date);

        // numeric
        standardTest.runNumericCasts(employee.id, employee2.id, 1);
        standardTest.runNumericTests(employee.id, employee2.id, 1);
        // BigDecimal
        standardTest.runNumericTests(employee.salary, employee2.salary, new BigDecimal("30000.00"));

        standardTest.runStringTests(employee.firstname, employee2.firstname, "Jennifer");
        Target target = Connections.getTarget();
        if (target != SQLITE && target != SQLSERVER) {
            // jTDS driver does not support TIME SQL data type
            standardTest.runTimeTests(employee.timefield, employee2.timefield, time);
        }

        standardTest.report();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void String() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals("abcd  ",           firstResult(StringExpressions.ltrim(str)));
        assertEquals(Integer.valueOf(3), firstResult(str.locate("a")));
        assertEquals(Integer.valueOf(0), firstResult(str.locate("a", 4)));
        assertEquals(Integer.valueOf(4), firstResult(str.locate("b", 2)));
        assertEquals("  abcd",           firstResult(StringExpressions.rtrim(str)));
    }

    @Test
    @ExcludeIn({POSTGRESQL, SQLITE})
    public void String_IndexOf() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals(Integer.valueOf(2),  firstResult(str.indexOf("a")));
        assertEquals(Integer.valueOf(-1), firstResult(str.indexOf("a", 4)));
        assertEquals(Integer.valueOf(3), firstResult(str.indexOf("b", 2)));
    }

    @Test
    public void StringFunctions2() throws SQLException {
        for (BooleanExpression where : Arrays.asList(
                employee.firstname.startsWith("a"),
                employee.firstname.startsWithIgnoreCase("a"),
                employee.firstname.endsWith("a"),
                employee.firstname.endsWithIgnoreCase("a"))) {
            query().from(employee).where(where).select(employee.firstname).fetch();
        }
    }

    @Test
    @ExcludeIn(SQLITE)
    public void String_Left() {
        assertEquals("John", query().from(employee).where(employee.lastname.eq("Johnson"))
                                    .select(SQLExpressions.left(employee.lastname, 4)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Right() {
        assertEquals("son", query().from(employee).where(employee.lastname.eq("Johnson"))
                                   .select(SQLExpressions.right(employee.lastname, 3)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Left_Right() {
        assertEquals("hn", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .select(SQLExpressions.right(SQLExpressions.left(employee.lastname, 4), 2)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Right_Left() {
        assertEquals("ns", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .select(SQLExpressions.left(SQLExpressions.right(employee.lastname, 4), 2)).fetchFirst());
    }

    @Test
    @ExcludeIn({DB2, DERBY, FIREBIRD})
    public void Substring() {
        //SELECT * FROM account where SUBSTRING(name, -x, 1) = SUBSTRING(name, -y, 1)
        query().from(employee)
               .where(employee.firstname.substring(-3, 1).eq(employee.firstname.substring(-2, 1)))
               .select(employee.id).fetch();
    }

    @Test
    public void Syntax_For_Employee() throws SQLException {
        query().from(employee).groupBy(employee.superiorId)
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch();

        query().from(employee).groupBy(employee.superiorId)
            .having(employee.id.max().gt(5))
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch();

        query().from(employee).groupBy(employee.superiorId)
            .having(employee.superiorId.isNotNull())
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch();
    }

    @Test
    public void TemplateExpression() {
        NumberExpression<Integer> one = Expressions.numberTemplate(Integer.class, "1");
        query().from(survey).select(one.as("col1")).fetch();
    }

    @Test
    public void Transform_GroupBy() {
        QEmployee employee = new QEmployee("employee");
        QEmployee employee2 = new QEmployee("employee2");
        Map<Integer, Map<Integer, Employee>> results = query().from(employee, employee2)
            .transform(GroupBy.groupBy(employee.id).as(GroupBy.map(employee2.id, employee2)));

        int count = (int) query().from(employee).fetchCount();
        assertEquals(count, results.size());
        for (Map.Entry<Integer, Map<Integer, Employee>> entry : results.entrySet()) {
            Map<Integer, Employee> employees = entry.getValue();
            assertEquals(count, employees.size());
        }

    }

    @Test
    public void Tuple_Projection() {
        List<Tuple> tuples = query().from(employee)
                .select(employee.firstname, employee.lastname).fetch();
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            assertNotNull(tuple.get(employee.firstname));
            assertNotNull(tuple.get(employee.lastname));
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void Tuple2() {
        query().from(employee)
                .select(Expressions.as(ConstantImpl.create("1"), "code"),
                        employee.id).fetch();
    }

    @Test
    public void TwoColumns() {
        // two columns
        for (Tuple row : query().from(survey).select(survey.id, survey.name).fetch()) {
            assertEquals(2, row.size());
            assertEquals(Integer.class, row.get(0, Object.class).getClass());
            assertEquals(String.class, row.get(1, Object.class).getClass());
        }
    }

    @Test
    public void TwoColumns_and_Projection() {
        // two columns and projection
        for (Tuple row : query().from(survey)
                .select(survey.id, survey.name, new QIdName(survey.id, survey.name)).fetch()) {
            assertEquals(3, row.size());
            assertEquals(Integer.class, row.get(0, Object.class).getClass());
            assertEquals(String.class, row.get(1, Object.class).getClass());
            assertEquals(IdName.class, row.get(2, Object.class).getClass());
        }
    }

    @Test
    public void Unique_Constructor_Projection() {
        IdName idAndName = query().from(survey).limit(1).select(new QIdName(survey.id, survey.name)).fetchFirst();
        assertNotNull(idAndName);
        assertNotNull(idAndName.getId());
        assertNotNull(idAndName.getName());
    }

    @Test
    public void Unique_Single() {
        String s = query().from(survey).limit(1).select(survey.name).fetchFirst();
        assertNotNull(s);
    }

    @Test
    public void Unique_Wildcard() {
        // unique wildcard
        Tuple row = query().from(survey).limit(1).select(survey.all()).fetchFirst();
        assertNotNull(row);
        assertEquals(3, row.size());
        assertNotNull(row.get(0, Object.class));
        assertNotNull(row.get(0, Object.class) +" is not null", row.get(1, Object.class));
    }

    @Test(expected=NonUniqueResultException.class)
    public void UniqueResultContract() {
        query().from(employee).select(employee.all()).fetchOne();
    }

    @Test
    public void Various() throws SQLException {
        for (String s : query().from(survey).select(survey.name.lower()).fetch()) {
            assertEquals(s, s.toLowerCase());
        }

        for (String s : query().from(survey).select(survey.name.append("abc")).fetch()) {
            assertTrue(s.endsWith("abc"));
        }

        System.out.println(query().from(survey).select(survey.id.sqrt()).fetch());
    }

    @Test
    public void Where_Exists() throws SQLException {
        SQLQuery<Integer> sq1 = query().from(employee).select(employee.id.max());
        query().from(employee).where(sq1.exists()).fetchCount();
    }

    @Test
    public void Where_Exists_Not() throws SQLException {
        SQLQuery<Integer> sq1 = query().from(employee).select(employee.id.max());
        query().from(employee).where(sq1.exists().not()).fetchCount();
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void With() {
        query().with(employee2, query().from(employee)
                .where(employee.firstname.eq("Tom"))
                .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch();
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void With2() {
        QEmployee employee3 = new QEmployee("e3");
        query().with(employee2, query().from(employee)
                .where(employee.firstname.eq("Tom"))
                .select(Wildcard.all))
               .with(employee2, query().from(employee)
                       .where(employee.firstname.eq("Tom"))
                       .select(Wildcard.all))
               .from(employee, employee2, employee3)
               .select(employee.id, employee2.id, employee3.id).fetch();
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void With3() {
        query().with(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Tom"))
                        .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch();
    }

    @Test
    @IncludeIn({ORACLE, POSTGRESQL})
    public void With_Recursive() {
        query().withRecursive(employee2, query().from(employee)
                .where(employee.firstname.eq("Tom"))
                .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch();
    }


    @Test
    @IncludeIn({ORACLE, POSTGRESQL})
    public void With_Recursive2() {
        query().withRecursive(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Tom"))
                        .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch();
    }

    @Test
    public void Wildcard() {
        // wildcard
        for (Tuple row : query().from(survey).select(survey.all()).fetch()) {
            assertNotNull(row);
            assertEquals(3, row.size());
            assertNotNull(row.get(0, Object.class));
            assertNotNull(row.get(0, Object.class) + " is not null", row.get(1, Object.class));
        }
    }

    @Test
    @SkipForQuoted
    public void Wildcard_All() {
        expectedQuery = "select * from EMPLOYEE e";
        query().from(employee).select(Wildcard.all).fetch();
    }

    @Test
    public void Wildcard_All2() {
        query().from(new RelationalPathBase<Object>(Object.class, "employee", "public", "EMPLOYEE"))
                .select(Wildcard.all).fetch();
    }

    @Test
    public void Wildcard_and_QTuple() {
        // wildcard and QTuple
        for (Tuple tuple : query().from(survey).select(survey.all()).fetch()) {
            assertNotNull(tuple.get(survey.id));
            assertNotNull(tuple.get(survey.name));
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void WithinGroup() {
        List<WithinGroup<?>> exprs = new ArrayList<WithinGroup<?>>();
        NumberPath<Integer> path = survey.id;

        // two args
        add(exprs, SQLExpressions.cumeDist(2, 3));
        add(exprs, SQLExpressions.denseRank(4, 5));
        add(exprs, SQLExpressions.listagg(path, ","));
        add(exprs, SQLExpressions.percentRank(6, 7));
        add(exprs, SQLExpressions.rank(8, 9));

        for (WithinGroup<?> wg : exprs) {
            query().from(survey).select(wg.withinGroup().orderBy(survey.id, survey.id)).fetch();
        }

        // one arg
        exprs.clear();
        add(exprs, SQLExpressions.percentileCont(0.1));
        add(exprs, SQLExpressions.percentileDisc(0.9));

        for (WithinGroup<?> wg : exprs) {
            query().from(survey).select(wg.withinGroup().orderBy(survey.id)).fetch();
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY, H2})
    public void YearWeek() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200006), query.select(employee.datefield.yearWeek()).fetchFirst());
    }

    @Test
    @IncludeIn({H2})
    public void YearWeek_H2() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200007), query.select(employee.datefield.yearWeek()).fetchFirst());
    }


}
