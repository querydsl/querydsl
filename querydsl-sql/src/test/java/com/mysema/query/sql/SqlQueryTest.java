/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static com.mysema.query.functions.AggregationFunctions.count;
import static com.mysema.query.sql.SQLGrammar.exists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import org.junit.AfterClass;
import org.junit.Test;

import com.mysema.query.ExcludeIn;
import com.mysema.query.IncludeIn;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.functions.StringFunctions;
import com.mysema.query.sql.domain.QEMPLOYEE;
import com.mysema.query.sql.domain.QSURVEY;
import com.mysema.query.sql.domain.QTEST;
import com.mysema.query.sql.dto.IdName;
import com.mysema.query.sql.dto.QIdName;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * SqlQueryTest provides SQL query tests for different DB types
 * 
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public abstract class SqlQueryTest {

    protected static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    protected static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();

    protected SQLPatterns dialect;

    protected final QEMPLOYEE employee = new QEMPLOYEE("employee");
    protected final QEMPLOYEE employee2 = new QEMPLOYEE("employee2");
    protected final QSURVEY survey = new QSURVEY("survey");
    protected final QSURVEY survey2 = new QSURVEY("survey2");
    protected final QTEST test = new QTEST("test");

    @Nullable
    protected String expectedQuery;

    @AfterClass
    public static void tearDown() throws Exception {
        if (stmtHolder.get() != null)
            stmtHolder.get().close();
        if (connHolder.get() != null)
            connHolder.get().close();
    }

    @Test
    public void testQuery1() throws Exception {
        for (String s : q().from(survey).list(survey.name)) {
            System.out.println(s);
        }
    }

    @Test
    public void testQuery2() throws Exception {
        for (Object[] row : q().from(survey).list(survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void testQueryWithConstant() throws Exception {
        for (Object[] row : q().from(survey).where(survey.id.eq(1)).list(
                survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void testJoin() throws Exception {
        for (String name : q().from(survey, survey2).where(
                survey.id.eq(survey2.id)).list(survey.name)) {
            System.out.println(name);
        }
    }

    @Test
    public void testConstructor() throws Exception {
        for (IdName idName : q().from(survey).list(
                new QIdName(survey.id, survey.name))) {
            System.out.println("id and name : " + idName.getId() + ","
                    + idName.getName());
        }
    }

    @Test
    public void testVarious() throws SQLException {
        System.out.println(q().from(survey).list(survey.name.lower()));
        System.out.println(q().from(survey).list(survey.name.add("abc")));
        System.out
                .println(q().from(survey).list(MathFunctions.sqrt(survey.id)));
    }

//    @Test
//    public void testColumnAlias() throws SQLException {
//        q().from(employee).list(employee.firstname.as("fn"),
//                employee.firstname.lower().as("fnLc"));
//    }

    @Test
    public void testSelectConcat() throws SQLException {
        System.out.println(q().from(survey)
                .list(survey.name.add("Hello World")));
    }

    @Test
    @ExcludeIn( { "oracle", "derby" })
    public void testSelectBooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(q().from(survey).list(survey.id.eq(0)));
    }

    @Test
    @ExcludeIn( { "oracle", "derby" })
    public void testSelectBooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(q().from(survey).list(survey.id.gt(0)));
    }

//    @Test
//    public void testSyntaxForTest() throws SQLException {
//        // TEST
//        // select count(*) from test where name = null
//        expectedQuery = "select count(*) from test test where test.name is null";
//        q().from(test).where(test.name.isNull()).count();
//        // select count(*) from test where name like null
//        // q().from(test).where(test.name.like(null)).count();
//        // select count(*) from test where name = ''
//        q().from(test).where(test.name.like("")).count();
//        // select count(*) from test where name is not null
//        q().from(test).where(test.name.isNotNull()).count();
//        // select count(*) from test where name like '%'
//        q().from(test).where(test.name.like("%")).count();
//        // select count(*) from test where left(name, 6) = 'name44'
//        q().from(test).where(test.name.substring(0, 6).like("name44%")).count();
//        // select count(*) from test where name like 'name44%'
//        q().from(test).where(test.name.like("name44%")).count();
//        // select count(*) from test where left(name,5) = 'name4' and
//        // right(name,1) = 5
//        // TODO
//        // select count(*) from test where name like 'name4%5'
//        q().from(test).where(test.name.like("name4%5")).count();
//        // select count(*) from test where left(name,5) = 'name4' and
//        // right(name,1) = 5
//        // TODO
//        // select count(*) from test where name like 'name4%5'
//        q().from(test).where(test.name.like("name4%5")).count();
//    }

    @Test
    public void testSyntaxForEmployee() throws SQLException {
        // EMPLOYEE
        // "select avg(salary), max(id) from employee "
        // + "group by superior_id " + "order by superior_id " + "";
        q().from(employee).groupBy(employee.superiorId).orderBy(
                employee.superiorId.asc()).list(employee.salary.avg(),
                employee.id.max());

        // "select avg(salary), max(id) from employee "
        // + "group by superior_id " + "having max(id) > 5 "
        // + "order by superior_id " + "";
        q().from(employee).groupBy(employee.superiorId).having(
                employee.id.max().gt(5)).orderBy(employee.superiorId.asc())
                .list(employee.salary.avg(), employee.id.max());

        // "select avg(salary), max(id) from employee "
        // + "group by superior_id "
        // + "having superior_id is not null "
        // + "order by superior_id " + "";
        q().from(employee).groupBy(employee.superiorId).having(
                employee.superiorId.isNotNull()).orderBy(
                employee.superiorId.asc()).list(employee.salary.avg(),
                employee.id.max());
    }

    @Test
    public void testJoins() throws SQLException {
        for (Object[] row : q().from(employee).innerJoin(employee2).on(
                employee.superiorId.eq(employee2.superiorId)).where(
                employee2.id.eq(10)).list(employee.id, employee2.id)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

//    class EmployeeProjection extends Projection {
//        public EmployeeProjection(String entityName) {
//            super(entityName);
//        }
//
//        Expr<Integer> id;
//        Expr<String> firstname;
//        Expr<Integer> superiorId;
//    }

//    @Test
//    public void testProjectionFromEntity() throws SQLException {
//        EmployeeProjection proj = new EmployeeProjection("proj");
//        for (Object[] row : q().from(employee).leftJoin(proj.from(employee2))
//                .on(proj.id.eq(employee.superiorId)).list(employee.id, proj.id,
//                        proj.superiorId)) {
//            System.out.println(row[0] + ", " + row[1]);
//        }
//    }

//    @Test
//    public void testProjectionFromSubQuery() throws SQLException {
//        EmployeeProjection proj = new EmployeeProjection("proj");
//        for (Object[] row : q().from(employee).leftJoin(
//                proj.from(select(employee2.id, employee2.firstname).from(
//                        employee2))).on(proj.id.eq(employee.superiorId)).list(
//                employee.id, proj.id)) {
//            System.out.println(row[0] + ", " + row[1]);
//        }
//    }

    @Test
    public void testIllegal() throws SQLException {
        // q().from(employee).list(employee);
    }

    @Test
    @ExcludeIn( { "oracle", "derby" })
    public void testLimitAndOffset() throws SQLException {
        // limit offset
        expectedQuery = "select employee.id from employee2 employee limit 4 offset 3";
        q().from(employee).limit(4).offset(3).list(employee.id);
    }

    @Test
    @IncludeIn("oracle")
    public void testLimitAndOffsetInOracle() throws SQLException {
        String prefix = "select employee.id from employee employee ";

        // limit
        expectedQuery = prefix + "where rownum < 4";
        q().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = prefix + "where rownum > 3";
        q().from(employee).offset(3).list(employee.id);

        // limit offset
        expectedQuery = prefix + "where rownum between 4 and 7";
        q().from(employee).limit(4).offset(3).list(employee.id);
    }

    @Test
    public void testSubQueries() throws SQLException {
        // subquery in where block
        expectedQuery = "select employee.id from employee2 employee "
                + "where employee.id = (select max(employee.id) "
                + "from employee2 employee)";
        List<Integer> list = q().from(employee).where(
//                employee.id.eq(select(Grammar.max(employee.id)).from(employee))).list(
                employee.id.eq(q().from(employee).uniqueExpr(employee.id.max()))).list(
                employee.id);
        assertFalse(list.isEmpty());
    }

    @Test
    public void testIllegalUnion() throws SQLException {
        ObjectSubQuery<Integer> sq1 = q().from(employee).uniqueExpr(employee.id.max());
        ObjectSubQuery<Integer> sq2 = q().from(employee).uniqueExpr(employee.id.max());
        try {
            q().from(employee).union(sq1, sq2).list();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

    }

    
    @Test
    public void testUnion() throws SQLException {
        // union
        ObjectSubQuery<Integer> sq1 = q().from(employee).uniqueExpr(employee.id.max());
        ObjectSubQuery<Integer> sq2 = q().from(employee).uniqueExpr(employee.id.min());
        List<Integer> list = q().union(sq1, sq2).list();
        assertFalse(list.isEmpty());

        // variation 1
        list = q().union(
                q().from(employee).uniqueExpr(employee.id.max()),
                q().from(employee).uniqueExpr(employee.id.min())).list();
        assertFalse(list.isEmpty());

        // union #2
        ObjectSubQuery<Object[]> sq3 = q().from(employee).uniqueExpr(count(), employee.id.max());
        ObjectSubQuery<Object[]> sq4 = q().from(employee).uniqueExpr(count(), employee.id.min());
        List<Object[]> list2 = q().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }

    @Test
    @ExcludeIn( { "hsqldb", "derby" })
    public void testQueryWithoutFrom() throws SQLException {
//        q().list(EConstant.create(1).add(1));
    }

    @Test
    public void testWhereExists() throws SQLException {
        ObjectSubQuery<Integer> sq1 = q().from(employee).uniqueExpr(employee.id.max());
        q().from(employee).where(exists(sq1)).count();
        q().from(employee).where(exists(sq1).not()).count();
    }

    @Test
    @ExcludeIn( { "derby" })
    public void testMathFunctions() throws SQLException {
        Expr<Integer> i = EConstant.create(1);
        Expr<Double> d = EConstant.create(1.0);
        for (Expr<?> e : Arrays.<Expr<?>> asList(MathFunctions.abs(i),
                MathFunctions.acos(d), MathFunctions.asin(d), MathFunctions
                        .atan(d), MathFunctions.ceil(d), MathFunctions.cos(d),
                MathFunctions.tan(d), MathFunctions.sqrt(i), MathFunctions
                        .sin(d), MathFunctions.round(d),
                MathFunctions.random(), MathFunctions.pow(d, d),
                // QMath.min(i,i),
                // QMath.max(i,i),
                // QMath.mod(i,i),
                MathFunctions.log10(d), MathFunctions.log(d), MathFunctions
                        .floor(d), MathFunctions.exp(d))) {
            q().from(employee).list((Expr<? extends Comparable>) e);
        }
    }

    @Test
    @ExcludeIn( { "derby" })
    public void testStringFunctions() throws SQLException {
        EString s = employee.firstname;
        for (EString e : Arrays.<EString> asList(s.lower(), s.upper(), s
                .substring(1), s.trim(), s.concat("abc"), StringFunctions
                .ltrim(s), StringFunctions.rtrim(s),
        // QString.length(s),
                StringFunctions.space(4))) {
            q().from(employee).list(e);
        }
    }

    @Test
    public void testStringFunctions2() throws SQLException {
        for (EBoolean where : Arrays.<EBoolean> asList(employee.firstname
                .startsWith("a"), employee.firstname.startsWith("a", false),
                employee.firstname.endsWith("a"), employee.firstname.endsWith(
                        "a", false))) {
            q().from(employee).where(where).list(employee.firstname);
        }
    }

//    @Test
//    @ExcludeIn( { "derby" })
//    public void testDateTimeFunctions() throws SQLException {
//        Expr<Date> d = new EConstant<Date>(new Date());
//        Expr<Time> t = new EConstant<Time>(new Time(0));
//        for (EComparable<?> e : Arrays.<EComparable<?>> asList(
//                DateTimeFunctions.currentDate(), 
//                DateTimeFunctions.currentTime(),
//
//                DateTimeFunctions.year(d), 
//                DateTimeFunctions.month(d),
//                DateTimeFunctions.week(d),
//
//                DateTimeFunctions.hours(t),
//                DateTimeFunctions.minutes(t),
//                DateTimeFunctions.seconds(t),
//
//                DateTimeFunctions.dayOfMonth(d),
//                DateTimeFunctions.dayOfWeek(d), 
//                DateTimeFunctions.dayOfYear(d))) {
//            q().from(employee).list(e);
//        }
//    }

    @Test
    @ExcludeIn( { "derby" })
    public void testCasts() throws SQLException {
        ENumber<?> num = employee.id;
        Expr<?>[] expr = new Expr[] { num.byteValue(), num.doubleValue(),
                num.floatValue(), num.intValue(), num.longValue(),
                num.shortValue(), num.stringValue() };

        for (Expr<?> e : expr) {
            q().from(employee).list(e);
        }

    }

    protected static void executeSafe(String sql) {
        try {
            stmtHolder.get().execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }

    protected final SQLQuery q() {
        return new SQLQuery(connHolder.get(), dialect) {
            @Override
            protected String buildQueryString(boolean countRow) {
                String rv = super.buildQueryString(countRow);
                if (expectedQuery != null) {
                    assertEquals(expectedQuery, rv);
                    expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }

}
