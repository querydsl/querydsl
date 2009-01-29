/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static com.mysema.query.grammar.OracleGrammar.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.FilteringTestRunner;
import com.mysema.query.Label;
import com.mysema.query.ResourceCheck;
import com.mysema.query.grammar.Dialect;
import com.mysema.query.sql.domain.QEMPLOYEE;

/**
 * MySqlTest provides
 *
 * @author tiwe
 * @version $Id$
 */
@RunWith(FilteringTestRunner.class)
@ResourceCheck("/oracle.run")
@Label("oracle")
public class OracleTest extends SqlQueryTest{
    
    @Test
    public void testConnectByPrior() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
        		"from employee employee " +
        		"connect by prior employee.id = employee.superior_id";
        qo().from(employee)
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior2() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "start with employee.id = ? " +
                "connect by prior employee.id = employee.superior_id";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior3() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "start with employee.id = ? " +                
                "connect by prior employee.id = employee.superior_id " +
                "order siblings by employee.lastname";
        qo().from(employee)
            .startWith(employee.id.eq(1))
            .connectByPrior(employee.id.eq(employee.superiorId))
            .orderSiblingsBy(employee.lastname)
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    public void testConnectByPrior4() throws SQLException{
        expectedQuery = 
                "select employee.id, employee.lastname, employee.superior_id " +
                "from employee employee " +
                "connect by nocycle prior employee.id = employee.superior_id";
        qo().from(employee)
            .connectByNocyclePrior(employee.id.eq(employee.superiorId))
            .list(employee.id, employee.lastname, employee.superiorId);
    }
    
    @Test
    @Ignore
    public void testConnectBy() throws SQLException{
        // TODO : come up with a legal case
        qo().from(employee)
            .where(level.eq(-1))
            .connectBy(level.lt(1000))            
            .list(employee.id);
    }
    
    @Test
    public void testSumOver() throws SQLException{
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
        expectedQuery = "select employee.lastname, employee.salary, " +
        		"sum(employee.salary) over (partition by employee.superior_id order by employee.lastname, employee.salary), " +
        		"sum(employee.salary) over ( order by employee.superior_id, employee.salary), " +
        		"sum(employee.salary) over () from employee employee order by employee.salary asc, employee.superior_id asc";
        qo().from(employee)
            .orderBy(employee.salary.asc(), employee.superiorId.asc())
            .list(
               employee.lastname,
               employee.salary,
               sumOver(employee.salary).partition(employee.superiorId).order(employee.lastname, employee.salary),
               sumOver(employee.salary).order(employee.superiorId, employee.salary),
               sumOver(employee.salary));
        
        // shorter version
        QEMPLOYEE e = employee;
        qo().from(e)
            .orderBy(e.salary.asc(), e.superiorId.asc())
            .list(e.lastname, e.salary,
               sumOver(e.salary).partition(e.superiorId).order(e.lastname, e.salary),
               sumOver(e.salary).order(e.superiorId, e.salary),
               sumOver(e.salary));
    }
            
    @BeforeClass
    public static void setUp() throws Exception{
        String sql;
        Connection c = getOracleConnection();
        Statement stmt = c.createStatement();
        
        connHolder.set(c);
        stmtHolder.set(stmt);
        
        // survey
        executeSafe("drop table survey");
        stmt.execute("create table survey (id number(10,0),name varchar(30))");
        stmt.execute("insert into survey values (1, 'Hello World')");
        
        // test
        executeSafe("drop table test");
        stmt.execute("create table test(name varchar(255))");
        sql   = "insert into test values(?)";
        PreparedStatement pstmt = c.prepareStatement(sql);
        for (int i = 0; i < 10000; i++) {
            pstmt.setString(1, "name" + i);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        
        // employee
        executeSafe("drop table employee");
        stmt.execute("create table employee(id number(10,0), "
                + "firstname VARCHAR(50), " + "lastname VARCHAR(50), "
                + "salary decimal(10, 2), " + "superior_id number(10,0), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) "
                + "REFERENCES employee(ID))");
        addEmployee(1, "Mike", "Smith", 160000, -1);
        addEmployee(2, "Mary", "Smith", 140000, -1);

        // Employee under Mike
        addEmployee(10, "Joe", "Divis", 50000, 1);
        addEmployee(11, "Peter", "Mason", 45000, 1);
        addEmployee(12, "Steve", "Johnson", 40000, 1);
        addEmployee(13, "Jim", "Hood", 35000, 1);

        // Employee under Mike
        addEmployee(20, "Jennifer", "Divis", 60000, 2);
        addEmployee(21, "Helen", "Mason", 50000, 2);
        addEmployee(22, "Daisy", "Johnson", 40000, 2);
        addEmployee(23, "Barbara", "Hood", 30000, 2);
        
        // date_test and time_test
//        executeSafe("drop table time_test");
        executeSafe("drop table date_test");
//        stmt.execute("create table time_test(time_test time)");
        stmt.execute("create table date_test(date_test date)");        
    }
    
    @Before
    public void setUpForTest(){
        dialect = Dialect.forOracle().newLineToSingleSpace();
    }
    
    protected OracleQuery qo(){
        return new OracleQuery(connHolder.get(), dialect){
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
    
    private static void addEmployee(int id, String firstName, String lastName, double salary,
            int superiorId) throws Exception {
        stmtHolder.get().execute("insert into employee values(" + id + ", '" + firstName
                + "', '" + lastName + "', " + salary + ", "
                + (superiorId <= 0 ? "null" : ("" + superiorId)) + ")");
    }
    
    private static Connection getOracleConnection() throws Exception{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@10.1.3.2:1521:xe";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }
}
