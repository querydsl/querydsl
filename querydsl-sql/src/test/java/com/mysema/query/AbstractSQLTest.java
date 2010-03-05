/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.MYSQL;
import static com.mysema.query.Target.ORACLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.hsqldb.Types;
import org.junit.AfterClass;
import org.junit.Test;

import com.mysema.commons.lang.Pair;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.domain.IdName;
import com.mysema.query.sql.domain.QEMPLOYEE;
import com.mysema.query.sql.domain.QIdName;
import com.mysema.query.sql.domain.QSURVEY;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * SqlQueryTest provides SQL query tests for different DB types
 * 
 * @author tiwe
 * @version $Id$
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSQLTest {

    static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();
    
    private static final java.sql.Date date;

//    private static final Date dateTime;    
    
    private static final QEMPLOYEE employee = new QEMPLOYEE("employee");
    
    private static final QEMPLOYEE employee2 = new QEMPLOYEE("employee2");
    
    private static final QSURVEY survey = new QSURVEY("survey");

    private static final QSURVEY survey2 = new QSURVEY("survey2");
    
//    private static final QTEST test = new QTEST("test");
    
    private static final java.sql.Time time;
    
    static{
        Calendar cal = Calendar.getInstance();
        cal.set(2000, 1, 2, 3, 4);
        cal.set(Calendar.MILLISECOND, 0);
//        dateTime = cal.getTime();
        date = new java.sql.Date(cal.getTimeInMillis());
        time = new java.sql.Time(cal.getTimeInMillis());
    }
    
    static void addEmployee(int id, String firstName, String lastName,
            double salary, int superiorId) throws Exception {
        String insert = "insert into employee2 (id, firstname, lastname, salary, datefield, timefield, superior_id) " +
        		"values(?,?,?,?,?,?,?)";
        PreparedStatement stmt = connHolder.get().prepareStatement(insert);
        stmt.setInt(1, id);
        stmt.setString(2, firstName);
        stmt.setString(3,lastName);
        stmt.setDouble(4, salary);        
        stmt.setDate(5, date);
        stmt.setTime(6, time);
        if (superiorId <= 0){
            stmt.setNull(7, Types.INTEGER);
        }else{
            stmt.setInt(7, superiorId);
        }
        stmt.execute();
        stmt.close();
    }

    protected static void executeSafe(String sql) {
        try {
            stmtHolder.get().execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (stmtHolder.get() != null){
            stmtHolder.get().close();
        }            
        if (connHolder.get() != null){
            connHolder.get().close();
        }            
    }
    
    protected SQLTemplates dialect;
    
    @Nullable
    protected String expectedQuery;
    
    private QueryExecution standardTest = new QueryExecution(Module.SQL, getClass().getAnnotation(Label.class).value()){        
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery() {
            return Pair.of(
                (Projectable)query().from(employee, employee2),
                Collections.<Expr<?>>emptyList());
        }
        @Override
        protected Pair<Projectable, List<Expr<?>>> createQuery(EBoolean filter) {
            return Pair.of(
                (Projectable)query().from(employee, employee2).where(filter),
                Collections.<Expr<?>>singletonList(employee.firstname));
        }              
    };

    @Test
    public void aggregate(){
        int min = 30000, avg = 65000, max = 160000;
        
        // uniqueResult
        assertEquals(min, query().from(employee).uniqueResult(employee.salary.min()).intValue());
        assertEquals(avg, query().from(employee).uniqueResult(employee.salary.avg()).intValue());
        assertEquals(max, query().from(employee).uniqueResult(employee.salary.max()).intValue());
        
        // list
        assertEquals(min, query().from(employee).list(employee.salary.min()).get(0).intValue());
        assertEquals(avg, query().from(employee).list(employee.salary.avg()).get(0).intValue());
        assertEquals(max, query().from(employee).list(employee.salary.max()).get(0).intValue());
    }
    
    protected SQLDeleteClause delete(PEntity<?> e){
        return new SQLDeleteClause(connHolder.get(), dialect, e);
    }
    
    @Test
    public void illegalUnion() throws SQLException {
        SubQuery<Integer> sq1 = s().from(employee).unique(employee.id.max());
        SubQuery<Integer> sq2 = s().from(employee).unique(employee.id.max());
        try {
            query().from(employee).union(sq1, sq2).list();
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

    }
    
    @Test
    @ExcludeIn({ORACLE,DERBY})
    public void limitAndOffset() throws SQLException {
        // limit offset
        expectedQuery = "select employee.id from employee2 employee limit 4 offset 3";
        query().from(employee).limit(4).offset(3).list(employee.id);
        
        // limit
        expectedQuery = "select employee.id from employee2 employee limit 4";
        query().from(employee).limit(4).list(employee.id);
        
        // offset
//        expectedQuery = "select employee.id from employee2 employee offset 3";
//        query().from(employee).offset(3).list(employee.id);
    }
    
    @Test
    @IncludeIn(DERBY)
    public void limitAndOffsetInDerby() throws SQLException {
        expectedQuery = "select employee.id from employee2 employee offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).list(employee.id);
        
        // limit
        expectedQuery = "select employee.id from employee2 employee fetch first 4 rows only";
        query().from(employee).limit(4).list(employee.id);
        
        // offset
        expectedQuery = "select employee.id from employee2 employee offset 3 rows";
        query().from(employee).offset(3).list(employee.id);
        
    }

    @Test
    @IncludeIn(ORACLE)
    public void limitAndOffsetInOracle() throws SQLException {
        String prefix = "select employee.id from employee employee ";

        // limit
        expectedQuery = prefix + "where rownum < 4";
        query().from(employee).limit(4).list(employee.id);

        // offset
        expectedQuery = prefix + "where rownum > 3";
        query().from(employee).offset(3).list(employee.id);

        // limit offset
        expectedQuery = prefix + "where rownum between 4 and 7";
        query().from(employee).limit(4).offset(3).list(employee.id);
    }

    @Test
    @ExcludeIn({DERBY})
    public void mathFunctions() throws SQLException {
//        Expr<Integer> i = ENumber.create(1);
        Expr<Double> d = ENumberConst.create(1.0);
        for (Expr<?> e : Arrays.<Expr<?>> asList(
//                MathFunctions.abs(i),
                MathFunctions.acos(d), 
                MathFunctions.asin(d), 
                MathFunctions.atan(d), 
//                MathFunctions.ceil(d), 
                MathFunctions.cos(d),
                MathFunctions.tan(d),
//                MathFunctions.sqrt(i), 
                MathFunctions.sin(d),
//                MathFunctions.round(d),
                ENumber.random(), 
                MathFunctions.pow(d, d),
                // QMath.min(i,i),
                // QMath.max(i,i),
                // QMath.mod(i,i),
                MathFunctions.log10(d), 
                MathFunctions.log(d),
//                MathFunctions.floor(d), 
                MathFunctions.exp(d))) {
            query().from(employee).list((Expr<? extends Comparable>) e);
        }
    }

    protected final SQLQuery query() {
        return new SQLQueryImpl(connHolder.get(), dialect) {
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

    @Test
    public void query1() throws Exception {
        for (String s : query().from(survey).list(survey.name)) {
            System.out.println(s);
        }
    }

    @Test
    public void query2() throws Exception {
        for (Object[] row : query().from(survey).list(survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void queryWithConstant() throws Exception {
        for (Object[] row : query().from(survey).where(survey.id.eq(1)).list(
                survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    protected SQLSubQuery s(){
        return new SQLSubQuery();
    }

    @Test
    @ExcludeIn({ORACLE, DERBY})
    public void selectBooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.eq(0)));
    }

    @Test
    @ExcludeIn({ORACLE, DERBY})
    public void selectBooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.gt(0)));
    }

    @Test
    public void selectConcat() throws SQLException {
        System.out.println(query().from(survey)
                .list(survey.name.append("Hello World")));
    }

    @Test
    public void serialization(){
        SQLQuery query = query();
        
        query.from(survey);
        assertEquals("from survey survey", query.toString());
        
        query.from(survey2);
        assertEquals("from survey survey, survey survey2", query.toString());
    }
    
    @Test
    public void standardTest(){
        standardTest.runBooleanTests(employee.firstname.isNull(), employee2.lastname.isNotNull());
        standardTest.runDateTests(employee.datefield, employee2.datefield, date);
        
        // int
        standardTest.runNumericCasts(employee.id, employee2.id, 1);
        standardTest.runNumericTests(employee.id, employee2.id, 1);
        
        // BigDecimal
//        standardTest.numericCasts(employee.salary, employee2.salary, new BigDecimal("30000.00"));
        standardTest.runNumericTests(employee.salary, employee2.salary, new BigDecimal("30000.00"));
        
        standardTest.runStringTests(employee.firstname, employee2.firstname, "Jennifer");
        standardTest.runTimeTests(employee.timefield, employee2.timefield, time);

//      standardTest.dateTimeTests(employee.birthdate, employee2.birthdate, birthDate);
      
        standardTest.report();        
    }

    @Test
    public void stringFunctions2() throws SQLException {
        for (EBoolean where : Arrays.<EBoolean> asList(employee.firstname
                .startsWith("a"), employee.firstname.startsWith("a", false),
                employee.firstname.endsWith("a"), employee.firstname.endsWith(
                        "a", false))) {
            query().from(employee).where(where).list(employee.firstname);
        }
    }

    @Test
    public void subQueries() throws SQLException {
        // subquery in where block
        expectedQuery = "select employee.id from employee2 employee "
                + "where employee.id = (select max(employee.id) "
                + "from employee2 employee)";
        List<Integer> list = query().from(employee).where(
//                employee.id.eq(select(Grammar.max(employee.id)).from(employee))).list(
                employee.id.eq(s().from(employee).unique(employee.id.max()))).list(
                employee.id);
        assertFalse(list.isEmpty());
    }

    @Test
    public void subQuerySerialization(){
        SQLSubQuery query = s();
        
        query.from(survey);
        assertEquals("from survey survey", query.toString());
        
        query.from(survey2);
        assertEquals("from survey survey, survey survey2", query.toString());
    }

    
    @Test
    public void syntaxForEmployee() throws SQLException {
        query().from(employee).groupBy(employee.superiorId).orderBy(
                employee.superiorId.asc()).list(employee.salary.avg(),
                employee.id.max());

        query().from(employee).groupBy(employee.superiorId).having(
                employee.id.max().gt(5)).orderBy(employee.superiorId.asc())
                .list(employee.salary.avg(), employee.id.max());

        query().from(employee).groupBy(employee.superiorId).having(
                employee.superiorId.isNotNull()).orderBy(
                employee.superiorId.asc()).list(employee.salary.avg(),
                employee.id.max());
    }
    
    @Test
    @ExcludeIn({DERBY})
    public void testCasts() throws SQLException {
        ENumber<?> num = employee.id;
        Expr<?>[] expr = new Expr[] { num.byteValue(), num.doubleValue(),
                num.floatValue(), num.intValue(), num.longValue(),
                num.shortValue(), num.stringValue() };

        for (Expr<?> e : expr) {
            query().from(employee).list(e);
        }

    }

    @Test
    public void testConstructor() throws Exception {
        for (IdName idName : query().from(survey).list(
                new QIdName(survey.id, survey.name))) {
            System.out.println("id and name : " + idName.getId() + ","
                    + idName.getName());
        }
    }

    @Test
    @ExcludeIn(MYSQL)
    public void testDelete(){
        // TODO : FIXME
        long count = query().from(survey).count();
        assertEquals(0, delete(survey).where(survey.name.eq("XXX")).execute());
        assertEquals(count, delete(survey).execute());
    }

    @Test
    public void testJoin() throws Exception {
        for (String name : query().from(survey, survey2).where(
                survey.id.eq(survey2.id)).list(survey.name)) {
            System.out.println(name);
        }
    }
    @Test
    public void testJoins() throws SQLException {
        for (Object[] row : query().from(employee).innerJoin(employee2).on(
                employee.superiorId.eq(employee2.superiorId)).where(
                employee2.id.eq(10)).list(employee.id, employee2.id)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    public void testUnion() throws SQLException {
        // union
        SubQuery<Integer> sq1 = s().from(employee).unique(employee.id.max());
        SubQuery<Integer> sq2 = s().from(employee).unique(employee.id.min());
        List<Integer> list = query().union(sq1, sq2).list();
        assertFalse(list.isEmpty());

        // variation 1
        list = query().union(
                s().from(employee).unique(employee.id.max()),
                s().from(employee).unique(employee.id.min())).list();
        assertFalse(list.isEmpty());

        // union #2
        ObjectSubQuery<Object[]> sq3 = s().from(employee).unique(new Expr[]{employee.id.max()});
        ObjectSubQuery<Object[]> sq4 = s().from(employee).unique(new Expr[]{employee.id.min()});
        List<Object[]> list2 = query().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }

    @Test
    public void testUpdate(){        
        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(survey.name, "S").execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());
        
        // update call with full update count
        assertEquals(count, update(survey).set(survey.name, "S").execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());
    }
    
    protected SQLUpdateClause update(PEntity<?> e){
        return new SQLUpdateClause(connHolder.get(), dialect, e);
    }
    
    @Test
    public void various() throws SQLException {
        System.out.println(query().from(survey).list(survey.name.lower()));
        System.out.println(query().from(survey).list(survey.name.append("abc")));
        System.out
                .println(query().from(survey).list(survey.id.sqrt()));
    }
    
    @Test
    public void whereExists() throws SQLException {
        SubQuery<Integer> sq1 = s().from(employee).unique(employee.id.max());
        query().from(employee).where(sq1.exists()).count();
        query().from(employee).where(sq1.exists().not()).count();
    }

}
