//CHECKSTYLERULE:OFF: FileLength
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
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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

    private final QueryExecution standardTest = new QueryExecution(QuerydslModule.SQL, Connections.getTarget()) {
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
    public void aggregate_list() {
        int min = 30000, avg = 65000, max = 160000;
        // fetch
        assertEquals(min, query().from(employee).select(employee.salary.min()).fetch().get(0).intValue());
        assertEquals(avg, query().from(employee).select(employee.salary.avg()).fetch().get(0).intValue());
        assertEquals(max, query().from(employee).select(employee.salary.max()).fetch().get(0).intValue());
    }

    @Test
    public void aggregate_uniqueResult() {
        int min = 30000, avg = 65000, max = 160000;
        // fetchOne
        assertEquals(min, query().from(employee).select(employee.salary.min()).fetchOne().intValue());
        assertEquals(avg, query().from(employee).select(employee.salary.avg()).fetchOne().intValue());
        assertEquals(max, query().from(employee).select(employee.salary.max()).fetchOne().intValue());
    }

    @Test
    @ExcludeIn(ORACLE)
    @SkipForQuoted
    public void alias() {
        expectedQuery = "select e.ID as id from EMPLOYEE e";
        query().from().select(employee.id.as(employee.id)).from(employee).fetch();
    }

    @Test
    @ExcludeIn({MYSQL, ORACLE})
    @SkipForQuoted
    public void alias_quotes() {
        expectedQuery = "select e.FIRSTNAME as \"First Name\" from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name")).fetch();
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void alias_quotes_MySQL() {
        expectedQuery = "select e.FIRSTNAME as `First Name` from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name")).fetch();
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void alias_quotes_Oracle() {
        expectedQuery = "select e.FIRSTNAME \"First Name\" from EMPLOYEE e";
        query().from(employee).select(employee.firstname.as("First Name"));
    }

    @Test
    public void all() {
        for (Expression<?> expr : survey.all()) {
            Path<?> path = (Path<?>) expr;
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
        assertEquals(6, firstResult(four.divide(two).multiply(three)).intValue());
        assertEquals(1, firstResult(four.divide(two.multiply(two))).intValue());
    }

    @Test
    public void arithmetic() {
        NumberExpression<Integer> one = Expressions.numberTemplate(Integer.class, "(1.0)");
        NumberExpression<Integer> two = Expressions.numberTemplate(Integer.class, "(2.0)");
        NumberExpression<Integer> three = Expressions.numberTemplate(Integer.class, "(3.0)");
        NumberExpression<Integer> four = Expressions.numberTemplate(Integer.class, "(4.0)");
        arithmeticTests(one, two, three, four);
        // the following one doesn't work with integer arguments
        assertEquals(2, firstResult(four.multiply(one.divide(two))).intValue());
    }

    @Test
    public void arithmetic2() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        NumberExpression<Integer> four = Expressions.FOUR;
        arithmeticTests(one, two, three, four);
    }

    @Test
    public void arithmetic_mod() {
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
    public void array() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "'{1,2,3}'::int[]");
        Integer[] result = firstResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0].intValue());
        assertEquals(2, result[1].intValue());
        assertEquals(3, result[2].intValue());
    }

    @Test
    @IncludeIn(POSTGRESQL) // TODO generalize array literal projections
    public void array2() {
        Expression<int[]> expr = Expressions.template(int[].class, "'{1,2,3}'::int[]");
        int[] result = firstResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
        assertEquals(3, result[2]);
    }

    @Test
    @ExcludeIn({DERBY, HSQLDB})
    public void array_null() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "null");
        assertNull(firstResult(expr));
    }

    @Test
    public void array_projection() {
        List<String[]> results = query().from(employee).select(
                new ArrayConstructorExpression<String>(String[].class, employee.firstname)).fetch();
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void beans() {
        List<Beans> rows = query().from(employee, employee2).select(new QBeans(employee, employee2)).fetch();
        assertFalse(rows.isEmpty());
        for (Beans row : rows) {
            assertEquals(Employee.class, row.get(employee).getClass());
            assertEquals(Employee.class, row.get(employee2).getClass());
        }
    }

    @Test
    public void between() {
        // 11-13
        assertEquals(Arrays.asList(11, 12, 13),
                query().from(employee).where(employee.id.between(11, 13)).orderBy(employee.id.asc())
                       .select(employee.id).fetch());
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void boolean_all() {
        assertTrue(query().from(employee).select(SQLExpressions.all(employee.firstname.isNotNull())).fetchOne());
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void boolean_any() {
        assertTrue(query().from(employee).select(SQLExpressions.any(employee.firstname.isNotNull())).fetchOne());
    }

    @Test
    public void case_() {
        NumberExpression<Float> numExpression = employee.salary.floatValue().divide(employee2.salary.floatValue()).multiply(100.1);
        NumberExpression<Float> numExpression2 = employee.id.when(0).then(0.0F).otherwise(numExpression);
        assertEquals(Arrays.asList(87, 90, 88, 87, 83, 80, 75),
                query().from(employee, employee2)
                        .where(employee.id.eq(employee2.id.add(1)))
                        .orderBy(employee.id.asc(), employee2.id.asc())
                        .select(numExpression2.floor().intValue()).fetch());
    }

    @Test
    public void casts() throws SQLException {
        NumberExpression<?> num = employee.id;
        List<Expression<?>> exprs = new ArrayList<>();

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
    public void coalesce() {
        Coalesce<String> c = new Coalesce<String>(employee.firstname, employee.lastname).add("xxx");
        assertEquals(Collections.emptyList(),
                query().from(employee).where(c.getValue().eq("xxx")).select(employee.id).fetch());
    }

    @Test
    public void compact_join() {
        // verbose
        assertEquals(8, query().from(employee)
            .innerJoin(employee2)
            .on(employee.superiorId.eq(employee2.id))
            .select(employee.id, employee2.id).fetch().size());

        // compact
        assertEquals(8, query().from(employee)
            .innerJoin(employee.superiorIdKey, employee2)
            .select(employee.id, employee2.id).fetch().size());

    }

    @Test
    public void complex_boolean() {
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
    public void complex_subQuery() {
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
    public void constructor_projection() {
        for (IdName idAndName : query().from(survey).select(new QIdName(survey.id, survey.name)).fetch()) {
            assertNotNull(idAndName);
            assertNotNull(idAndName.getId());
            assertNotNull(idAndName.getName());
        }
    }

    @Test
    public void constructor_projection2() {
        List<SimpleProjection> projections = query().from(employee).select(
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
    public void count_with_pK() {
        assertEquals(10, query().from(employee).fetchCount());
    }

    @Test
    public void count_without_pK() {
        assertEquals(10, query().from(QEmployeeNoPK.employee).fetchCount());
    }

    @Test
    public void count2() {
        assertEquals(10, query().from(employee).select(employee.count()).fetchFirst().intValue());
    }

    @Test
    @SkipForQuoted
    @ExcludeIn(ORACLE)
    public void count_all() {
        expectedQuery = "select count(*) as rc from EMPLOYEE e";
        NumberPath<Long> rowCount = Expressions.numberPath(Long.class, "rc");
        assertEquals(10, query().from(employee).select(Wildcard.count.as(rowCount)).fetchOne().intValue());
    }

    @Test
    @SkipForQuoted
    @IncludeIn(ORACLE)
    public void count_all_Oracle() {
        expectedQuery = "select count(*) rc from EMPLOYEE e";
        NumberPath<Long> rowCount = Expressions.numberPath(Long.class, "rc");
        assertEquals(10, query().from(employee).select(Wildcard.count.as(rowCount)).fetchOne().intValue());
    }

    @Test
    public void count_distinct_with_pK() {
        assertEquals(10, query().from(employee).distinct().fetchCount());
    }

    @Test
    public void count_distinct_without_pK() {
        assertEquals(10, query().from(QEmployeeNoPK.employee).distinct().fetchCount());
    }

    @Test
    public void count_distinct2() {
        query().from(employee).select(employee.countDistinct()).fetchFirst();
    }

    @Test
    public void custom_projection() {
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
    public void dates() {
        long ts = ((long) Math.floor(System.currentTimeMillis() / 1000)) * 1000;
        long tsDate = new org.joda.time.LocalDate(ts).toDateMidnight().getMillis();
        long tsTime = new org.joda.time.LocalTime(ts).getMillisOfDay();

        List<Object> data = new ArrayList<>();
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

        java.time.Instant javaInstant = java.time.Instant.now().truncatedTo(java.time.temporal.ChronoUnit.SECONDS);
        java.time.LocalDateTime javaDateTime = java.time.LocalDateTime.ofInstant(javaInstant, java.time.ZoneId.of("Z"));
        java.time.LocalDate javaDate = javaDateTime.toLocalDate();
        java.time.LocalTime javaTime = javaDateTime.toLocalTime();
        data.add(javaInstant);                                      //java.time.Instant
        data.add(javaDateTime);                                     //java.time.LocalDateTime
        data.add(javaDate);                                         //java.time.LocalDate
        data.add(javaTime);                                         //java.time.LocalTime
        data.add(javaDateTime.atOffset(java.time.ZoneOffset.UTC));  //java.time.OffsetDateTime
        data.add(javaTime.atOffset(java.time.ZoneOffset.UTC));      //java.time.OffsetTime
        data.add(javaDateTime.atZone(java.time.ZoneId.of("Z")));    //java.time.ZonedDateTime

        Map<Object, Object> failures = new IdentityHashMap<>();
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
    @Ignore // FIXME
    @ExcludeIn({CUBRID, DB2, DERBY, HSQLDB, POSTGRESQL, SQLITE, TERADATA})
    public void dates_cST() {
        TimeZone tz = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("CST")); // -6:00
            dates();
        } finally {
            TimeZone.setDefault(tz);
        }
    }

    @Test
    @Ignore // FIXME
    @ExcludeIn({CUBRID, DB2, DERBY, HSQLDB, POSTGRESQL, SQLITE, TERADATA})
    public void dates_iOT() {
        TimeZone tz = TimeZone.getDefault();
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("IOT")); // +6:00
            dates();
        } finally {
            TimeZone.setDefault(tz);
        }
    }

    @Test
    @ExcludeIn({CUBRID, SQLITE, TERADATA})
    public void dates_literals() {
        if (configuration.getUseLiterals()) {
            dates();
        }
    }

    @Test
    @ExcludeIn({SQLITE})
    public void date_add() {
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
    public void date_add_Timestamp() {
        List<Expression<?>> exprs = new ArrayList<>();
        DateTimeExpression<java.util.Date> dt = Expressions.currentTimestamp();

        add(exprs, SQLExpressions.addYears(dt, 1));
        add(exprs, SQLExpressions.addMonths(dt, 1), ORACLE);
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
    public void date_diff() {
        QEmployee employee2 = new QEmployee("employee2");
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        SQLQuery<?> query2 = query().from(employee, employee2)
                .orderBy(employee.id.asc(), employee2.id.desc());

        List<DatePart> dps = new ArrayList<>();
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

        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
        for (DatePart dp : dps) {
            query.select(SQLExpressions.datediff(dp, Expressions.currentTimestamp(), timestamp)).fetchOne();
        }
    }

    // TDO Date_diff with timestamps

    @Test
    @ExcludeIn({DB2, HSQLDB, SQLITE, TERADATA})
    public void date_diff2() {
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
    public void date_trunc() {
        DateTimeExpression<java.util.Date> expr = DateTimeExpression.currentTimestamp();

        List<DatePart> dps = new ArrayList<>();
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
    public void date_trunc2() {
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
    public void dateTime() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(10),     query.select(employee.datefield.dayOfMonth()).fetchFirst());
        assertEquals(Integer.valueOf(2),      query.select(employee.datefield.month()).fetchFirst());
        assertEquals(Integer.valueOf(2000),   query.select(employee.datefield.year()).fetchFirst());
        assertEquals(Integer.valueOf(200002), query.select(employee.datefield.yearMonth()).fetchFirst());
    }

    @Test
    public void dateTime_to_date() {
        firstResult(SQLExpressions.date(DateTimeExpression.currentTimestamp()));
    }

    private double degrees(double x) {
        return x * 180.0 / Math.PI;
    }

    @Test
    public void distinct_count() {
        long count1 = query().from(employee).distinct().fetchCount();
        long count2 = query().from(employee).distinct().fetchCount();
        assertEquals(count1, count2);
    }

    @Test
    public void distinct_list() {
        List<Integer> lengths1 = query().from(employee).distinct().select(employee.firstname.length()).fetch();
        List<Integer> lengths2 = query().from(employee).distinct().select(employee.firstname.length()).fetch();
        assertEquals(lengths1, lengths2);
    }

    @Test
    public void duplicate_columns() {
        assertEquals(10, query().from(employee)
                .select(employee.id, employee.id).fetch().size());
    }

    @Test
    public void duplicate_columns_In_Subquery() {
        QEmployee employee2 = new QEmployee("e2");
        assertEquals(10, query().from(employee).where(
                query().from(employee2)
                        .where(employee2.id.eq(employee.id))
                        .select(employee2.id, employee2.id).exists()).fetchCount());
    }

    @Test
    public void factoryExpression_in_groupBy() {
        Expression<Employee> empBean = Projections.bean(Employee.class, employee.id, employee.superiorId);
        assertTrue(query().from(employee).groupBy(empBean).select(empBean).fetchFirst() != null);
    }

    @Test
    @ExcludeIn({H2, SQLITE, DERBY, CUBRID, MYSQL})
    public void full_join() throws SQLException {
        assertEquals(18, query().from(employee).fullJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void getResultSet() throws IOException, SQLException {
        ResultSet results = query().select(survey.id, survey.name).from(survey).getResults();
        while (results.next()) {
            assertNotNull(results.getObject(1));
            assertNotNull(results.getObject(2));
        }
        results.close();
    }

    @Test
    public void groupBy_superior() {
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
    public void groupBy_yearMonth() {
        assertEquals(Collections.singletonList(10L), query().from(employee)
               .groupBy(employee.datefield.yearMonth())
               .orderBy(employee.datefield.yearMonth().asc())
               .select(employee.id.count()).fetch());
    }

    @Test
    @ExcludeIn({H2, DB2, DERBY, ORACLE, SQLSERVER})
    public void groupBy_validate() {
        NumberPath<BigDecimal> alias = Expressions.numberPath(BigDecimal.class, "alias");
        assertEquals(8, query().from(employee)
               .groupBy(alias)
                .select(employee.salary.multiply(100).as(alias),
                        employee.salary.avg()).fetch().size());
    }

    @Test
    @ExcludeIn({FIREBIRD})
    public void groupBy_count() {
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
    public void groupBy_Distinct_count() {
        List<Integer> ids = query().from(employee).groupBy(employee.id).distinct().select(Expressions.ONE).fetch();
        QueryResults<Integer> results = query().from(employee).groupBy(employee.id)
                .limit(1).distinct().select(Expressions.ONE).fetchResults();

        assertEquals(1, ids.size());
        assertEquals(1, results.getResults().size());
        assertEquals(1, results.getTotal());
    }

    @Test
    @ExcludeIn({FIREBIRD})
    public void having_count() {
        //Produces empty resultset https://github.com/querydsl/querydsl/issues/1055
        query().from(employee)
                .innerJoin(employee2).on(employee.id.eq(employee2.id))
                .groupBy(employee.id)
                .having(Wildcard.count.eq(4L))
                .select(employee.id, employee.firstname).fetchResults();
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void illegalUnion() throws SQLException {
        SubQueryExpression<Integer> sq1 = query().from(employee).select(employee.id.max());
        SubQueryExpression<Integer> sq2 = query().from(employee).select(employee.id.max());
        assertEquals(0, query().from(employee).union(sq1, sq2).list().size());
    }

    @Test
    public void in() {
        assertEquals(2, query().from(employee)
                .where(employee.id.in(Arrays.asList(1, 2)))
                .select(employee).fetch().size());
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER, TERADATA})
    public void in_long_list() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(
                query().from(employee).fetchCount(),
                query().from(employee).where(employee.id.in(ids)).fetchCount());
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER, TERADATA})
    public void notIn_long_list() {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(0, query().from(employee).where(employee.id.notIn(ids)).fetchCount());
    }

    @Test
    public void in_empty() {
        assertEquals(0, query().from(employee).where(employee.id.in(Collections.emptyList())).fetchCount());
    }

    @Test
    @ExcludeIn(DERBY)
    public void in_null() {
        assertEquals(1, query().from(employee).where(employee.id.in(1, null)).fetchCount());
    }

    @Test
    @ExcludeIn({MYSQL, TERADATA})
    public void in_subqueries() {
        QEmployee e1 = new QEmployee("e1");
        QEmployee e2 = new QEmployee("e2");
        assertEquals(2, query().from(employee).where(employee.id.in(
            query().from(e1).where(e1.firstname.eq("Mike")).select(e1.id),
            query().from(e2).where(e2.firstname.eq("Mary")).select(e2.id)
        )).fetchCount());
    }

    @Test
    public void notIn_empty() {
        long count = query().from(employee).fetchCount();
        assertEquals(count, query().from(employee).where(employee.id.notIn(Collections.emptyList())).fetchCount());
    }

    @Test
    public void inner_join() throws SQLException {
        assertEquals(8, query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void inner_join_2Conditions() {
        assertEquals(8, query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .on(employee2.firstname.isNotNull())
            .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void join() throws Exception {
        for (String name : query().from(survey, survey2)
                .where(survey.id.eq(survey2.id)).select(survey.name).fetch()) {
            assertNotNull(name);
        }
    }

    @Test
    public void joins() throws SQLException {
        for (Tuple row : query().from(employee).innerJoin(employee2)
                .on(employee.superiorId.eq(employee2.superiorId))
                .where(employee2.id.eq(10))
                .select(employee.id, employee2.id).fetch()) {
            assertNotNull(row.get(employee.id));
            assertNotNull(row.get(employee2.id));
        }
    }

    @Test
    public void left_join() throws SQLException {
        assertEquals(10, query().from(employee).leftJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void like() {
        assertEquals(0, query().from(employee).where(employee.firstname.like("\\")).fetchCount());
        assertEquals(0, query().from(employee).where(employee.firstname.like("\\\\")).fetchCount());
    }

    @Test
    public void like_ignore_case() {
        assertEquals(3, query().from(employee).where(employee.firstname.likeIgnoreCase("%m%")).fetchCount());
    }

    @Test
    @ExcludeIn(FIREBIRD)
    public void like_escape() {
        List<String> strs = Arrays.asList("%a", "a%", "%a%", "_a", "a_", "_a_", "[C-P]arsen", "a\nb");

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
    public void like_number() {
        assertEquals(5, query().from(employee)
                .where(employee.id.like("1%")).fetchCount());
    }

    @Test
    public void limit() throws SQLException {
        assertEquals(Arrays.asList(23, 22, 21, 20), query().from(employee)
            .orderBy(employee.firstname.asc())
            .limit(4).select(employee.id).fetch());
    }

    @Test
    public void limit_and_offset() throws SQLException {
        assertEquals(Arrays.asList(20, 13, 10, 2),
            query().from(employee)
                   .orderBy(employee.firstname.asc())
                   .limit(4).offset(3)
                   .select(employee.id).fetch());
    }

    @Test
    public void limit_and_offset_Group() {
        assertEquals(9,
                query().from(employee)
                       .orderBy(employee.id.asc())
                       .limit(100).offset(1)
                .transform(GroupBy.groupBy(employee.id).as(employee)).size());
    }

    @Test
    public void limit_and_offset_and_Order() {
        List<String> names2 = Arrays.asList("Helen","Jennifer","Jim","Joe");
        assertEquals(names2, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4).offset(2)
                .select(employee.firstname).fetch());
    }

    @Test
    @IncludeIn(DERBY)
    public void limit_and_offset_In_Derby() throws SQLException {
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
    public void limit_and_offset_In_Oracle() throws SQLException {
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
    public void limit_and_offset2() throws SQLException {
        // limit
        expectedQuery = "select e.ID from EMPLOYEE e limit ?";
        query().from(employee).limit(4).select(employee.id).fetch();

        // limit offset
        expectedQuery = "select e.ID from EMPLOYEE e limit ? offset ?";
        query().from(employee).limit(4).offset(3).select(employee.id).fetch();

    }

    @Test
    public void limit_and_order() {
        List<String> names1 = Arrays.asList("Barbara","Daisy","Helen","Jennifer");
        assertEquals(names1, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4)
                .select(employee.firstname).fetch());
    }

    @Test
    public void listResults() {
        QueryResults<Integer> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .select(employee.id).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    public void listResults2() {
        QueryResults<Integer> results = query().from(employee)
                .limit(2).offset(10).orderBy(employee.id.asc())
                .select(employee.id).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    public void listResults_factoryExpression() {
        QueryResults<Employee> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .select(employee).fetchResults();
        assertEquals(10, results.getTotal());
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void literals() {
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
    public void literals_literals() {
        if (configuration.getUseLiterals()) {
            literals();
        }
    }

    private double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void lPad() {
        assertEquals("  ab", firstResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4)));
        assertEquals("!!ab", firstResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4, '!')));
    }

//    @Test
//    public void map() {
//        Map<Integer, String> idToName = query().from(employee).map(employee.id.as("id"), employee.firstname);
//        for (Map.Entry<Integer, String> entry : idToName.entrySet()) {
//            assertNotNull(entry.getKey());
//            assertNotNull(entry.getValue());
//        }
//    }

    @Test
    @SuppressWarnings("serial")
    public void mappingProjection() {
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
    @ExcludeIn({HSQLDB}) // FIXME
    public void math() {
        math(Expressions.numberTemplate(Double.class, "0.50"));
    }

    @Test
    @ExcludeIn({FIREBIRD, SQLSERVER, HSQLDB}) // FIXME
    public void math2() {
        math(Expressions.constant(0.5));
    }

    private void math(Expression<Double> expr) {
        double precision = 0.001;
        assertEquals(Math.acos(0.5), firstResult(MathExpressions.acos(expr)), precision);
        assertEquals(Math.asin(0.5), firstResult(MathExpressions.asin(expr)), precision);
        assertEquals(Math.atan(0.5), firstResult(MathExpressions.atan(expr)), precision);
        assertEquals(Math.cos(0.5),  firstResult(MathExpressions.cos(expr)), precision);
        assertEquals(Math.cosh(0.5), firstResult(MathExpressions.cosh(expr)), precision);
        assertEquals(cot(0.5),       firstResult(MathExpressions.cot(expr)), precision);
        if (target != Target.DERBY || expr instanceof Constant) {
            // FIXME: The resulting value is outside the range for the data type DECIMAL/NUMERIC(4,4).
            assertEquals(coth(0.5),      firstResult(MathExpressions.coth(expr)), precision);
        }

        assertEquals(degrees(0.5),   firstResult(MathExpressions.degrees(expr)), precision);
        assertEquals(Math.exp(0.5),  firstResult(MathExpressions.exp(expr)), precision);
        assertEquals(Math.log(0.5),  firstResult(MathExpressions.ln(expr)), precision);
        assertEquals(log(0.5, 10),   firstResult(MathExpressions.log(expr, 10)), precision);
        assertEquals(0.25,           firstResult(MathExpressions.power(expr, 2)), precision);
        assertEquals(radians(0.5),   firstResult(MathExpressions.radians(expr)), precision);
        assertEquals(Integer.valueOf(1),
                firstResult(MathExpressions.sign(expr)));
        assertEquals(Math.sin(0.5),  firstResult(MathExpressions.sin(expr)), precision);
        assertEquals(Math.sinh(0.5), firstResult(MathExpressions.sinh(expr)), precision);
        assertEquals(Math.tan(0.5),  firstResult(MathExpressions.tan(expr)), precision);
        assertEquals(Math.tanh(0.5), firstResult(MathExpressions.tanh(expr)), precision);
    }

    @Test
    @ExcludeIn(DERBY) // Derby doesn't support mod with decimal operands
    public void math3() {
        // 1.0 + 2.0 * 3.0 - 4.0 / 5.0 + 6.0 % 3.0
        NumberTemplate<Double> one = Expressions.numberTemplate(Double.class, "1.0");
        NumberTemplate<Double> two = Expressions.numberTemplate(Double.class, "2.0");
        NumberTemplate<Double> three = Expressions.numberTemplate(Double.class, "3.0");
        NumberTemplate<Double> four = Expressions.numberTemplate(Double.class, "4.0");
        NumberTemplate<Double> five = Expressions.numberTemplate(Double.class, "5.0");
        NumberTemplate<Double> six = Expressions.numberTemplate(Double.class, "6.0");
        Double num = query().select(one.add(two.multiply(three)).subtract(four.divide(five)).add(six.mod(three))).fetchFirst();
        assertEquals(6.2, num, 0.001);
    }

    @Test
    public void nested_tuple_projection() {
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
    public void no_from() {
        assertNotNull(firstResult(DateExpression.currentDate()));
    }

    @Test
    public void nullif() {
        query().from(employee).select(employee.firstname.nullif(employee.lastname)).fetch();
    }

    @Test
    public void nullif_constant() {
        query().from(employee).select(employee.firstname.nullif("xxx")).fetch();
    }

    @Test
    public void num_cast() {
        query().from(employee).select(employee.id.castToNum(Long.class)).fetch();
        query().from(employee).select(employee.id.castToNum(Float.class)).fetch();
        query().from(employee).select(employee.id.castToNum(Double.class)).fetch();
    }

    @Test
    public void num_cast2() {
        NumberExpression<Integer> num = Expressions.numberTemplate(Integer.class, "0");
        firstResult(num.castToNum(Byte.class));
        firstResult(num.castToNum(Short.class));
        firstResult(num.castToNum(Integer.class));
        firstResult(num.castToNum(Long.class));
        firstResult(num.castToNum(Float.class));
        firstResult(num.castToNum(Double.class));
    }

    @Test
    public void num_date_operation() {
        long result = query()
                .select(employee.datefield.year().mod(1))
                .from(employee)
                .fetchFirst();
        assertEquals(0, result);
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, POSTGRESQL})
    public void number_as_boolean() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).set(numberTest.col1Boolean, true).execute();
        insert(numberTest).set(numberTest.col1Number, (byte) 1).execute();
        assertEquals(2, query().from(numberTest).select(numberTest.col1Boolean).fetch().size());
        assertEquals(2, query().from(numberTest).select(numberTest.col1Number).fetch().size());
    }

    @Test
    public void number_as_boolean_Null() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).setNull(numberTest.col1Boolean).execute();
        insert(numberTest).setNull(numberTest.col1Number).execute();
        assertEquals(2, query().from(numberTest).select(numberTest.col1Boolean).fetch().size());
        assertEquals(2, query().from(numberTest).select(numberTest.col1Number).fetch().size());
    }

    @Test
    public void offset_only() {
        assertEquals(Arrays.asList(20, 13, 10, 2, 1, 11, 12), query().from(employee)
            .orderBy(employee.firstname.asc())
            .offset(3)
            .select(employee.id).fetch());
    }

    @Test
    public void operation_in_constant_list() {
        assertEquals(0, query().from(survey).where(survey.name.charAt(0).in(Collections.singletonList('a'))).fetchCount());
        assertEquals(0, query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b'))).fetchCount());
        assertEquals(0, query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b','c'))).fetchCount());
    }

    @Test
    public void order_nullsFirst() {
        assertEquals(Collections.singletonList("Hello World"), query().from(survey)
            .orderBy(survey.name.asc().nullsFirst())
            .select(survey.name).fetch());
    }

    @Test
    public void order_nullsLast() {
        assertEquals(Collections.singletonList("Hello World"), query().from(survey)
            .orderBy(survey.name.asc().nullsLast())
            .select(survey.name).fetch());
    }

    @Test
    public void params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .select(employee.firstname).fetchFirst());
    }

    @Test
    public void params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .select(employee.firstname).fetchFirst());
    }

    @Test(expected = ParamNotSetException.class)
    public void params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike", query()
                .from(employee).where(employee.firstname.eq(name))
                .select(employee.firstname).fetchFirst());
    }

    @Test
    @ExcludeIn({DB2, DERBY, FIREBIRD, HSQLDB, ORACLE, SQLSERVER})
    @SkipForQuoted
    public void path_alias() {
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
    public void path_in_constant_list() {
        assertEquals(0, query().from(survey).where(survey.name.in(Collections.singletonList("a"))).fetchCount());
        assertEquals(0, query().from(survey).where(survey.name.in(Arrays.asList("a","b"))).fetchCount());
        assertEquals(0, query().from(survey).where(survey.name.in(Arrays.asList("a","b","c"))).fetchCount());
    }

    @Test
    public void precedence() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith")).or(fn.eq("Joe").and(ln.eq("Divis")));
        assertEquals(2L, query().from(employee).where(where).fetchCount());
    }

    @Test
    public void precedence2() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith").or(fn.eq("Joe")).and(ln.eq("Divis")));
        assertEquals(0L, query().from(employee).where(where).fetchCount());
    }

    @Test
    public void projection() throws IOException {
        CloseableIterator<Tuple> results = query().from(survey).select(survey.all()).iterate();
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            assertEquals(3, results.next().size());
        }
        results.close();
    }

    @Test
    public void projection_and_twoColumns() {
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
    public void projection2() throws IOException {
        CloseableIterator<Tuple> results = query().from(survey).select(survey.id, survey.name).iterate();
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            assertEquals(2, results.next().size());
        }
        results.close();
    }

    @Test
    public void projection3() throws IOException {
        CloseableIterator<String> names = query().from(survey).select(survey.name).iterate();
        assertTrue(names.hasNext());
        while (names.hasNext()) {
            System.out.println(names.next());
        }
        names.close();
    }

    @Test
    public void qBeanUsage() {
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        List<Survey> surveys =
            query().from(
                    query().from(survey).select(survey.all()).as("sq"))
                    .select(Projections.bean(Survey.class, Collections.singletonMap("name", sq.get(survey.name)))).fetch();
        assertFalse(surveys.isEmpty());

    }

    @Test
    public void query_with_constant() throws Exception {
        for (Tuple row : query().from(survey)
                .where(survey.id.eq(1))
                .select(survey.id, survey.name).fetch()) {
            assertNotNull(row.get(survey.id));
            assertNotNull(row.get(survey.name));
        }
    }

    @Test
    public void query1() throws Exception {
        for (String s : query().from(survey).select(survey.name).fetch()) {
            assertNotNull(s);
        }
    }

    @Test
    public void query2() throws Exception {
        for (Tuple row : query().from(survey).select(survey.id, survey.name).fetch()) {
            assertNotNull(row.get(survey.id));
            assertNotNull(row.get(survey.name));
        }
    }

    private double radians(double x) {
        return x * Math.PI / 180.0;
    }

    @Test
    public void random() {
        firstResult(MathExpressions.random());
    }

    @Test
    @ExcludeIn({FIREBIRD, ORACLE, POSTGRESQL, SQLITE, TERADATA})
    public void random2() {
        firstResult(MathExpressions.random(10));
    }

    @Test
    public void relationalPath_projection() {
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
    public void relationalPath_eq() {
        assertEquals(10, query().from(employee, employee2)
                .where(employee.eq(employee2))
                .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void relationalPath_ne() {
        assertEquals(90, query().from(employee, employee2)
                .where(employee.ne(employee2))
                .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void relationalPath_eq2() {
        assertEquals(1, query().from(survey, survey2)
                .where(survey.eq(survey2))
                .select(survey.id, survey2.id).fetch().size());
    }

    @Test
    public void relationalPath_ne2() {
        assertEquals(0, query().from(survey, survey2)
                .where(survey.ne(survey2))
                .select(survey.id, survey2.id).fetch().size());
    }

    @Test
    @ExcludeIn(SQLITE)
    public void right_join() throws SQLException {
        assertEquals(16, query().from(employee).rightJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    @ExcludeIn(DERBY)
    public void round() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "1.32");

        assertEquals(Double.valueOf(1.0), firstResult(MathExpressions.round(expr)));
        assertEquals(Double.valueOf(1.3), firstResult(MathExpressions.round(expr, 1)));
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void rpad() {
        assertEquals("ab  ", firstResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4)));
        assertEquals("ab!!", firstResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4, '!')));
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void select_booleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).select(survey.id.eq(0)).fetch());
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void select_booleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).select(survey.id.gt(0)).fetch());
    }

    @Test
    public void select_booleanExpr3() {
        assertTrue(query().select(Expressions.TRUE).fetchFirst());
        assertFalse(query().select(Expressions.FALSE).fetchFirst());
    }

    @Test
    public void select_concat() throws SQLException {
        for (Tuple row : query().from(survey).select(survey.name, survey.name.append("Hello World")).fetch()) {
            assertEquals(
                    row.get(survey.name) + "Hello World",
                    row.get(survey.name.append("Hello World")));
        }
    }

    @Test
    @ExcludeIn({SQLITE, CUBRID, TERADATA})
    public void select_for_update() {
        assertEquals(1, query().from(survey).forUpdate().select(survey.id).fetch().size());
    }

    @Test
    @ExcludeIn({SQLITE, CUBRID, TERADATA})
    public void select_for_update_Where() {
        assertEquals(1, query().from(survey).forUpdate().where(survey.id.isNotNull()).select(survey.id).fetch().size());
    }

    @Test
    @ExcludeIn({SQLITE, CUBRID, TERADATA})
    public void select_for_update_UniqueResult() {
        query().from(survey).forUpdate().select(survey.id).fetchOne();
    }

    @Test
    public void select_for_share() {
        if (configuration.getTemplates().isForShareSupported()) {
            assertEquals(1, query().from(survey).forShare().where(survey.id.isNotNull()).select(survey.id).fetch().size());
        } else {
            try {
                query().from(survey).forShare().where(survey.id.isNotNull()).select(survey.id).fetch().size();
                fail();
            } catch (QueryException e) {
                assertTrue(e.getMessage().equals("Using forShare() is not supported"));
            }
        }
    }

    @Test
    @SkipForQuoted
    public void serialization() {
        SQLQuery<?> query = query();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());
        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void serialization2() throws Exception {
        List<Tuple> rows = query().from(survey).select(survey.id, survey.name).fetch();
        serialize(rows);
    }

    private void serialize(List<Tuple> rows) throws IOException, ClassNotFoundException {
        rows = Serialization.serialize(rows);
        for (Tuple row : rows) {
            row.hashCode();
        }
    }

    @Test
    public void single() {
        assertNotNull(query().from(survey).select(survey.name).fetchFirst());
    }

    @Test
    public void single_array() {
        assertNotNull(query().from(survey).select(new Expression<?>[]{survey.name}).fetchFirst());
    }

    @Test
    public void single_column() {
        // single column
        for (String s : query().from(survey).select(survey.name).fetch()) {
            assertNotNull(s);
        }
    }

    @Test
    public void single_column_via_Object_type() {
        for (Object s : query().from(survey)
                .select(ExpressionUtils.path(Object.class, survey.name.getMetadata())).fetch()) {
            assertEquals(String.class, s.getClass());
        }
    }

    @Test
    public void specialChars() {
        assertEquals(0, query().from(survey).where(survey.name.in("\n", "\r", "\\", "\'", "\"")).fetchCount());
    }

    @Test
    public void standardTest() {
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
    @IncludeIn(H2)
    public void standardTest_turkish() {
        Locale defaultLocale = Locale.getDefault();
        Locale.setDefault(new Locale("tr", "TR"));
        try {
            standardTest();
        } finally {
            Locale.setDefault(defaultLocale);
        }
    }

    @Test
    @ExcludeIn(SQLITE)
    public void string() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals("abcd  ",           firstResult(StringExpressions.ltrim(str)));
        assertEquals(Integer.valueOf(3), firstResult(str.locate("a")));
        assertEquals(Integer.valueOf(0), firstResult(str.locate("a", 4)));
        assertEquals(Integer.valueOf(4), firstResult(str.locate("b", 2)));
        assertEquals("  abcd",           firstResult(StringExpressions.rtrim(str)));
        assertEquals("abc",              firstResult(str.substring(2, 5)));
    }

    @Test
    @ExcludeIn(SQLITE)
    public void string_withTemplate() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        NumberExpression<Integer> four = Expressions.numberTemplate(Integer.class, "4");
        NumberExpression<Integer> two = Expressions.numberTemplate(Integer.class, "2");
        NumberExpression<Integer> five = Expressions.numberTemplate(Integer.class, "5");

        assertEquals("abcd  ",           firstResult(StringExpressions.ltrim(str)));
        assertEquals(Integer.valueOf(3), firstResult(str.locate("a")));
        assertEquals(Integer.valueOf(0), firstResult(str.locate("a", four)));
        assertEquals(Integer.valueOf(4), firstResult(str.locate("b", two)));
        assertEquals("  abcd",           firstResult(StringExpressions.rtrim(str)));
        assertEquals("abc",              firstResult(str.substring(two, five)));
    }

    @Test
    @ExcludeIn({POSTGRESQL, SQLITE})
    public void string_indexOf() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals(Integer.valueOf(2),  firstResult(str.indexOf("a")));
        assertEquals(Integer.valueOf(-1), firstResult(str.indexOf("a", 4)));
        assertEquals(Integer.valueOf(3), firstResult(str.indexOf("b", 2)));
    }

    @Test
    public void stringFunctions2() throws SQLException {
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
    public void string_left() {
        assertEquals("John", query().from(employee).where(employee.lastname.eq("Johnson"))
                                    .select(SQLExpressions.left(employee.lastname, 4)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void string_right() {
        assertEquals("son", query().from(employee).where(employee.lastname.eq("Johnson"))
                                   .select(SQLExpressions.right(employee.lastname, 3)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void string_left_Right() {
        assertEquals("hn", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .select(SQLExpressions.right(SQLExpressions.left(employee.lastname, 4), 2)).fetchFirst());
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void string_right_Left() {
        assertEquals("ns", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .select(SQLExpressions.left(SQLExpressions.right(employee.lastname, 4), 2)).fetchFirst());
    }

    @Test
    @ExcludeIn({DB2, DERBY, FIREBIRD})
    public void substring() {
        //SELECT * FROM account where SUBSTRING(name, -x, 1) = SUBSTRING(name, -y, 1)
        query().from(employee)
                .where(employee.firstname.substring(-3, 1).eq(employee.firstname.substring(-2, 1)))
                .select(employee.id).fetch();
    }

    @Test
    public void syntax_for_employee() throws SQLException {
        assertEquals(3, query().from(employee).groupBy(employee.superiorId)
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch().size());

        assertEquals(2, query().from(employee).groupBy(employee.superiorId)
            .having(employee.id.max().gt(5))
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch().size());

        assertEquals(2, query().from(employee).groupBy(employee.superiorId)
            .having(employee.superiorId.isNotNull())
            .orderBy(employee.superiorId.asc())
            .select(employee.salary.avg(), employee.id.max()).fetch().size());
    }

    @Test
    public void templateExpression() {
        NumberExpression<Integer> one = Expressions.numberTemplate(Integer.class, "1");
        assertEquals(Collections.singletonList(1), query().from(survey).select(one.as("col1")).fetch());
    }

    @Test
    public void transform_groupBy() {
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
    public void tuple_projection() {
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
    public void tuple2() {
        assertEquals(10, query().from(employee)
                .select(Expressions.as(ConstantImpl.create("1"), "code"),
                        employee.id).fetch().size());
    }

    @Test
    public void twoColumns() {
        // two columns
        for (Tuple row : query().from(survey).select(survey.id, survey.name).fetch()) {
            assertEquals(2, row.size());
            assertEquals(Integer.class, row.get(0, Object.class).getClass());
            assertEquals(String.class, row.get(1, Object.class).getClass());
        }
    }

    @Test
    public void twoColumns_and_projection() {
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
    public void unique_Constructor_projection() {
        IdName idAndName = query().from(survey).limit(1).select(new QIdName(survey.id, survey.name)).fetchFirst();
        assertNotNull(idAndName);
        assertNotNull(idAndName.getId());
        assertNotNull(idAndName.getName());
    }

    @Test
    public void unique_single() {
        String s = query().from(survey).limit(1).select(survey.name).fetchFirst();
        assertNotNull(s);
    }

    @Test
    public void unique_wildcard() {
        // unique wildcard
        Tuple row = query().from(survey).limit(1).select(survey.all()).fetchFirst();
        assertNotNull(row);
        assertEquals(3, row.size());
        assertNotNull(row.get(0, Object.class));
        assertNotNull(row.get(0, Object.class) + " is not null", row.get(1, Object.class));
    }

    @Test(expected = NonUniqueResultException.class)
    public void uniqueResultContract() {
        query().from(employee).select(employee.all()).fetchOne();
    }

    @Test
    public void various() throws SQLException {
        for (String s : query().from(survey).select(survey.name.lower()).fetch()) {
            assertEquals(s, s.toLowerCase());
        }

        for (String s : query().from(survey).select(survey.name.append("abc")).fetch()) {
            assertTrue(s.endsWith("abc"));
        }

        System.out.println(query().from(survey).select(survey.id.sqrt()).fetch());
    }

    @Test
    public void where_exists() throws SQLException {
        SQLQuery<Integer> sq1 = query().from(employee).select(employee.id.max());
        assertEquals(10, query().from(employee).where(sq1.exists()).fetchCount());
    }

    @Test
    public void where_exists_Not() throws SQLException {
        SQLQuery<Integer> sq1 = query().from(employee).select(employee.id.max());
        assertEquals(0, query().from(employee).where(sq1.exists().not()).fetchCount());
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void with() {
        assertEquals(10, query().with(employee2, query().from(employee)
                .where(employee.firstname.eq("Jim"))
                .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void with2() {
        QEmployee employee3 = new QEmployee("e3");
        assertEquals(100, query().with(employee2, query().from(employee)
                .where(employee.firstname.eq("Jim"))
                .select(Wildcard.all))
               .with(employee2, query().from(employee)
                       .where(employee.firstname.eq("Jim"))
                       .select(Wildcard.all))
               .from(employee, employee2, employee3)
               .select(employee.id, employee2.id, employee3.id).fetch().size());
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void with3() {
        assertEquals(10, query().with(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Jim"))
                        .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void with_limit() {
        assertEquals(5, query().with(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Jim"))
                        .select(Wildcard.all))
                .from(employee, employee2)
                .limit(5)
                .orderBy(employee.id.asc(), employee2.id.asc())
                .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRESQL})
    public void with_limitOffset() {
        assertEquals(5, query().with(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Jim"))
                        .select(Wildcard.all))
                .from(employee, employee2)
                .limit(10)
                .offset(5)
                .orderBy(employee.id.asc(), employee2.id.asc())
                .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    @IncludeIn({ORACLE, POSTGRESQL})
    public void with_recursive() {
        assertEquals(10, query().withRecursive(employee2, query().from(employee)
                .where(employee.firstname.eq("Jim"))
                .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch().size());
    }


    @Test
    @IncludeIn({ORACLE, POSTGRESQL})
    public void with_recursive2() {
        assertEquals(10, query().withRecursive(employee2, employee2.all()).as(
                query().from(employee)
                        .where(employee.firstname.eq("Jim"))
                        .select(Wildcard.all))
               .from(employee, employee2)
               .select(employee.id, employee2.id).fetch().size());
    }

    @Test
    public void wildcard() {
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
    public void wildcard_all() {
        expectedQuery = "select * from EMPLOYEE e";
        query().from(employee).select(Wildcard.all).fetch();
    }

    @Test
    public void wildcard_all2() {
        assertEquals(10, query().from(new RelationalPathBase<Object>(Object.class, "employee", "public", "EMPLOYEE"))
                .select(Wildcard.all).fetch().size());
    }

    @Test
    public void wildcard_and_qTuple() {
        // wildcard and QTuple
        for (Tuple tuple : query().from(survey).select(survey.all()).fetch()) {
            assertNotNull(tuple.get(survey.id));
            assertNotNull(tuple.get(survey.name));
        }
    }

    @Test
    @IncludeIn(ORACLE)
    public void withinGroup() {
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
            query().from(survey).select(wg.withinGroup().orderBy(survey.id.asc(), survey.id.asc())).fetch();
        }

        // one arg
        exprs.clear();
        add(exprs, SQLExpressions.percentileCont(0.1));
        add(exprs, SQLExpressions.percentileDisc(0.9));

        for (WithinGroup<?> wg : exprs) {
            query().from(survey).select(wg.withinGroup().orderBy(survey.id)).fetch();
            query().from(survey).select(wg.withinGroup().orderBy(survey.id.asc())).fetch();
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY, H2})
    public void yearWeek() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200006), query.select(employee.datefield.yearWeek()).fetchFirst());
    }

    @Test
    @IncludeIn({H2})
    public void yearWeek_h2() {
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200007), query.select(employee.datefield.yearWeek()).fetchFirst());
    }

    @Test
    public void statementOptions() {
        StatementOptions options = StatementOptions.builder().setFetchSize(15).setMaxRows(150).build();
        SQLQuery<?> query = query().from(employee).orderBy(employee.id.asc());
        query.setStatementOptions(options);
        query.addListener(new SQLBaseListener() {
            public void preExecute(SQLListenerContext context) {
                try {
                    assertEquals(15, context.getPreparedStatement().getFetchSize());
                    assertEquals(150, context.getPreparedStatement().getMaxRows());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        query.select(employee.id).fetch();
    }

    @Test
    public void getResults() throws SQLException, InterruptedException {
        final AtomicLong endCalled = new AtomicLong(0);
        SQLQuery<Integer> query = query().select(employee.id).from(employee);
        query.addListener(new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                endCalled.set(System.currentTimeMillis());
            }
        });
        ResultSet results = query.getResults(employee.id);
        long getResultsCalled = System.currentTimeMillis();
        Thread.sleep(100);
        results.close();
        assertTrue(endCalled.get() - getResultsCalled >= 100);
    }

    @Test
    @ExcludeIn({DB2, DERBY, ORACLE, SQLSERVER})
    public void groupConcat() {
        List<String> expected = Arrays.asList("Mike,Mary", "Joe,Peter,Steve,Jim", "Jennifer,Helen,Daisy,Barbara");
        if (Connections.getTarget() == POSTGRESQL) {
            expected = Arrays.asList("Steve,Jim,Joe,Peter", "Barbara,Helen,Daisy,Jennifer", "Mary,Mike");
        }
        assertEquals(
                expected,
                query().select(SQLExpressions.groupConcat(employee.firstname))
                        .from(employee)
                        .groupBy(employee.superiorId).fetch());
    }

    @Test
    @ExcludeIn({DB2, DERBY, ORACLE, SQLSERVER})
    public void groupConcat2() {
        List<String> expected = Arrays.asList("Mike-Mary", "Joe-Peter-Steve-Jim", "Jennifer-Helen-Daisy-Barbara");
        if (Connections.getTarget() == POSTGRESQL) {
            expected = Arrays.asList("Steve-Jim-Joe-Peter", "Barbara-Helen-Daisy-Jennifer", "Mary-Mike");
        }
        assertEquals(
                expected,
                query().select(SQLExpressions.groupConcat(employee.firstname, "-"))
                        .from(employee)
                        .groupBy(employee.superiorId).fetch());
    }

}
//CHECKSTYLERULE:ON: FileLength
