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

import static com.querydsl.sql.Constants.*;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.*;

import java.io.*;
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
import com.querydsl.sql.domain.*;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.*;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.PathBuilder;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.query.NumberSubQuery;
import com.querydsl.core.types.template.NumberTemplate;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;

public class SelectBase extends AbstractBaseTest {

    private static final Expression<?>[] NO_EXPRESSIONS = new Expression[0];

    private final QueryExecution standardTest = new QueryExecution(Module.SQL, Connections.getTarget()) {
        @Override
        protected Pair<Projectable, Expression<?>[]> createQuery() {
            return Pair.of(
                    (Projectable)testQuery().from(employee, employee2),
                    NO_EXPRESSIONS);
        }
        @Override
        protected Pair<Projectable, Expression<?>[]> createQuery(Predicate filter) {
            return Pair.of(
                    (Projectable)testQuery().from(employee, employee2).where(filter),
                    new Expression<?>[]{employee.firstname});
        }
    };

    private <T> T singleResult(Expression<T> expr) {
        return query().singleResult(expr);
    }

    @Test
    public void Aggregate_List() {
        int min = 30000, avg = 65000, max = 160000;
        // list
        assertEquals(min, query().from(employee).list(employee.salary.min()).get(0).intValue());
        assertEquals(avg, query().from(employee).list(employee.salary.avg()).get(0).intValue());
        assertEquals(max, query().from(employee).list(employee.salary.max()).get(0).intValue());
    }

    @Test
    public void Aggregate_UniqueResult() {
        int min = 30000, avg = 65000, max = 160000;
        // uniqueResult
        assertEquals(min, query().from(employee).uniqueResult(employee.salary.min()).intValue());
        assertEquals(avg, query().from(employee).uniqueResult(employee.salary.avg()).intValue());
        assertEquals(max, query().from(employee).uniqueResult(employee.salary.max()).intValue());
    }

    @Test
    @ExcludeIn({MYSQL, ORACLE})
    @SkipForQuoted
    public void Alias_Quotes() {
        expectedQuery = "select e.FIRSTNAME as \"First Name\" from EMPLOYEE e";
        query().from(employee).list(employee.firstname.as("First Name"));
    }

    @Test
    @IncludeIn(MYSQL)
    @SkipForQuoted
    public void Alias_Quotes_MySQL() {
        expectedQuery = "select e.FIRSTNAME as `First Name` from EMPLOYEE e";
        query().from(employee).list(employee.firstname.as("First Name"));
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void Alias_Quotes_Oracle() {
        expectedQuery = "select e.FIRSTNAME \"First Name\" from EMPLOYEE e";
        query().from(employee).list(employee.firstname.as("First Name"));
    }

    @Test
    public void All() {
        for (Expression<?> expr : survey.all()) {
            Path<?> path = (Path<?>)expr;
            assertEquals(survey, path.getMetadata().getParent());
        }
    }

    @Test
    @IncludeIn(POSTGRES) // TODO generalize array literal projections
    public void Array() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "'{1,2,3}'::int[]");
        Integer[] result = query().singleResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0].intValue());
        assertEquals(2, result[1].intValue());
        assertEquals(3, result[2].intValue());
    }

    @Test
    @IncludeIn(POSTGRES) // TODO generalize array literal projections
    public void Array2() {
        Expression<int[]> expr = Expressions.template(int[].class, "'{1,2,3}'::int[]");
        int[] result = query().singleResult(expr);
        assertEquals(3, result.length);
        assertEquals(1, result[0]);
        assertEquals(2, result[1]);
        assertEquals(3, result[2]);
    }

    @Test
    @ExcludeIn({DERBY, HSQLDB})
    public void Array_Null() {
        Expression<Integer[]> expr = Expressions.template(Integer[].class, "null");
        assertNull(query().singleResult(expr));
    }

    @Test
    public void Array_Projection() {
        List<String[]> results = query().from(employee).list(
                new ArrayConstructorExpression<String>(String[].class, employee.firstname));
        assertFalse(results.isEmpty());
        for (String[] result : results) {
            assertNotNull(result[0]);
        }
    }

    @Test
    public void Beans() {
        QEmployee EMPLOYEE = new QEmployee("EMPLOYEE");
        List<Beans> rows = query().from(employee, EMPLOYEE).list(new QBeans(employee, EMPLOYEE));
        assertFalse(rows.isEmpty());
        for (Beans row : rows) {
            assertEquals(Employee.class, row.get(employee).getClass());
            assertEquals(Employee.class, row.get(EMPLOYEE).getClass());
        }
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_All() {
        assertTrue(query().from(employee).uniqueResult(SQLExpressions.all(employee.firstname.isNotNull())));
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, FIREBIRD, DB2, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_Any() {
        assertTrue(query().from(employee).uniqueResult(SQLExpressions.any(employee.firstname.isNotNull())));
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
            for (Object o : query().from(employee).list(expr)) {
                assertEquals(expr.getType(), o.getClass());
            }
        }
    }

    @Test
    public void Coalesce() {
        Coalesce<String> c = new Coalesce<String>(employee.firstname, employee.lastname).add("xxx");
        query().from(employee).where(c.getValue().eq("xxx")).list(employee.id);
    }

    @Test
    public void Compact_Join() {
        // verbose
        query().from(employee).innerJoin(employee2)
        .on(employee.superiorId.eq(employee2.id))
        .list(employee.id, employee2.id);

        // compact
        query().from(employee)
        .innerJoin(employee.superiorIdKey, employee2)
        .list(employee.id, employee2.id);

    }

    @Test
    public void Complex_Boolean() {
        BooleanExpression first = employee.firstname.eq("Mike").and(employee.lastname.eq("Smith"));
        BooleanExpression second = employee.firstname.eq("Joe").and(employee.lastname.eq("Divis"));
        assertEquals(2, query().from(employee).where(first.or(second)).count());

        assertEquals(0, query().from(employee).where(
                employee.firstname.eq("Mike"),
                employee.lastname.eq("Smith").or(employee.firstname.eq("Joe")),
                employee.lastname.eq("Divis")
        ).count());
    }

    @Test
    public void Complex_SubQuery() {
        // alias for the salary
        NumberPath<BigDecimal> sal = new NumberPath<BigDecimal>(BigDecimal.class, "sal");
        // alias for the subquery
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        // querydsl execution
        query().from(
                sq().from(employee)
                .list(employee.salary.add(employee.salary).add(employee.salary).as(sal)).as(sq)
        ).list(sq.get(sal).avg(), sq.get(sal).min(), sq.get(sal).max());
    }

    @Test
    public void Constructor() throws Exception {
        for (IdName idName : query().from(survey).list(new QIdName(survey.id, survey.name))) {
            System.out.println("id and name : " + idName.getId() + ","+ idName.getName());
        }
    }

    @Test
    public void Constructor_Projection() {
        // constructor projection
        for (IdName idAndName : query().from(survey).list(new QIdName(survey.id, survey.name))) {
            assertNotNull(idAndName);
            assertNotNull(idAndName.getId());
            assertNotNull(idAndName.getName());
        }
    }

    @Test
    public void Constructor_Projection2() {
        List<SimpleProjection> projections =query().from(employee).list(
                ConstructorExpression.create(SimpleProjection.class,
                        employee.firstname, employee.lastname));
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
        query().from(employee).count();
    }

    @Test
    public void Count_Without_PK() {
        query().from(QEmployeeNoPK.employee).count();
    }

    @Test
    public void Count2() {
        query().from(employee).singleResult(employee.count());
    }

    @Test
    @SkipForQuoted
    @ExcludeIn(ORACLE)
    public void Count_All() {
        expectedQuery = "select count(*) as rc from EMPLOYEE e";
        NumberPath<Long> rowCount = new NumberPath<Long>(Long.class, "rc");
        query().from(employee).uniqueResult(Wildcard.count.as(rowCount));
    }

    @Test
    @SkipForQuoted
    @IncludeIn(ORACLE)
    public void Count_All_Oracle() {
        expectedQuery = "select count(*) rc from EMPLOYEE e";
        NumberPath<Long> rowCount = new NumberPath<Long>(Long.class, "rc");
        query().from(employee).uniqueResult(Wildcard.count.as(rowCount));
    }

    @Test
    public void Count_Distinct_With_PK() {
        query().from(employee).distinct().count();
    }

    @Test
    public void Count_Distinct_Without_PK() {
        query().from(QEmployeeNoPK.employee).distinct().count();
    }

    @Test
    public void Count_Distinct2() {
        query().from(employee).singleResult(employee.countDistinct());
    }

    @Test
    public void Custom_Projection() {
        List<Projection> tuples = query().from(employee).list(
                new QProjection(employee.firstname, employee.lastname));
        assertFalse(tuples.isEmpty());
        for (Projection tuple : tuples) {
            assertNotNull(tuple.get(employee.firstname));
            assertNotNull(tuple.get(employee.lastname));
            assertNotNull(tuple.getExpr(employee.firstname));
            assertNotNull(tuple.getExpr(employee.lastname));
        }
    }

    @Test
    @IncludeIn({H2, SQLSERVER, MYSQL, ORACLE, TERADATA}) // TODO fix postgres
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
            Object dt2 = query().singleResult(new ConstantImpl(dt));
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
    @ExcludeIn({SQLITE})
    public void Date_Add() {
        TestQuery query = query().from(employee);
        Date date1 = query.singleResult(employee.datefield);
        Date date2 = query.singleResult(SQLExpressions.addYears(employee.datefield, 1));
        Date date3 = query.singleResult(SQLExpressions.addMonths(employee.datefield, 1));
        Date date4 = query.singleResult(SQLExpressions.addDays(employee.datefield, 1));

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
        add(exprs, SQLExpressions.addHours(dt, 1));
        add(exprs, SQLExpressions.addMinutes(dt, 1));
        add(exprs, SQLExpressions.addSeconds(dt, 1));

        for (Expression<?> expr : exprs) {
            assertNotNull(query().singleResult(expr));
        }
    }

    @Test
    @ExcludeIn({CUBRID, DB2, SQLITE, TERADATA})
    public void Date_Diff() {
        QEmployee employee2 = new QEmployee("employee2");
        TestQuery query = query().from(employee, employee2);

        List<DatePart> dps = Lists.newArrayList();
        add(dps, DatePart.year);
        add(dps, DatePart.month);
        add(dps, DatePart.week);
        add(dps, DatePart.day);
        add(dps, DatePart.hour, HSQLDB);
        add(dps, DatePart.minute, HSQLDB);
        add(dps, DatePart.second, HSQLDB);

        Date date = new Date(0);
        for (DatePart dp : dps) {
            query.singleResult(SQLExpressions.datediff(dp, employee.datefield, employee2.datefield));
            query.singleResult(SQLExpressions.datediff(dp, employee.datefield, date));
            query.singleResult(SQLExpressions.datediff(dp, date, employee.datefield));
        }
    }

    @Test
    @ExcludeIn({CUBRID, DB2, DERBY, HSQLDB, SQLITE, TERADATA})
    public void Date_Diff2() {
        TestQuery query = query().from(employee).orderBy(employee.id.asc());

        LocalDate localDate = new LocalDate(1970, 1, 10);
        Date date = new Date(localDate.toDateMidnight().getMillis());

        int years = query.singleResult(SQLExpressions.datediff(DatePart.year, date, employee.datefield));
        int months = query.singleResult(SQLExpressions.datediff(DatePart.month, date, employee.datefield));
        // weeks
        int days = query.singleResult(SQLExpressions.datediff(DatePart.day, date, employee.datefield));
        int hours = query.singleResult(SQLExpressions.datediff(DatePart.hour, date, employee.datefield));
        int minutes = query.singleResult(SQLExpressions.datediff(DatePart.minute, date, employee.datefield));
        int seconds = query.singleResult(SQLExpressions.datediff(DatePart.second, date, employee.datefield));

        assertEquals(949363200, seconds);
        assertEquals(15822720,  minutes);
        assertEquals(263712,    hours);
        assertEquals(10988,     days);
        assertEquals(361,       months);
        assertEquals(30,        years);
    }

    @Test
    @ExcludeIn({CUBRID, DB2, DERBY, FIREBIRD, H2, HSQLDB, MYSQL, SQLITE, SQLSERVER, TERADATA}) // FIXME
    public void Date_Trunc() {
        DateTimeExpression<java.util.Date> expr = DateTimeExpression.currentTimestamp();

        List<DatePart> dps = Lists.newArrayList();
        add(dps, DatePart.year);
        add(dps, DatePart.month);
        add(dps, DatePart.week);
        add(dps, DatePart.day);
        add(dps, DatePart.hour);
        add(dps, DatePart.minute);
        add(dps, DatePart.second);

        for (DatePart dp : dps) {
            query().singleResult(SQLExpressions.datetrunc(dp, expr));
        }
    }

    @Test
    public void DateTime() {
        TestQuery query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(10),      query.singleResult(employee.datefield.dayOfMonth()));
        assertEquals(Integer.valueOf(2),      query.singleResult(employee.datefield.month()));
        assertEquals(Integer.valueOf(2000),   query.singleResult(employee.datefield.year()));
        assertEquals(Integer.valueOf(200002), query.singleResult(employee.datefield.yearMonth()));
    }

    @Test
    @ExcludeIn(CUBRID)
    public void DateTime_To_Date() {
        query().singleResult(SQLExpressions.date(DateTimeExpression.currentTimestamp()));
    }

    private double degrees(double x) {
        return x * 180.0 / Math.PI;
    }

    @Test
    public void Distinct_Count() {
        long count1 = query().from(employee).distinct().count();
        long count2 = query().from(employee).distinct().count();
        assertEquals(count1, count2);
    }

    @Test
    public void Distinct_List() {
        List<Integer> lengths1 = query().from(employee).distinct().list(employee.firstname.length());
        List<Integer> lengths2 = query().from(employee).distinct().list(employee.firstname.length());
        assertEquals(lengths1, lengths2);
    }

    @Test
    public void Exists() {
        assertTrue(query().from(employee).where(employee.firstname.eq("Barbara")).exists());
    }

    @Test
    public void FactoryExpression_In_GroupBy() {
        Expression<Employee> empBean = Projections.bean(Employee.class, employee.id, employee.superiorId);
        assertFalse(query().from(employee).groupBy(empBean).list(empBean).isEmpty());
    }

    @Test
    @ExcludeIn({H2, SQLITE, DERBY, CUBRID, MYSQL})
    public void Full_Join() throws SQLException {
        query().from(employee).fullJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
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
        TestQuery qry = query()
            .from(employee)
            .innerJoin(employee._superiorIdKey, employee2);

        QTuple subordinates = new QTuple(employee2.id, employee2.firstname, employee2.lastname);

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
               .list(employee.id.count());
    }

    @Test
    @ExcludeIn({H2, DB2, DERBY, ORACLE, SQLSERVER})
    public void GroupBy_Validate() {
        NumberPath<BigDecimal> alias = new NumberPath<BigDecimal>(BigDecimal.class, "alias");
        query().from(employee)
               .groupBy(alias)
               .list(employee.salary.multiply(100).as(alias),
                     employee.salary.avg());
    }

    @Test
    @ExcludeIn({FIREBIRD})
    public void GroupBy_Count() {
        List<Integer> ids = query().from(employee).groupBy(employee.id).list(employee.id);
        long count = query().from(employee).groupBy(employee.id).count();
        SearchResults<Integer> results = query().from(employee).groupBy(employee.id)
                .limit(1).listResults(employee.id);

        assertEquals(10, ids.size());
        assertEquals(10, count);
        assertEquals(1, results.getResults().size());
        assertEquals(10, results.getTotal());
    }

    @Test
    @ExcludeIn({FIREBIRD, SQLSERVER})
    public void GroupBy_Distinct_Count() {
        List<Integer> ids = query().from(employee).groupBy(employee.id).distinct().list(NumberTemplate.ONE);
        SearchResults<Integer> results = query().from(employee).groupBy(employee.id)
                .limit(1).distinct().listResults(NumberTemplate.ONE);

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
                .listResults(employee.id, employee.firstname);
    }

    @SuppressWarnings("unchecked")
    @Test(expected=IllegalArgumentException.class)
    public void IllegalUnion() throws SQLException {
        SubQueryExpression<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        SubQueryExpression<Integer> sq2 = sq().from(employee).unique(employee.id.max());
        query().from(employee).union(sq1, sq2).list();
    }

    @Test
    public void In() {
        query().from(employee).where(employee.id.in(Arrays.asList(1,2))).list(employee);
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER})
    public void In_Long_List() {
        List<Integer> ids = Lists.newArrayList();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(
            query().from(employee).count(),
            query().from(employee).where(employee.id.in(ids)).count());
    }

    @Test
    @ExcludeIn({DERBY, FIREBIRD, SQLITE, SQLSERVER})
    public void NotIn_Long_List() {
        List<Integer> ids = Lists.newArrayList();
        for (int i = 0; i < 20000; i++) {
            ids.add(i);
        }
        assertEquals(0, query().from(employee).where(employee.id.notIn(ids)).count());
    }

    @Test
    public void In_Empty() {
        assertEquals(0, query().from(employee).where(employee.id.in(ImmutableList.<Integer>of())).count());
    }

    @Test
    public void NotIn_Empty() {
        long count = query().from(employee).count();
        assertEquals(count, query().from(employee).where(employee.id.notIn(ImmutableList.<Integer>of())).count());
    }

    @Test
    public void Inner_Join() throws SQLException {
        query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    public void Inner_Join_2Conditions() {
        query().from(employee).innerJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .on(employee2.firstname.isNotNull())
            .list(employee.id, employee2.id);
    }

    @Test
    public void Join() throws Exception {
        for (String name : query().from(survey, survey2)
                .where(survey.id.eq(survey2.id)).list(survey.name)) {
            System.out.println(name);
        }
    }

    @Test
    public void Joins() throws SQLException {
        for (Tuple row : query().from(employee).innerJoin(employee2)
                .on(employee.superiorId.eq(employee2.superiorId))
                .where(employee2.id.eq(10))
                .list(employee.id, employee2.id)) {
            System.out.println(row.get(employee.id) + ", " + row.get(employee2.id));
        }
    }

    @Test
    public void Left_Join() throws SQLException {
        query().from(employee).leftJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    public void Like() {
        query().from(employee).where(employee.firstname.like("\\")).count();
        query().from(employee).where(employee.firstname.like("\\\\")).count();
    }

    @Test
    @ExcludeIn(FIREBIRD)
    public void Like_Escape() {
        List<String> strs = ImmutableList.of("%a", "a%", "%a%", "_a", "a_", "_a_", "[C-P]arsen");

        for (String str : strs) {
            assertTrue(str, query()
                .from(employee)
                .where(Expressions.stringTemplate("'" + str + "'").contains(str)).count() > 0);
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY})
    public void Like_Number() {
        assertEquals(5, query().from(employee)
                .where(employee.id.like("1%")).count());
    }

    @Test
    public void Limit() throws SQLException {
        query().from(employee)
            .orderBy(employee.firstname.asc())
            .limit(4).list(employee.id);
    }

    @Test
    public void Limit_and_Offset() throws SQLException {
        assertEquals(Arrays.asList(20, 13, 10, 2),
            query().from(employee)
                   .orderBy(employee.firstname.asc())
                   .limit(4).offset(3)
                   .list(employee.id));
    }

    @Test
    public void Limit_and_Offset_and_Order() {
        List<String> names2 = Arrays.asList("Helen","Jennifer","Jim","Joe");
        assertEquals(names2, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4).offset(2)
                .list(employee.firstname));
    }

    @Test
    @IncludeIn(DERBY)
    public void Limit_and_Offset_In_Derby() throws SQLException {
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).list(employee.id);

        // limit
        expectedQuery = "select e.ID from EMPLOYEE e fetch first 4 rows only";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = "select e.ID from EMPLOYEE e offset 3 rows";
        query().from(employee).offset(3).list(employee.id);

    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void Limit_and_Offset_In_Oracle() throws SQLException {
        if (configuration.getUseLiterals()) return;

        // limit
        expectedQuery = "select * from (   select e.ID from EMPLOYEE e ) where rownum <= ?";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE e  ) a) where rn > ?";
        query().from(employee).offset(3).list(employee.id);

        // limit offset
        expectedQuery =  "select * from (  select a.*, rownum rn from (   select e.ID from EMPLOYEE e  ) a) where rn > 3 and rownum <= 4";
        query().from(employee).limit(4).offset(3).list(employee.id);
    }

    @Test
    @ExcludeIn({ORACLE, DB2, DERBY, FIREBIRD, SQLSERVER, CUBRID, TERADATA})
    @SkipForQuoted
    public void Limit_and_Offset2() throws SQLException {
        // limit
        expectedQuery = "select e.ID from EMPLOYEE e limit ?";
        query().from(employee).limit(4).list(employee.id);

        // limit offset
        expectedQuery = "select e.ID from EMPLOYEE e limit ? offset ?";
        query().from(employee).limit(4).offset(3).list(employee.id);

    }

    @Test
    public void Limit_and_Order() {
        List<String> names1 = Arrays.asList("Barbara","Daisy","Helen","Jennifer");
        assertEquals(names1, query().from(employee)
                .orderBy(employee.firstname.asc())
                .limit(4)
                .list(employee.firstname));
    }

    @Test
    public void ListResults() {
        SearchResults<Integer> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .listResults(employee.id);
        assertEquals(10, results.getTotal());
    }

    @Test
    public void ListResults2() {
        SearchResults<Integer> results = query().from(employee)
                .limit(2).offset(10).orderBy(employee.id.asc())
                .listResults(employee.id);
        assertEquals(10, results.getTotal());
    }

    @Test
    public void ListResults_FactoryExpression() {
        SearchResults<Employee> results = query().from(employee)
                .limit(10).offset(1).orderBy(employee.id.asc())
                .listResults(employee);
        assertEquals(10, results.getTotal());
    }

    @Test
    @ExcludeIn({DB2, DERBY, HSQLDB})
    public void Literals() {
        assertEquals(1, singleResult(ConstantImpl.create(1)).intValue());
        assertEquals(2l, singleResult(ConstantImpl.create(2l)).longValue());
        assertEquals(3.0, singleResult(ConstantImpl.create(3.0)).doubleValue(), 0.001);
        assertEquals(4.0f, singleResult(ConstantImpl.create(4.0f)).floatValue(), 0.001);
        assertEquals(true, singleResult(ConstantImpl.create(true)));
        assertEquals(false, singleResult(ConstantImpl.create(false)));
        assertEquals("abc", singleResult(ConstantImpl.create("abc")));
    }

    private double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void LPad() {
        assertEquals("  ab", singleResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4)));
        assertEquals("!!ab", singleResult(StringExpressions.lpad(ConstantImpl.create("ab"), 4, '!')));
    }


    @Test
    public void Map() {
        Map<Integer, String> idToName = query().from(employee).map(employee.id.as("id"), employee.firstname);
        for (Map.Entry<Integer, String> entry : idToName.entrySet()) {
            assertNotNull(entry.getKey());
            assertNotNull(entry.getValue());
        }
    }

    @Test
    @SuppressWarnings("serial")
    public void MappingProjection() {
        List<Pair<String, String>> pairs = query().from(employee)
                .list(new MappingProjection<Pair<String,String>>(Pair.class,
                      employee.firstname, employee.lastname) {
            @Override
            protected Pair<String, String> map(Tuple row) {
                return Pair.of(row.get(employee.firstname), row.get(employee.lastname));
            }
        });

        for (Pair<String, String> pair : pairs) {
            assertNotNull(pair.getFirst());
            assertNotNull(pair.getSecond());
        }
    }

    @Test
    public void Math() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "0.50");

        assertEquals(Math.acos(0.5), singleResult(MathExpressions.acos(expr)), 0.001);
        assertEquals(Math.asin(0.5), singleResult(MathExpressions.asin(expr)), 0.001);
        assertEquals(Math.atan(0.5), singleResult(MathExpressions.atan(expr)), 0.001);
        assertEquals(Math.cos(0.5),  singleResult(MathExpressions.cos(expr)), 0.001);
        assertEquals(Math.cosh(0.5), singleResult(MathExpressions.cosh(expr)), 0.001);
        assertEquals(cot(0.5),       singleResult(MathExpressions.cot(expr)), 0.001);
        assertEquals(coth(0.5),      singleResult(MathExpressions.coth(expr)), 0.001);
        assertEquals(degrees(0.5),   singleResult(MathExpressions.degrees(expr)), 0.001);
        assertEquals(Math.exp(0.5),  singleResult(MathExpressions.exp(expr)), 0.001);
        assertEquals(Math.log(0.5),  singleResult(MathExpressions.ln(expr)), 0.001);
        assertEquals(log(0.5, 10),   singleResult(MathExpressions.log(expr, 10)), 0.001);
        assertEquals(0.25,           singleResult(MathExpressions.power(expr, 2)), 0.001);
        assertEquals(radians(0.5),   singleResult(MathExpressions.radians(expr)), 0.001);
        assertEquals(Integer.valueOf(1),
                singleResult(MathExpressions.sign(expr)));
        assertEquals(Math.sin(0.5),  singleResult(MathExpressions.sin(expr)), 0.001);
        assertEquals(Math.sinh(0.5), singleResult(MathExpressions.sinh(expr)), 0.001);
        assertEquals(Math.tan(0.5),  singleResult(MathExpressions.tan(expr)), 0.001);
        assertEquals(Math.tanh(0.5), singleResult(MathExpressions.tanh(expr)), 0.001);
    }

    @Test
    public void Nested_Tuple_Projection() {
        Concatenation concat = new Concatenation(employee.firstname, employee.lastname);
        List<Tuple> tuples = query().from(employee)
                .list(new QTuple(employee.firstname, employee.lastname, concat));
        assertFalse(tuples.isEmpty());
        for (Tuple tuple : tuples) {
            String firstName = tuple.get(employee.firstname);
            String lastName = tuple.get(employee.lastname);
            assertEquals(firstName + lastName, tuple.get(concat));
        }
    }


    @Test
    public void No_From() {
        assertNotNull(query().singleResult(DateExpression.currentDate()));
    }

    @Test
    public void NotExists() {
        assertTrue(query().from(employee).where(employee.firstname.eq("Barb")).notExists());
    }

    @Test
    public void Nullif() {
        query().from(employee).list(employee.firstname.nullif(employee.lastname));
    }

    @Test
    public void Nullif_Constant() {
        query().from(employee).list(employee.firstname.nullif("xxx"));
    }

    @Test
    public void Num_Cast() {
        query().from(employee).list(employee.id.castToNum(Long.class));
        query().from(employee).list(employee.id.castToNum(Float.class));
        query().from(employee).list(employee.id.castToNum(Double.class));
    }

    @Test
    public void Num_Cast2() {
        NumberExpression<Integer> num = Expressions.numberTemplate(Integer.class, "0");
        query().uniqueResult(num.castToNum(Byte.class));
        query().uniqueResult(num.castToNum(Short.class));
        query().uniqueResult(num.castToNum(Integer.class));
        query().uniqueResult(num.castToNum(Long.class));
        query().uniqueResult(num.castToNum(Float.class));
        query().uniqueResult(num.castToNum(Double.class));
    }

    @Test
    @ExcludeIn({CUBRID, DERBY, FIREBIRD, POSTGRES})
    public void Number_As_Boolean() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).set(numberTest.col1Boolean, true).execute();
        insert(numberTest).set(numberTest.col1Number, (byte)1).execute();
        assertEquals(2, query().from(numberTest).list(numberTest.col1Boolean).size());
        assertEquals(2, query().from(numberTest).list(numberTest.col1Number).size());
    }

    @Test
    public void Number_As_Boolean_Null() {
        QNumberTest numberTest = QNumberTest.numberTest;
        delete(numberTest).execute();
        insert(numberTest).setNull(numberTest.col1Boolean).execute();
        insert(numberTest).setNull(numberTest.col1Number).execute();
        assertEquals(2, query().from(numberTest).list(numberTest.col1Boolean).size());
        assertEquals(2, query().from(numberTest).list(numberTest.col1Number).size());
    }

    @Test
    public void Offset_Only() {
        query().from(employee)
            .orderBy(employee.firstname.asc())
            .offset(3).list(employee.id);
    }

    @Test
    public void Operation_in_Constant_list() {
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a'))).count();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b'))).count();
        query().from(survey).where(survey.name.charAt(0).in(Arrays.asList('a','b','c'))).count();
    }

    @Test
    @ExcludeIn(CUBRID)
    public void Order_NullsFirst() {
        query().from(survey)
            .orderBy(survey.name.asc().nullsFirst())
            .list(survey.name);
    }

    @Test
    @ExcludeIn(CUBRID)
    public void Order_NullsLast() {
        query().from(survey)
            .orderBy(survey.name.asc().nullsLast())
            .list(survey.name);
    }

    @Test
    public void Params() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .uniqueResult(employee.firstname));
    }

    @Test
    public void Params_anon() {
        Param<String> name = new Param<String>(String.class);
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .set(name, "Mike")
                .uniqueResult(employee.firstname));
    }

    @Test(expected=ParamNotSetException.class)
    public void Params_not_set() {
        Param<String> name = new Param<String>(String.class,"name");
        assertEquals("Mike",query()
                .from(employee).where(employee.firstname.eq(name))
                .uniqueResult(employee.firstname));
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
            .list(employee.lastname, salarySum);
    }

    @Test
    public void Path_in_Constant_list() {
        query().from(survey).where(survey.name.in(Arrays.asList("a"))).count();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b"))).count();
        query().from(survey).where(survey.name.in(Arrays.asList("a","b","c"))).count();
    }

    @Test
    public void Precedence() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith")).or(fn.eq("Joe").and(ln.eq("Divis")));
        assertEquals(2l, query().from(employee).where(where).count());
    }

    @Test
    public void Precedence2() {
        StringPath fn = employee.firstname;
        StringPath ln = employee.lastname;
        Predicate where = fn.eq("Mike").and(ln.eq("Smith").or(fn.eq("Joe")).and(ln.eq("Divis")));
        assertEquals(0l, query().from(employee).where(where).count());
    }

    @Test
    public void Projection() throws IOException{
        CloseableIterator<Tuple> results = query().from(survey).iterate(survey.all());
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
                .list(new QIdName(survey.id, survey.name), survey.id, survey.name)) {
            assertEquals(3, row.size());
            assertEquals(IdName.class, row.get(0, Object.class).getClass());
            assertEquals(Integer.class, row.get(1, Object.class).getClass());
            assertEquals(String.class, row.get(2, Object.class).getClass());
        }
    }

    @Test
    public void Projection2() throws IOException{
        // TODO : add assertions
        CloseableIterator<Tuple> results = query().from(survey).iterate(survey.id, survey.name);
        assertTrue(results.hasNext());
        while (results.hasNext()) {
            assertEquals(2, results.next().size());
        }
        results.close();
    }

    @Test
    public void Projection3() throws IOException{
        CloseableIterator<String> names = query().from(survey).iterate(survey.name);
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
                sq().from(survey).list(survey.all()).as("sq"))
            .list(new QBean<Survey>(Survey.class, Collections.singletonMap("name", sq.get(survey.name))));
        assertFalse(surveys.isEmpty());

    }

    @Test
    public void Query_with_Constant() throws Exception {
        for (Tuple row : query().from(survey)
                .where(survey.id.eq(1))
                .list(survey.id, survey.name)) {
            System.out.println(row.get(survey.id) + ", " + row.get(survey.name));
        }
    }

    @Test
    public void Query1() throws Exception {
        for (String s : query().from(survey).list(survey.name)) {
            System.out.println(s);
        }
    }

    @Test
    public void Query2() throws Exception {
        for (Tuple row : query().from(survey).list(survey.id, survey.name)) {
            System.out.println(row.get(survey.id) + ", " + row.get(survey.name));
        }
    }

    private double radians(double x) {
        return x * Math.PI / 180.0;
    }

    @Test
    public void Random() {
        query().uniqueResult(MathExpressions.random());
    }

    @Test
    @ExcludeIn({FIREBIRD, ORACLE, POSTGRES, SQLITE, TERADATA})
    public void Random2() {
        query().uniqueResult(MathExpressions.random(10));
    }

    @Test
    public void RelationalPath_Projection() {
        List<Tuple> results = query().from(employee, employee2).where(employee.id.eq(employee2.id))
                .list(employee, employee2);
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
                .list(employee.id, employee2.id);
    }

    @Test
    public void RelationalPath_Ne() {
        query().from(employee, employee2)
                .where(employee.ne(employee2))
                .list(employee.id, employee2.id);
    }

    @Test
    public void RelationalPath_Eq2() {
        query().from(survey, survey2)
                .where(survey.eq(survey2))
                .list(survey.id, survey2.id);
    }

    @Test
    public void RelationalPath_Ne2() {
        query().from(survey, survey2)
                .where(survey.ne(survey2))
                .list(survey.id, survey2.id);
    }

    @Test
    @ExcludeIn(SQLITE)
    public void Right_Join() throws SQLException {
        query().from(employee).rightJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    @ExcludeIn(DERBY)
    public void Round() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "1.32");

        assertEquals(Double.valueOf(1.0), singleResult(MathExpressions.round(expr)));
        assertEquals(Double.valueOf(1.3), singleResult(MathExpressions.round(expr, 1)));
    }

    @Test
    @ExcludeIn({SQLITE, DERBY})
    public void Rpad() {
        assertEquals("ab  ", singleResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4)));
        assertEquals("ab!!", singleResult(StringExpressions.rpad(ConstantImpl.create("ab"), 4,'!')));
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.eq(0)));
    }

    @Test
    @Ignore
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void Select_BooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.gt(0)));
    }

    @Test
    public void Select_Concat() throws SQLException {
        System.out.println(query().from(survey).list(survey.name.append("Hello World")));
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, CUBRID, TERADATA})
    public void Select_For_Update() {
        query().from(survey).forUpdate().list(survey.id);
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, CUBRID, TERADATA})
    public void Select_For_Update_UniqueResult() {
        query().from(survey).forUpdate().uniqueResult(survey.id);
    }

    @Test
    @SkipForQuoted
    public void Serialization() {
        TestQuery query = query();
        query.from(survey);
        assertEquals("from SURVEY s", query.toString());
        query.from(survey2);
        assertEquals("from SURVEY s, SURVEY s2", query.toString());
    }

    @Test
    public void Serialization2() throws Exception {
        List<Tuple> rows = query().from(survey).list(survey.id, survey.name);
        serialize(rows);
    }

    private void serialize(Object obj) throws IOException, ClassNotFoundException{
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytesOut);
        out.writeObject(obj);
        out.close();
        bytesOut.close();

        ByteArrayInputStream bytesIn = new ByteArrayInputStream(bytesOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bytesIn);
        List<Tuple> rows = (List<Tuple>) in.readObject();
        for (Tuple row : rows) {
            row.hashCode();
        }
    }

    @Test
    public void Single() {
        assertNotNull(query().from(survey).singleResult(survey.name));
    }

    @Test
    public void Single_Array() {
        assertNotNull(query().from(survey).singleResult(new Expression<?>[]{survey.name}));
    }

    @Test
    public void Single_Column() {
        // single column
        for (String s : query().from(survey).list(survey.name)) {
            assertNotNull(s);
        }
    }

    @Test
    public void Single_Column_via_Object_type() {
        for (Object s : query().from(survey)
                .list(new PathImpl<Object>(Object.class, survey.name.getMetadata()))) {
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

        assertEquals("abcd  ",           singleResult(StringExpressions.ltrim(str)));
        assertEquals(Integer.valueOf(3), singleResult(str.locate("a")));
        assertEquals(Integer.valueOf(0), singleResult(str.locate("a", 4)));
        assertEquals(Integer.valueOf(4), singleResult(str.locate("b", 2)));
        assertEquals("  abcd",           singleResult(StringExpressions.rtrim(str)));
    }

    @Test
    @ExcludeIn({POSTGRES, SQLITE})
    public void String_IndexOf() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals(Integer.valueOf(2),  singleResult(str.indexOf("a")));
        assertEquals(Integer.valueOf(-1), singleResult(str.indexOf("a", 4)));
        assertEquals(Integer.valueOf(3),  singleResult(str.indexOf("b", 2)));
    }

    @Test
    public void StringFunctions2() throws SQLException {
        for (BooleanExpression where : Arrays.<BooleanExpression> asList(
                employee.firstname.startsWith("a"),
                employee.firstname.startsWithIgnoreCase("a"),
                employee.firstname.endsWith("a"),
                employee.firstname.endsWithIgnoreCase("a"))) {
            query().from(employee).where(where).list(employee.firstname);
        }
    }

    @Test
    @ExcludeIn(SQLITE)
    public void String_Left() {
        assertEquals("John", query().from(employee).where(employee.lastname.eq("Johnson"))
                                    .singleResult(SQLExpressions.left(employee.lastname, 4)));
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Right() {
        assertEquals("son", query().from(employee).where(employee.lastname.eq("Johnson"))
                                   .singleResult(SQLExpressions.right(employee.lastname, 3)));
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Left_Right() {
        assertEquals("hn", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .singleResult(SQLExpressions.right(SQLExpressions.left(employee.lastname, 4), 2)));
    }

    @Test
    @ExcludeIn({DERBY, SQLITE})
    public void String_Right_Left() {
        assertEquals("ns", query().from(employee).where(employee.lastname.eq("Johnson"))
                                  .singleResult(SQLExpressions.left(SQLExpressions.right(employee.lastname, 4), 2)));
    }

    @Test
    @ExcludeIn({DB2, DERBY, FIREBIRD})
    public void Substring() {
        //SELECT * FROM account where SUBSTRING(name, -x, 1) = SUBSTRING(name, -y, 1)
        query().from(employee)
               .where(employee.firstname.substring(-3, 1).eq(employee.firstname.substring(-2, 1)))
               .list(employee.id);
    }

    @Test
    public void Syntax_For_Employee() throws SQLException {
        query().from(employee).groupBy(employee.superiorId)
            .orderBy(employee.superiorId.asc())
            .list(employee.salary.avg(),employee.id.max());

        query().from(employee).groupBy(employee.superiorId)
            .having(employee.id.max().gt(5))
            .orderBy(employee.superiorId.asc())
            .list(employee.salary.avg(), employee.id.max());

        query().from(employee).groupBy(employee.superiorId)
            .having(employee.superiorId.isNotNull())
            .orderBy(employee.superiorId.asc())
            .list(employee.salary.avg(),employee.id.max());
    }

    @Test
    public void TemplateExpression() {
        NumberExpression<Integer> one = NumberTemplate.create(Integer.class, "1");
        query().from(survey).list(one.as("col1"));
    }

    @Test
    public void Transform_GroupBy() {
        QEmployee employee = new QEmployee("employee");
        QEmployee employee2 = new QEmployee("employee2");
        Map<Integer, Map<Integer, Employee>> results = query().from(employee, employee2)
            .transform(GroupBy.groupBy(employee.id).as(GroupBy.map(employee2.id, employee2)));

        int count = (int) query().from(employee).count();
        assertEquals(count, results.size());
        for (Map.Entry<Integer, Map<Integer, Employee>> entry : results.entrySet()) {
            Map<Integer, Employee> employees = entry.getValue();
            assertEquals(count, employees.size());
        }

    }

    @Test
    public void Tuple_Projection() {
        List<Tuple> tuples = query().from(employee)
                .list(new QTuple(employee.firstname, employee.lastname));
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
            .list(Expressions.as(ConstantImpl.create("1"),"code"),
                  employee.id);
    }

    @Test
    public void TwoColumns() {
        // two columns
        for (Tuple row : query().from(survey).list(survey.id, survey.name)) {
            assertEquals(2, row.size());
            assertEquals(Integer.class, row.get(0, Object.class).getClass());
            assertEquals(String.class, row.get(1, Object.class).getClass());
        }
    }

    @Test
    public void TwoColumns_and_Projection() {
        // two columns and projection
        for (Tuple row : query().from(survey)
                .list(survey.id, survey.name, new QIdName(survey.id, survey.name))) {
            assertEquals(3, row.size());
            assertEquals(Integer.class, row.get(0, Object.class).getClass());
            assertEquals(String.class, row.get(1, Object.class).getClass());
            assertEquals(IdName.class, row.get(2, Object.class).getClass());
        }
    }

    @Test
    public void Unique_Constructor_Projection() {
        IdName idAndName = query().from(survey).limit(1).uniqueResult(new QIdName(survey.id, survey.name));
        assertNotNull(idAndName);
        assertNotNull(idAndName.getId());
        assertNotNull(idAndName.getName());
    }

    @Test
    public void Unique_Single() {
        String s = query().from(survey).limit(1).uniqueResult(survey.name);
        assertNotNull(s);
    }

    @Test
    public void Unique_Wildcard() {
        // unique wildcard
        Tuple row = query().from(survey).limit(1).uniqueResult(survey.all());
        assertNotNull(row);
        assertEquals(3, row.size());
        assertNotNull(row.get(0, Object.class));
        assertNotNull(row.get(0, Object.class) +" is not null", row.get(1, Object.class));
    }

    @Test(expected=NonUniqueResultException.class)
    public void UniqueResultContract() {
        query().from(employee).uniqueResult(employee.all());
    }

    @Test
    public void Various() throws SQLException {
        for (String s : query().from(survey).list(survey.name.lower())) {
            assertEquals(s, s.toLowerCase());
        }

        for (String s : query().from(survey).list(survey.name.append("abc"))) {
            assertTrue(s.endsWith("abc"));
        }

        System.out.println(query().from(survey).list(survey.id.sqrt()));
    }

    @Test
    public void Where_Exists() throws SQLException {
        NumberSubQuery<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        query().from(employee).where(sq1.exists()).count();
    }

    @Test
    public void Where_Exists_Not() throws SQLException {
        NumberSubQuery<Integer> sq1 = sq().from(employee).unique(employee.id.max());
        query().from(employee).where(sq1.exists().not()).count();
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRES})
    public void With() {
        query().with(employee2, sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .from(employee, employee2)
               .list(employee.id, employee2.id);
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRES})
    public void With2() {
        QEmployee employee3 = new QEmployee("e3");
        query().with(employee2, sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .with(employee2, sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .from(employee, employee2, employee3)
               .list(employee.id, employee2.id, employee3.id);
    }

    @Test
    @IncludeIn({HSQLDB, ORACLE, POSTGRES})
    public void With3() {
        query().with(employee2, employee2.all()).as(
                sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .from(employee, employee2)
               .list(employee.id, employee2.id);
    }

    @Test
    @IncludeIn({ORACLE, POSTGRES})
    public void With_Recursive() {
        query().withRecursive(employee2, sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .from(employee, employee2)
               .list(employee.id, employee2.id);
    }


    @Test
    @IncludeIn({ORACLE, POSTGRES})
    public void With_Recursive2() {
        query().withRecursive(employee2, employee2.all()).as(
                sq().from(employee)
                  .where(employee.firstname.eq("Tom"))
                  .list(Wildcard.all))
               .from(employee, employee2)
               .list(employee.id, employee2.id);
    }

    @Test
    public void Wildcard() {
        // wildcard
        for (Tuple row : query().from(survey).list(survey.all())) {
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
        query().from(employee).list(Wildcard.all);
    }

    @Test
    public void Wildcard_All2() {
        query().from(new RelationalPathBase(Object.class, "employee", "public", "EMPLOYEE"))
               .list(Wildcard.all);
    }

    @Test
    public void Wildcard_and_QTuple() {
        // wildcard and QTuple
        for (Tuple tuple : query().from(survey).list(new QTuple(survey.all()))) {
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
            query().from(survey).list(wg.withinGroup().orderBy(survey.id, survey.id));
        }

        // one arg
        exprs.clear();
        add(exprs, SQLExpressions.percentileCont(0.1));
        add(exprs, SQLExpressions.percentileDisc(0.9));

        for (WithinGroup<?> wg : exprs) {
            query().from(survey).list(wg.withinGroup().orderBy(survey.id));
        }
    }

    @Test
    @ExcludeIn({DB2, DERBY, H2})
    public void YearWeek() {
        TestQuery query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200006), query.singleResult(employee.datefield.yearWeek()));
    }

}
