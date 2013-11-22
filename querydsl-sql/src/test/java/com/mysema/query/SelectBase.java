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
package com.mysema.query;

import static com.mysema.query.Constants.date;
import static com.mysema.query.Constants.employee;
import static com.mysema.query.Constants.employee2;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static com.mysema.query.Constants.time;
import static com.mysema.query.Target.CUBRID;
import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.H2;
import static com.mysema.query.Target.HSQLDB;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.ORACLE;
import static com.mysema.query.Target.POSTGRES;
import static com.mysema.query.Target.SQLITE;
import static com.mysema.query.Target.SQLSERVER;
import static com.mysema.query.Target.TERADATA;
import static com.mysema.query.sql.mssql.SQLServerGrammar.rn;
import static com.mysema.query.sql.oracle.OracleGrammar.level;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.group.Group;
import com.mysema.query.group.GroupBy;
import com.mysema.query.sql.Beans;
import com.mysema.query.sql.DatePart;
import com.mysema.query.sql.QBeans;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLExpressions;
import com.mysema.query.sql.WindowOver;
import com.mysema.query.sql.domain.Employee;
import com.mysema.query.sql.domain.IdName;
import com.mysema.query.sql.domain.QEmployee;
import com.mysema.query.sql.domain.QIdName;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Concatenation;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.QBean;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.Coalesce;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.MathExpressions;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.StringExpressions;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;
import com.mysema.testutil.ExcludeIn;
import com.mysema.testutil.IncludeIn;

public class SelectBase extends AbstractBaseTest{

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

    @SuppressWarnings("unchecked")
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
    @ExcludeIn({ORACLE, CUBRID, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_All() {
        assertTrue(query().from(employee).uniqueResult(SQLExpressions.all(employee.firstname.isNotNull())));
    }

    @Test
    @ExcludeIn({ORACLE, CUBRID, DERBY, SQLSERVER, SQLITE, TERADATA})
    public void Boolean_Any() {
        assertTrue(query().from(employee).uniqueResult(SQLExpressions.any(employee.firstname.isNotNull())));
    }

    @Test
    @ExcludeIn({DERBY,MYSQL})
    public void Casts() throws SQLException {
        NumberExpression<?> num = employee.id;
        Expression<?>[] expr = new Expression[] {
                num.byteValue(),
                num.doubleValue(),
                num.floatValue(),
                num.intValue(),
                num.longValue(),
                num.shortValue(),
                num.stringValue() };

        for (Expression<?> e : expr) {
            for (Object o : query().from(employee).list(e)) {
                assertEquals(e.getType(), o.getClass());
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
    public void Complex_boolean() {
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
    public void ComplexSubQuery() {
        // alias for the salary
        NumberPath<BigDecimal> sal = new NumberPath<BigDecimal>(BigDecimal.class, "sal");
        // alias for the subquery
        PathBuilder<Object[]> sq = new PathBuilder<Object[]>(Object[].class, "sq");
        // query execution
        query().from(
                sq().from(employee)
                .list(employee.salary.add(employee.salary).add(employee.salary).as(sal)).as(sq)
        ).list(sq.get(sal).avg(), sq.get(sal).min(), sq.get(sal).max());
    }

    @Test
    @Ignore
    public void ConnectBy() throws SQLException {
        // TODO : come up with a legal case
        oracleQuery().from(employee)
            .where(level.eq(-1))
            .connectBy(level.lt(1000))
            .list(employee.id);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior() throws SQLException {
        expectedQuery =  "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                        "from EMPLOYEE e " +
                        "connect by prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior2() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior3() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "start with e.ID = ? " +
                "connect by prior e.ID = e.SUPERIOR_ID " +
                "order siblings by e.LASTNAME";
        oracleQuery().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .orderSiblingsBy(employee.lastname)
            .list(employee.id, employee.lastname, employee.superiorId);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void ConnectByPrior4() throws SQLException {
        if (configuration.getUseLiterals()) return;

        expectedQuery =
                "select e.ID, e.LASTNAME, e.SUPERIOR_ID " +
                "from EMPLOYEE e " +
                "connect by nocycle prior e.ID = e.SUPERIOR_ID";
        oracleQuery().from(employee)
            .connectByNocyclePrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
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
    @ExcludeIn({CUBRID, SQLITE})
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
    @ExcludeIn({CUBRID, SQLITE, HSQLDB, MYSQL, TERADATA})
    public void Date_Diff() {
        QEmployee employee2 = new QEmployee("employee2");
        TestQuery query = query().from(employee, employee2);

        Date date = new Date(0);
        for (DatePart dp : DatePart.values()) {
            if (dp != DatePart.millisecond) {
                query.singleResult(SQLExpressions.datediff(dp, employee.datefield, employee2.datefield));
                query.singleResult(SQLExpressions.datediff(dp, employee.datefield, date));
                query.singleResult(SQLExpressions.datediff(dp, date, employee.datefield));
            }
        }
    }

    @Test
    @IncludeIn(HSQLDB)
    public void Date_Diff_HSQLDB() {
        QEmployee employee2 = new QEmployee("employee2");
        TestQuery query = query().from(employee, employee2);

        for (DatePart dp : new DatePart[]{DatePart.year, DatePart.month, DatePart.day}) {
            if (dp != DatePart.millisecond) {
                query.singleResult(
                    SQLExpressions.datediff(dp, employee.datefield, employee2.datefield));
            }
        }
    }

    @Test
    @IncludeIn(MYSQL)
    public void Date_Diff_MySQL() {
        QEmployee employee2 = new QEmployee("employee2");
        TestQuery query = query().from(employee, employee2);

        query.singleResult(
                SQLExpressions.datediff(DatePart.day, employee.datefield, employee2.datefield));

    }

    @Test
    @IncludeIn({DERBY, H2, POSTGRES})
    public void Date_Diff2() {
        TestQuery query = query().from(employee).limit(1);

        Date date = new Date(0);
        int years = query.singleResult(SQLExpressions.datediff(DatePart.year, date, employee.datefield));
        int months = query.singleResult(SQLExpressions.datediff(DatePart.month, date, employee.datefield));
        // weeks
        int days = query.singleResult(SQLExpressions.datediff(DatePart.day, date, employee.datefield));
        int hours = query.singleResult(SQLExpressions.datediff(DatePart.hour, date, employee.datefield));
        int minutes = query.singleResult(SQLExpressions.datediff(DatePart.minute, date, employee.datefield));
        int seconds = query.singleResult(SQLExpressions.datediff(DatePart.second, date, employee.datefield));

        assertEquals(30,       years);
        assertEquals(361,      months);
        assertEquals(10989,    days);
        assertEquals(263736,   hours);
        assertEquals(15824160, minutes);
        assertEquals(949449600, seconds);
    }

    @Test
    @IncludeIn(ORACLE)
    public void Date_Diff2_Oracle() {
        TestQuery query = query().from(employee).limit(1);

        Date date = new Date(0);
        int years = query.singleResult(SQLExpressions.datediff(DatePart.year, date, employee.datefield));
        int months = query.singleResult(SQLExpressions.datediff(DatePart.month, date, employee.datefield));
        // weeeks
        int days = query.singleResult(SQLExpressions.datediff(DatePart.day, date, employee.datefield));
        int hours = query.singleResult(SQLExpressions.datediff(DatePart.hour, date, employee.datefield));
        int minutes = query.singleResult(SQLExpressions.datediff(DatePart.minute, date, employee.datefield));
        int seconds = query.singleResult(SQLExpressions.datediff(DatePart.second, date, employee.datefield));

        assertEquals(30,       years);
        assertEquals(366,      months);
        assertEquals(10989,    days);
        assertEquals(263736,   hours);
        assertEquals(15824160, minutes);
        assertEquals(949449600, seconds);
    }

    @Test
    @ExcludeIn({CUBRID, DERBY, H2, HSQLDB, MYSQL, SQLSERVER, SQLITE, TERADATA})
    public void Date_Trunc() {
        DateTimeExpression<java.util.Date> expr = DateTimeExpression.currentTimestamp();

        for (DatePart dp : DatePart.values()) {
            if (dp != DatePart.millisecond) {
                query().singleResult(SQLExpressions.datetrunc(dp, expr));
            }
        }
    }

    @Test
    public void DateTime() {
        TestQuery query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(2),      query.singleResult(employee.datefield.dayOfMonth()));
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
    @IncludeIn(MYSQL)
    public void Extensions() {
        mysqlQuery().from(survey).bigResult().list(survey.id);
        mysqlQuery().from(survey).bufferResult().list(survey.id);
        mysqlQuery().from(survey).cache().list(survey.id);
        mysqlQuery().from(survey).calcFoundRows().list(survey.id);
        mysqlQuery().from(survey).noCache().list(survey.id);

        mysqlQuery().from(survey).highPriority().list(survey.id);
        mysqlQuery().from(survey).lockInShareMode().list(survey.id);
        mysqlQuery().from(survey).smallResult().list(survey.id);
        mysqlQuery().from(survey).straightJoin().list(survey.id);
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
    @ExcludeIn(DERBY)
    public void Like_Number() {
        assertEquals(5, query().from(employee)
                .where(employee.id.like("1%")).count());
    }

    @Test
    public void Limit() throws SQLException {
        // limit
        query().from(employee)
        .orderBy(employee.firstname.asc())
        .limit(4).list(employee.id);
    }

    @Test
    public void Limit_and_Offset() throws SQLException {
        // limit and offset
        query().from(employee)
        .orderBy(employee.firstname.asc())
        .limit(4).offset(3).list(employee.id);
    }

    @Test
    public void Limit_and_Offset_and_Order() {
        // limit + offset
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
    @ExcludeIn({ORACLE, DERBY, SQLSERVER, CUBRID, TERADATA})
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
        // limit
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

    private double log(double x, int y) {
        return Math.log(x) / Math.log(y);
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, DERBY})
    public void LPad() {
        assertEquals("  ab", unique(StringExpressions.lpad(ConstantImpl.create("ab"), 4)));
        assertEquals("!!ab", unique(StringExpressions.lpad(ConstantImpl.create("ab"), 4, '!')));
    }

    @Test
    @IncludeIn(SQLSERVER)
    public void Manual_Paging() {
        Expression<Long> rowNumber = SQLExpressions.rowNumber().over().orderBy(employee.lastname.asc()).as(rn);
        // TODO : create a short cut for wild card
        Expression<Object[]> all = SimpleTemplate.create(Object[].class, "*");

        // simple
        System.out.println("#1");
        for (Tuple row : query().from(employee).list(employee.firstname, employee.lastname, rowNumber)) {
            System.out.println(row);
        }
        System.out.println();

        // with subquery, generic alias
        System.out.println("#2");
        ListSubQuery<Tuple> sub = sq().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        SimplePath<Tuple> subAlias = new SimplePath<Tuple>(Tuple.class, "s");
        for (Object[] row : query().from(sub.as(subAlias)).list(all)) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, only row number
        System.out.println("#3");
        SimpleSubQuery<Long> sub2 = sq().from(employee).unique(rowNumber);
        SimplePath<Long> subAlias2 = new SimplePath<Long>(Long.class, "s");
        for (Object[] row : query().from(sub2.as(subAlias2)).list(all)) {
            System.out.println(Arrays.asList(row));
        }
        System.out.println();

        // with subquery, specific alias
        System.out.println("#4");
        ListSubQuery<Tuple> sub3 = sq().from(employee).list(employee.firstname, employee.lastname, rowNumber);
        for (Tuple row : query().from(sub3.as(employee2)).list(employee2.firstname, employee2.lastname)) {
            System.out.println(Arrays.asList(row));
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
    @ExcludeIn(SQLSERVER) // FIXME
    public void Math() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "0.5");

        assertEquals(Math.acos(0.5), unique(MathExpressions.acos(expr)), 0.001);
        assertEquals(Math.asin(0.5), unique(MathExpressions.asin(expr)), 0.001);
        assertEquals(Math.atan(0.5), unique(MathExpressions.atan(expr)), 0.001);
        assertEquals(Math.cos(0.5),  unique(MathExpressions.cos(expr)), 0.001);
        assertEquals(Math.cosh(0.5), unique(MathExpressions.cosh(expr)), 0.001);
        assertEquals(cot(0.5),       unique(MathExpressions.cot(expr)), 0.001);
        assertEquals(coth(0.5),      unique(MathExpressions.coth(expr)), 0.001);
        assertEquals(degrees(0.5),   unique(MathExpressions.degrees(expr)), 0.001);
        assertEquals(Math.exp(0.5),  unique(MathExpressions.exp(expr)), 0.001);
        assertEquals(Math.log(0.5),  unique(MathExpressions.ln(expr)), 0.001);
        assertEquals(log(0.5, 10),   unique(MathExpressions.log(expr, 10)), 0.001);
        assertEquals(0.25,           unique(MathExpressions.power(expr, 2)), 0.001);
        assertEquals(radians(0.5),   unique(MathExpressions.radians(expr)), 0.001);
        assertEquals(Integer.valueOf(1),
                                     unique(MathExpressions.sign(expr)));
        assertEquals(Math.sin(0.5),  unique(MathExpressions.sin(expr)), 0.001);
        assertEquals(Math.sinh(0.5), unique(MathExpressions.sinh(expr)), 0.001);
        assertEquals(Math.tan(0.5),  unique(MathExpressions.tan(expr)), 0.001);
        assertEquals(Math.tanh(0.5), unique(MathExpressions.tanh(expr)), 0.001);
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
    @ExcludeIn({HSQLDB, H2, MYSQL, SQLITE})
    public void Offset_Only() {
        // offset
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
    @ExcludeIn({DERBY, HSQLDB, ORACLE, SQLSERVER})
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

//    @Test
//    public void Date_Add() {
//        Date date = new Date();
//        Expression<Date> expr = new ConstantImpl<Date>(date);
//
//        Date date3 = unique(DateExpressions.dateadd(expr, 1, DatePart.day));
//        assertTrue(date3.after(date));
//    }
//
//    @Test
//    public void Date_Diff() {
//        long ts = System.currentTimeMillis();
//        Date date = new Date(ts);
//        Date date2 = new Date(ts + 60 * 60 * 1000);
//        Expression<Date> expr = new ConstantImpl<Date>(date);
//
//        assertEquals(1,       unique(DateExpressions.datediff(expr, date2, DatePart.hour)).intValue());
//        assertEquals(60,      unique(DateExpressions.datediff(expr, date2, DatePart.minute)).intValue());
//        assertEquals(3600,    unique(DateExpressions.datediff(expr, date2, DatePart.second)).intValue());
//        //assertEquals(3600000, unique(DateExpressions.datediff(expr, date2, DatePart.millisecond)).intValue());
//    }

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
    @ExcludeIn({ORACLE, POSTGRES, SQLITE, TERADATA})
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
    @ExcludeIn(SQLITE)
    public void Right_Join() throws SQLException {
        query().from(employee).rightJoin(employee2)
            .on(employee.superiorIdKey.on(employee2))
            .list(employee.id, employee2.id);
    }

    @Test
    @ExcludeIn({DERBY, SQLSERVER})
    public void Round() {
        Expression<Double> expr = Expressions.numberTemplate(Double.class, "1.32");

        assertEquals(Double.valueOf(1.0), unique(MathExpressions.round(expr)));
        assertEquals(Double.valueOf(1.3), unique(MathExpressions.round(expr, 1)));
    }

    @Test
    @ExcludeIn({SQLITE, SQLSERVER, DERBY})
    public void Rpad() {
        assertEquals("ab  ", unique(StringExpressions.rpad(ConstantImpl.create("ab"), 4)));
        assertEquals("ab!!", unique(StringExpressions.rpad(ConstantImpl.create("ab"), 4,'!')));
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
        standardTest.runDateTests(employee.datefield, employee2.datefield, date);

        // int
        standardTest.runNumericCasts(employee.id, employee2.id, 1);
        standardTest.runNumericTests(employee.id, employee2.id, 1);
        // BigDecimal
        standardTest.runNumericTests(employee.salary, employee2.salary, new BigDecimal("30000.00"));

        standardTest.runStringTests(employee.firstname, employee2.firstname, "Jennifer");
        standardTest.runTimeTests(employee.timefield, employee2.timefield, time);

        standardTest.report();
    }

    @Test
    @ExcludeIn(SQLITE)
    public void String() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals("abcd  ", unique(StringExpressions.ltrim(str)));
        assertEquals(Integer.valueOf(3), unique(str.locate("a")));
        assertEquals(Integer.valueOf(0), unique(str.locate("a", 4)));
        assertEquals(Integer.valueOf(4), unique(str.locate("b", 2)));
        assertEquals("  abcd", unique(StringExpressions.rtrim(str)));
    }

    @Test
    @ExcludeIn({POSTGRES, SQLITE})
    public void String_IndexOf() {
        StringExpression str = Expressions.stringTemplate("'  abcd  '");

        assertEquals(Integer.valueOf(2), unique(str.indexOf("a")));
        assertEquals(Integer.valueOf(-1), unique(str.indexOf("a", 4)));
        assertEquals(Integer.valueOf(3), unique(str.indexOf("b", 2)));
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
    @ExcludeIn(DERBY)
    public void Substring() {
        //SELECT * FROM account where SUBSTRING(name, -x, 1) = SUBSTRING(name, -y, 1)
        query().from(employee)
               .where(employee.firstname.substring(-3, 1).eq(employee.firstname.substring(-2, 1)))
               .list(employee.id);
    }

    @Test
    @IncludeIn(ORACLE)
    @SkipForQuoted
    public void SumOver() throws SQLException{
//        SQL> select deptno,
//        2  ename,
//        3  sal,
//        4  sum(sal) over (partition by deptno
//        5  order by sal,ename) CumDeptTot,
//        6  sum(sal) over (partition by deptno) SalByDept,
//        7  sum(sal) over (order by deptno, sal) CumTot,
//        8  sum(sal) over () TotSal
//        9  from emp
//       10  order by deptno, sal;
        expectedQuery = "select e.LASTNAME, e.SALARY, " +
            "sum(e.SALARY) over (partition by e.SUPERIOR_ID order by e.LASTNAME, e.SALARY), " +
            "sum(e.SALARY) over (order by e.SUPERIOR_ID, e.SALARY), " +
            "sum(e.SALARY) over () from EMPLOYEE e order by e.SALARY asc, e.SUPERIOR_ID asc";

        oracleQuery().from(employee)
            .orderBy(employee.salary.asc(), employee.superiorId.asc())
            .list(
               employee.lastname,
               employee.salary,
               SQLExpressions.sum(employee.salary).over().partitionBy(employee.superiorId).orderBy(employee.lastname, employee.salary),
               SQLExpressions.sum(employee.salary).over().orderBy(employee.superiorId, employee.salary),
               SQLExpressions.sum(employee.salary).over());

        // shorter version
        QEmployee e = employee;
        oracleQuery().from(e)
            .orderBy(e.salary.asc(), e.superiorId.asc())
            .list(e.lastname, e.salary,
               SQLExpressions.sum(e.salary).over().partitionBy(e.superiorId).orderBy(e.lastname, e.salary),
               SQLExpressions.sum(e.salary).over().orderBy(e.superiorId, e.salary),
               SQLExpressions.sum(e.salary).over());
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
    @ExcludeIn(DERBY)
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

    private <T> T unique(Expression<T> expr) {
        return query().uniqueResult(expr);
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
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions() {
        List<WindowOver<?>> exprs = new ArrayList<WindowOver<?>>();
        NumberPath<Integer> path = survey.id;
        exprs.add(SQLExpressions.sum(path));
        exprs.add(SQLExpressions.count(path));
        exprs.add(SQLExpressions.avg(path));
        exprs.add(SQLExpressions.min(path));
        exprs.add(SQLExpressions.max(path));
        exprs.add(SQLExpressions.lead(path));
        exprs.add(SQLExpressions.lag(path));
        exprs.add(SQLExpressions.rank());
        exprs.add(SQLExpressions.denseRank());
        exprs.add(SQLExpressions.rowNumber());
        exprs.add(SQLExpressions.firstValue(path));
        exprs.add(SQLExpressions.lastValue(path));

        for (WindowOver<?> wo : exprs) {
            query().from(survey).list(wo.over().partitionBy(survey.name).orderBy(survey.id));
        }
    }

    @Test
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions_Over() {
        //SELECT Shipment_id,Ship_date, SUM(Qty) OVER () AS Total_Qty
        //FROM TestDB.Shipment
        query().from(employee).list(
                employee.id,
                SQLExpressions.sum(employee.salary).over());
    }

    @Test
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions_PartitionBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ) AS Total_Qty
        //FROM TestDB.Shipment
        query().from(employee).list(
                employee.id,
                employee.superiorId,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId));
    }

    @Test
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions_OrderBy() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS Total_Qty
        //FROM TestDB.Shipment
        query().from(employee).list(
                employee.id,
                SQLExpressions.sum(employee.salary).over()
                    .partitionBy(employee.superiorId).orderBy(employee.datefield));
    }

    @Test
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions_UnboundedRows() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //SUM(Qty) OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt
        //ROWS BETWEEN UNBOUNDED PRECEDING
        //AND CURRENT ROW) AS Total_Qty
        //FROM TestDB.Shipment

        // TODO
    }

    @Test
    @IncludeIn({POSTGRES, ORACLE, TERADATA})
    public void WindowFunctions_Qualify() {
        //SELECT Shipment_id,Ship_date,Ship_Type,
        //Rank() OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ) AS rnk
        //FROM TestDB.Shipment
        //QUALIFY  (Rank() OVER (PARTITION BY Ship_Type ORDER BY Ship_Dt ))  =1

        // TODO
    }

    @Test
    @ExcludeIn({DERBY, H2})
    public void YearWeek() {
        TestQuery query = query().from(employee).orderBy(employee.id.asc());
        assertEquals(Integer.valueOf(200005), query.singleResult(employee.datefield.yearWeek()));
    }

}
