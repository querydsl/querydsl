/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static com.mysema.query.Constants.date;
import static com.mysema.query.Constants.employee;
import static com.mysema.query.Constants.employee2;
import static com.mysema.query.Constants.survey;
import static com.mysema.query.Constants.survey2;
import static com.mysema.query.Constants.time;
import static com.mysema.query.Target.DERBY;
import static com.mysema.query.Target.ORACLE;
import static com.mysema.query.Target.SQLSERVER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLSubQuery;
import com.mysema.query.sql.domain.IdName;
import com.mysema.query.sql.domain.QIdName;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ENumberConst;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.SubQuery;
import com.mysema.testutil.ExcludeIn;
import com.mysema.testutil.IncludeIn;
import com.mysema.testutil.Label;

public abstract class SelectBaseTest extends AbstractBaseTest{
    
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
    
    
    @Test
    @ExcludeIn({DERBY})
    public void casts() throws SQLException {
        ENumber<?> num = employee.id;
        Expr<?>[] expr = new Expr[] { 
                num.byteValue(), 
                num.doubleValue(),
                num.floatValue(), 
                num.intValue(), 
                num.longValue(),
                num.shortValue(), 
                num.stringValue() };

        for (Expr<?> e : expr) {
            query().from(employee).list(e);
        }

    }
    
    @Test
    public void constructor() throws Exception {
        for (IdName idName : query().from(survey).list(new QIdName(survey.id, survey.name))) {
            System.out.println("id and name : " + idName.getId() + ","+ idName.getName());
        }
    }
    
    @Test
    public void getResultSet() throws IOException, SQLException{
        ResultSet results = query().from(survey).getResults(survey.id, survey.name);
        while(results.next()){
            System.out.println(results.getInt(1) +","+results.getString(2));
        }
        results.close();
    }

    @SuppressWarnings("unchecked")
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
    public void join() throws Exception {
        for (String name : query().from(survey, survey2)
            .where(survey.id.eq(survey2.id)).list(survey.name)) {
            System.out.println(name);
        }
    }

    @Test
    public void joins() throws SQLException {
        for (Object[] row : query().from(employee).innerJoin(employee2)
                .on(employee.superiorId.eq(employee2.superiorId))
                .where(employee2.id.eq(10))
                .list(employee.id, employee2.id)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }
    
    @Test
    public void limitAndOffset() throws SQLException {
        // limit offset
        query().from(employee).limit(4).offset(3).list(employee.id);
        
        // limit
        query().from(employee).limit(4).list(employee.id);
    }

    @Test
    @ExcludeIn({ORACLE,DERBY,SQLSERVER})
    public void limitAndOffse2t() throws SQLException {
        // limit offset
        expectedQuery = "select e.id from employee2 e limit 4 offset 3";
        query().from(employee).limit(4).offset(3).list(employee.id);
        
        // limit
        expectedQuery = "select e.id from employee2 e limit 4";
        query().from(employee).limit(4).list(employee.id);
        
        // offset
//        expectedQuery = "select employee.id from employee2 employee offset 3";
//        query().from(employee).offset(3).list(employee.id);
    }

    @Test
    @IncludeIn(DERBY)
    public void limitAndOffsetInDerby() throws SQLException {
        expectedQuery = "select e.id from employee2 e offset 3 rows fetch next 4 rows only";
        query().from(employee).limit(4).offset(3).list(employee.id);
        
        // limit
        expectedQuery = "select e.id from employee2 e fetch first 4 rows only";
        query().from(employee).limit(4).list(employee.id);
        
        // offset
        expectedQuery = "select e.id from employee2 e offset 3 rows";
        query().from(employee).offset(3).list(employee.id);
        
    }
    
    @Test
    @IncludeIn(ORACLE)
    public void limitAndOffsetInOracle() throws SQLException {
        String prefix = "select e.id from employee2 e ";

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

    @SuppressWarnings("unchecked")
    @Test
    @ExcludeIn({DERBY})
    public void mathFunctions() throws SQLException {
        Expr<Double> d = ENumberConst.create(1.0);
        for (Expr<?> e : Arrays.<Expr<?>> asList(
                MathFunctions.acos(d), 
                MathFunctions.asin(d), 
                MathFunctions.atan(d), 
                MathFunctions.cos(d),
                MathFunctions.tan(d), 
                MathFunctions.sin(d),
                ENumber.random(), 
                MathFunctions.pow(d, d),
                MathFunctions.log10(d), 
                MathFunctions.log(d),
                MathFunctions.exp(d))) {
            query().from(employee).list((Expr<? extends Comparable>) e);
        }
    }

    @Test
    public void projection() throws IOException{
        CloseableIterator<Object[]> results = query().from(survey).iterate(survey.all());
        assertTrue(results.hasNext());
        while (results.hasNext()){
            System.out.println(Arrays.asList(results.next()));
        }
        results.close();
    }

    @Test
    public void projection2() throws IOException{
        CloseableIterator<Object[]> results = query().from(survey).iterate(survey.id, survey.name);
        assertTrue(results.hasNext());
        while (results.hasNext()){
            System.out.println(Arrays.asList(results.next()));
        }
        results.close();
    }

    @Test
    public void projection3() throws IOException{
        CloseableIterator<String> names = query().from(survey).iterate(survey.name);
        assertTrue(names.hasNext());
        while (names.hasNext()){
            System.out.println(names.next());
        }
        names.close();
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
        for (Object[] row : query().from(survey)
                .where(survey.id.eq(1))
                .list(survey.id, survey.name)) {
            System.out.println(row[0] + ", " + row[1]);
        }
    }

    @Test
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void selectBooleanExpr() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.eq(0)));
    }

    
    @Test
    @ExcludeIn({ORACLE, DERBY, SQLSERVER})
    public void selectBooleanExpr2() throws SQLException {
        // TODO : FIXME
        System.out.println(query().from(survey).list(survey.id.gt(0)));
    }
    
    @Test
    public void selectConcat() throws SQLException {
        System.out.println(query().from(survey).list(survey.name.append("Hello World")));
    }

    @Test
    public void serialization(){
        SQLQuery query = query();
        
        query.from(survey);
        assertEquals("from survey s", query.toString());
        
        query.from(survey2);
        assertEquals("from survey s, survey s2", query.toString());
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
        for (EBoolean where : Arrays.<EBoolean> asList(
                employee.firstname.startsWith("a"), 
                employee.firstname.startsWith("a", false),
                employee.firstname.endsWith("a"), 
                employee.firstname.endsWith("a", false))) {
            query().from(employee).where(where).list(employee.firstname);
        }
    }
    @Test
    public void subQueries() throws SQLException {
        // subquery in where block
        expectedQuery = "select e.id from employee2 e "
                + "where e.id = (select max(e.id) "
                + "from employee2 e)";
        List<Integer> list = query().from(employee)
            .where(employee.id.eq(s().from(employee).unique(employee.id.max())))
            .list(employee.id);
        assertFalse(list.isEmpty());
    }

    @Test
    public void subQuerySerialization(){
        SQLSubQuery query = s();
        
        query.from(survey);
        assertEquals("from survey s", query.toString());
        
        query.from(survey2);
        assertEquals("from survey s, survey s2", query.toString());
    }
    
    @Test
    public void syntaxForEmployee() throws SQLException {
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
    
    @SuppressWarnings("unchecked")
    @Test
    public void union() throws SQLException {
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
    public void various() throws SQLException {
        System.out.println(query().from(survey).list(survey.name.lower()));
        System.out.println(query().from(survey).list(survey.name.append("abc")));
        System.out.println(query().from(survey).list(survey.id.sqrt()));
    }
    
    @Test
    public void whereExists() throws SQLException {
        SubQuery<Integer> sq1 = s().from(employee).unique(employee.id.max());
        query().from(employee).where(sq1.exists()).count();
        query().from(employee).where(sq1.exists().not()).count();
    }

}
