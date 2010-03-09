/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.hsqldb.Types;

/**
 * @author tiwe
 *
 */
public final class Connections {
    
    private static final String INSERT_INTO_EMPLOYEE = "insert into employee2 " +
        "(id, firstname, lastname, salary, datefield, timefield, superior_id) " +
        "values (?,?,?,?,?,?,?)";
    
    private static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    private static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();
    
    public static Connection getConnection(){
        return connHolder.get();
    }
    
    public static Statement getStatement(){
        return stmtHolder.get();
    }
    
    private Connections(){}
    
    private static Connection getDerby() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/demoDB;create=true";
        return DriverManager.getConnection(url, "", "");
    }
    
    private static Connection getHSQL() throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:target/tutorial";
        return DriverManager.getConnection(url, "sa", "");
    }

    private static Connection getMySQL() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/querydsl";
        return DriverManager.getConnection(url, "root", "");
    }
    
    public static void close() throws SQLException{
        if (stmtHolder.get() != null){
            stmtHolder.get().close();
        }            
        if (connHolder.get() != null){
            connHolder.get().close();
        }    
    }
    
    public static void initHSQL() throws Exception{
        String sql;
        Connection c = getHSQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        // survey
        stmt.execute("drop table survey if exists");
        stmt.execute("create table survey (id int,name varchar(30));");
        stmt.execute("insert into survey values (1, 'Hello World');");

        // test
        stmt.execute("drop table test if exists");
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
        stmt.execute("drop table employee2 if exists");
        stmt.execute("create table employee2(id int, "
                + "firstname VARCHAR(50), " 
                + "lastname VARCHAR(50), "
                + "salary decimal(10, 2), "
                + "datefield date, "
                + "timefield time, "
                + "superior_id int, "
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
        stmt.execute("drop table time_test if exists");
        stmt.execute("drop table date_test if exists");
        stmt.execute("create table time_test(time_test time)");
        stmt.execute("create table date_test(date_test date)");
    }
    
    public static void initMySQL() throws Exception{
        Connection c = getMySQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        // survey
        stmt.execute("drop table if exists survey");
        stmt.execute("create table survey (id int,name varchar(30));");
        stmt.execute("insert into survey values (1, 'Hello World');");

        // test
        stmt.execute("drop table if exists test");
        stmt.execute("create table test(name varchar(255))");
        String sql = "insert into test values(?)";
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
                + "firstname VARCHAR(50), " 
                + "lastname VARCHAR(50), "
                + "salary decimal(10, 2), "
                + "datefield date, "
                + "timefield time, "
                + "superior_id int, "
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
    
    public static void initDerby() throws Exception{
        Connection c = getDerby();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
     // survey
        // stmt.execute("drop table survey if exists");
        safeExecute(stmt, "drop table survey");
        stmt.execute("create table survey (id int,name varchar(30))");
        stmt.execute("insert into survey values (1, 'Hello World')");

        // test
        // stmt.execute("drop table test if exists");
        safeExecute(stmt, "drop table test");
        stmt.execute("create table test(name varchar(255))");
        String sql = "insert into test values(?)";
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
        // stmt.execute("drop table employee if exists");
        safeExecute(stmt, "drop table employee2");
        stmt.execute("create table employee2(id int, "
                + "firstname VARCHAR(50), " 
                + "lastname VARCHAR(50), "
                + "salary decimal(10, 2), " 
                + "datefield date, "
                + "timefield time, "
                + "superior_id int, "                
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
        safeExecute(stmt, "drop table time_test");
        safeExecute(stmt, "drop table date_test");
        stmt.execute("create table time_test(time_test time)");
        stmt.execute("create table date_test(date_test date)");
    }
    
    static void addEmployee(int id, String firstName, String lastName,
            double salary, int superiorId) throws Exception {
        PreparedStatement stmt = connHolder.get().prepareStatement(INSERT_INTO_EMPLOYEE);
        stmt.setInt(1, id);
        stmt.setString(2, firstName);
        stmt.setString(3,lastName);
        stmt.setDouble(4, salary);        
        stmt.setDate(5, Constants.date);
        stmt.setTime(6, Constants.time);
        if (superiorId <= 0){
            stmt.setNull(7, Types.INTEGER);
        }else{
            stmt.setInt(7, superiorId);
        }
        stmt.execute();
        stmt.close();
    }

    
    private static void safeExecute(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }
}
