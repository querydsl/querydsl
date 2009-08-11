/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.mysema.query.FilteringTestRunner;
import com.mysema.query.Label;
import com.mysema.query.ResourceCheck;

/**
 * MySqlTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
@RunWith(FilteringTestRunner.class)
@ResourceCheck("/mysql.run")
@Label("mysql")
public class MySqlTest extends SqlQueryTest {

    @BeforeClass
    public static void setUp() throws Exception {
        String sql;
        Connection c = getMysqlConnection();
        Statement stmt = c.createStatement();

        connHolder.set(c);
        stmtHolder.set(stmt);

        // survey
        stmt.execute("drop table if exists survey");
        stmt.execute("create table survey (id int,name varchar(30));");
        stmt.execute("insert into survey values (1, 'Hello World');");

        // test
        stmt.execute("drop table if exists test");
        stmt.execute("create table test(name varchar(255))");
        sql = "insert into test values(?)";
        PreparedStatement pstmt = c.prepareStatement(sql);
        try{
            for (int i = 0; i < 10000; i++) {
                pstmt.setString(1, "name" + i);
                pstmt.addBatch();
            }
            pstmt.executeBatch();    
        }finally{
            pstmt.close();
        }       

        // employee
        stmt.execute("drop table if exists employee2");
        stmt.execute("create table employee2(id int, "
                + "firstname VARCHAR(50), " + "lastname VARCHAR(50), "
                + "salary decimal(10, 2), " + "superior_id int, "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) "
                + "REFERENCES employee2(ID))");
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
        stmt.execute("drop table if exists time_test");
        stmt.execute("drop table if exists date_test");
        stmt.execute("create table time_test(time_test time)");
        stmt.execute("create table date_test(date_test date)");
    }

    @Before
    public void setUpForTest() {
        dialect = Dialect.forMySQL().newLineToSingleSpace();
    }

    private static void addEmployee(int id, String firstName, String lastName,
            double salary, int superiorId) throws Exception {
        stmtHolder.get().execute(
                "insert into employee2 values(" + id + ", '" + firstName
                        + "', '" + lastName + "', " + salary + ", "
                        + (superiorId <= 0 ? "null" : ("" + superiorId)) + ")");
    }

    private static Connection getMysqlConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/querydsl";
        return DriverManager.getConnection(url, "root", "");
    }
}
