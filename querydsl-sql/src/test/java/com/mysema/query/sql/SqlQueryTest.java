/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static com.mysema.query.grammar.Grammar.avg;
import static com.mysema.query.grammar.Grammar.count;
import static com.mysema.query.grammar.Grammar.not;
import static com.mysema.query.grammar.QMath.add;
import static com.mysema.query.grammar.QMath.max;
import static com.mysema.query.grammar.QMath.min;
import static com.mysema.query.grammar.SqlGrammar.exists;
import static com.mysema.query.grammar.SqlGrammar.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Test;

import com.mysema.query.ExcludeIn;
import com.mysema.query.IncludeIn;
import com.mysema.query.grammar.QDateTime;
import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.QString;
import com.mysema.query.grammar.SqlJoinMeta;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Projection;
import com.mysema.query.grammar.types.SubQuery;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.Expr.ENumber;
import com.mysema.query.grammar.types.Expr.EString;
import com.mysema.query.sql.domain.QEMPLOYEE;
import com.mysema.query.sql.domain.QSURVEY;
import com.mysema.query.sql.domain.QTEST;
import com.mysema.query.sql.dto.IdName;
import com.mysema.query.sql.dto.QIdName;


/**
 * SqlQueryTest provides SQL query tests for different DB types
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class SqlQueryTest {
    
    protected static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();
    
    protected static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();
    
    protected SqlOps dialect;
    
    protected final QEMPLOYEE employee = new QEMPLOYEE("employee");
    protected final QEMPLOYEE employee2 = new QEMPLOYEE("employee2");
    protected final QSURVEY survey = new QSURVEY("survey");    
    protected final QSURVEY survey2 = new QSURVEY("survey2");    
    protected final QTEST test = new QTEST("test");
    
    protected String expectedQuery;
        
    @AfterClass
    public static void tearDown() throws Exception{
        if (stmtHolder.get() != null) stmtHolder.get().close();
        if (connHolder.get() != null) connHolder.get().close();
    }
    
    @Test
    public void testQuery1() throws Exception{                
        for (String s : q().from(survey).list(survey.name)){
            System.out.println(s);
        }        
    }
    
    @Test
    public void testQuery2() throws Exception{              
        for (Object[] row : q().from(survey).list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }        
    }
    
    @Test
    public void testQueryWithConstant() throws Exception{
        for (Object[] row : q().from(survey)
                           .where(survey.id.eq(1))
                           .list(survey.id, survey.name)){
            System.out.println(row[0]+", " + row[1]);
        }
    }
    
    @Test
    public void testJoin() throws Exception{
        for (String name : q().from(survey, survey2)
                          .where(survey.id.eq(survey2.id))
                          .list(survey.name)){
            System.out.println(name);
        }
    }
    
    @Test
    public void testConstructor() throws Exception{
        for (IdName idName : q().from(survey)
                            .list(new QIdName(survey.id, survey.name))){
            System.out.println("id and name : " + idName.getId()+ ","+idName.getName());
        }
    }
    
    @Test
    public void testVarious() throws SQLException{
        System.out.println(q().from(survey).list(survey.name.lower()));
        System.out.println(q().from(survey).list(survey.name.add("abc")));                
        System.out.println(q().from(survey).list(QMath.sqrt(survey.id)));        
    }
    
    @Test
    public void testColumnAlias() throws SQLException{
        q().from(employee).list(
            employee.firstname.as("fn"),
            employee.firstname.lower().as("fnLc"));
    }
        
    @Test
    public void testSelectConcat() throws SQLException{
        System.out.println(q().from(survey).list(survey.name.add("Hello World")));
    }
    
    @Test
    @ExcludeIn("oracle")
    public void testSelectBooleanExpr() throws SQLException{
        // TODO : FIXME
        System.out.println(q().from(survey).list(survey.id.eq(0)));
    }
    
    @Test
    @ExcludeIn("oracle")
    public void testSelectBooleanExpr2() throws SQLException{
        // TODO : FIXME
        System.out.println(q().from(survey).list(survey.id.gt(0)));
    }
    
    @Test
    public void testSyntaxForTest() throws SQLException{        
        // TEST        
        // select count(*) from test where name = null
        expectedQuery = "select count(*) from test test where test.name is null";
        q().from(test).where(test.name.isnull()).count();        
        // select count(*) from test where name like null
//        q().from(test).where(test.name.like(null)).count();
        // select count(*) from test where name = ''
        q().from(test).where(test.name.like("")).count();        
        // select count(*) from test where name is not null
        q().from(test).where(test.name.isnotnull()).count();
        // select count(*) from test where name like '%'
        q().from(test).where(test.name.like("%")).count();
        // select count(*) from test where left(name, 6) = 'name44'
        q().from(test).where(test.name.substring(0,6).like("name44%")).count();
        // select count(*) from test where name like 'name44%'
        q().from(test).where(test.name.like("name44%")).count();
        // select count(*) from test where left(name,5) = 'name4' and right(name,1) = 5
        // TODO
        // select count(*) from test where name like 'name4%5'
        q().from(test).where(test.name.like("name4%5")).count();
        // select count(*) from test where left(name,5) = 'name4' and right(name,1) = 5
        // TODO
        // select count(*) from test where name like 'name4%5'
        q().from(test).where(test.name.like("name4%5")).count();
    }
    
    @Test
    public void testSyntaxForEmployee() throws SQLException{
        // EMPLOYEE
//        "select avg(salary), max(id) from employee "
//        + "group by superior_id " + "order by superior_id " + "";
        q().from(employee)
           .groupBy(employee.superiorId)
           .orderBy(employee.superiorId.asc())
           .list(avg(employee.salary), max(employee.id));
        
//        "select avg(salary), max(id) from employee "
//        + "group by superior_id " + "having max(id) > 5 "
//        + "order by superior_id " + "";
        q().from(employee)
           .groupBy(employee.superiorId).having(max(employee.id).gt(5))
           .orderBy(employee.superiorId.asc())
           .list(avg(employee.salary), max(employee.id));
        
//        "select avg(salary), max(id) from employee "
//        + "group by superior_id "
//        + "having superior_id is not null "
//        + "order by superior_id " + "";
        q().from(employee)
           .groupBy(employee.superiorId).having(employee.superiorId.isnotnull())
           .orderBy(employee.superiorId.asc())
           .list(avg(employee.salary),max(employee.id));        
    }
    
    @Test
    public void testJoins() throws SQLException{
        for (Object[] row : q().from(employee).innerJoin(employee2)
           .on(employee.superiorId.eq(employee2.superiorId))
           .where(employee2.id.eq(10))
           .list(employee.id, employee2.id)){
            System.out.println(row[0] + ", " + row[1]);
        }
    }
    
    class EmployeeProjection extends Projection{
        public EmployeeProjection(String entityName) {
            super(entityName);
        }
        Expr<Integer> id;
        Expr<String> firstname;
        Expr<Integer> superiorId;
    }
        
    @Test
    public void testProjectionFromEntity() throws SQLException{
        EmployeeProjection proj = new EmployeeProjection("proj");
        for (Object[] row : q().from(employee).leftJoin(proj.from(employee2))                
                .on(proj.id.eq(employee.superiorId))
            .list(employee.id, proj.id, proj.superiorId)){
            System.out.println(row[0] + ", " + row[1]);
        }
    }
       
    
    @Test
    public void testProjectionFromSubQuery() throws SQLException{
        EmployeeProjection proj = new EmployeeProjection("proj");
        for (Object[] row : q().from(employee).leftJoin(
            proj.from(select(employee2.id,employee2.firstname).from(employee2)))
                .on(proj.id.eq(employee.superiorId))
            .list(employee.id, proj.id)){
            System.out.println(row[0] + ", " + row[1]);
        }        
    }
    
    
    @Test
    public void testIllegal() throws SQLException{
//        q().from(employee).list(employee);
    }
    
    @Test
    @ExcludeIn("oracle")
    public void testLimitAndOffset() throws SQLException{
        // limit offset
        expectedQuery = "select employee.id from employee employee limit 4 offset 3";
        q().from(employee).limit(4).offset(3).list(employee.id);        
    }
    
    @Test
    @IncludeIn("oracle")
    public void testLimitAndOffsetInOracle() throws SQLException{
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
    public void testSubQueries() throws SQLException{
     // subquery in where block
        expectedQuery = "select employee.id from employee employee " +
            "where employee.id = (select max(employee.id) " + 
            "from employee employee)";
        List<Integer> list = q().from(employee).where(employee.id.eq(
                select(max(employee.id)).from(employee)))
            .list(employee.id);
        assertFalse(list.isEmpty());
    }
    
    @Test
    public void testIllegalUnion() throws SQLException{
        SubQuery<SqlJoinMeta,Integer> sq1 = select(max(employee.id)).from(employee);
        SubQuery<SqlJoinMeta,Integer> sq2 = select(min(employee.id)).from(employee);
        try{
            q().from(employee).union(sq1, sq2).list();
            fail();
        }catch(IllegalArgumentException e){
            // expected
        }
        
    }
    
    @Test
    public void testUnion() throws SQLException{
        // union
        SubQuery<SqlJoinMeta,Integer> sq1 = select(max(employee.id)).from(employee);
        SubQuery<SqlJoinMeta,Integer> sq2 = select(min(employee.id)).from(employee);
        List<Integer> list = q().union(sq1, sq2).list(); 
        assertFalse(list.isEmpty());
        
        // variation 1
        list = q().union(
            select(max(employee.id)).from(employee), 
            select(min(employee.id)).from(employee)).list();
        assertFalse(list.isEmpty());
    
        // union #2
        SubQuery<SqlJoinMeta,Object[]> sq3 = select(count(), max(employee.id)).from(employee);
        SubQuery<SqlJoinMeta,Object[]> sq4 = select(count(), min(employee.id)).from(employee);
        List<Object[]> list2 = q().union(sq3, sq4).list();
        assertFalse(list2.isEmpty());
    }
    
    @Test
    @ExcludeIn("hsqldb")
    public void testQueryWithoutFrom() throws SQLException{
        q().list(add(new Expr.EConstant<Integer>(1),1));
    }
    
    @Test
    public void testWhereExists() throws SQLException{
        SubQuery<SqlJoinMeta,Integer> sq1 = select(max(employee.id)).from(employee);
        q().from(employee).where(exists(sq1)).count();
        q().from(employee).where(not(exists(sq1))).count();
    }
    
    @Test
    public void testMathFunctions() throws SQLException{
        Expr<Integer> i = new Expr.EConstant<Integer>(1);
        Expr<Double> d = new Expr.EConstant<Double>(1.0);
        for (Expr<?> e : Arrays.<Expr<?>>asList(
                QMath.abs(i),
                QMath.acos(d),
                QMath.asin(d),
                QMath.atan(d),
                QMath.ceil(d),
                QMath.cos(d),
                QMath.tan(d),
                QMath.sqrt(i),
                QMath.sin(d),
                QMath.round(d),
                QMath.random(),
                QMath.pow(d,d),
//                QMath.min(i,i),
//                QMath.max(i,i),
//                QMath.mod(i,i),
                QMath.log10(d),
                QMath.log(d),
                QMath.floor(d),
                QMath.exp(d))){
            q().from(employee).list((Expr<? extends Comparable>)e);
        }
    }
    
    @Test
    public void testStringFunctions() throws SQLException{
        EString s = employee.firstname;
        for (EString e : Arrays.<EString>asList(
                s.lower(),
                s.upper(),
                s.substring(1),
                s.trim(),
                s.concat("abc"),
                QString.ltrim(s),
                QString.rtrim(s),
//                QString.length(s),
                QString.space(4))){
            q().from(employee).list(e);
        }    
    }
    
    @Test
    public void testDateTimeFunctions() throws SQLException{
        Expr<Date> d = new Expr.EConstant<Date>(new Date());
        Expr<Time> t = new Expr.EConstant<Time>(new Time(0));
        for (EComparable<?> e : Arrays.<EComparable<?>>asList(
                QDateTime.currentDate(),
                QDateTime.currentTime(),
                QDateTime.now(),
                
                QDateTime.year(d),
                QDateTime.month(d),
                QDateTime.week(d),
                
                QDateTime.hour(t),
                QDateTime.minute(t),
                QDateTime.second(t),
                
                QDateTime.dayOfMonth(d),
                QDateTime.dayOfWeek(d),
                QDateTime.dayOfYear(d)
        )){
            q().from(employee).list(e);
        }    
    }
    
    @Test
    public void testCasts() throws SQLException{        
        ENumber<?> num = employee.id;
        Expr<?>[] expr = new Expr[]{
                num.byteValue(), 
                num.doubleValue(),
                num.floatValue(),
                num.intValue(),
                num.longValue(),
                num.shortValue(),
                num.stringValue()};
        
        for (Expr<?> e : expr){
            q().from(employee).list(e);    
        }        
        
    }
    
    protected static void executeSafe(String sql){
        try {
            stmtHolder.get().execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }
    
    protected final SqlQuery q(){
        return new SqlQuery(connHolder.get(), dialect){
            @Override
            protected String buildQueryString() {
                String rv = super.buildQueryString();
                if (expectedQuery != null){
                   assertEquals(expectedQuery, rv);
                   expectedQuery = null;
                }
                System.out.println(rv);
                return rv;
            }
        };
    }
    
}
