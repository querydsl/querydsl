/*
 * Copyright (c) 2010 Mysema Ltd.
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
    
    private static ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();

    private static final String CREATE_TABLE_DATETEST = "create table DATE_TEST(DATE_TEST date)";

    private static final String CREATE_TABLE_SURVEY = "create table SURVEY(ID int,NAME varchar(30))";

    private static final String CREATE_TABLE_TEST = "create table TEST(NAME varchar(255))";

    private static final String CREATE_TABLE_TIMETEST = "create table TIME_TEST(TIME_TEST time)";

    private static final String DROP_TABLE_DATETEST = "drop table DATE_TEST";

    private static final String DROP_TABLE_EMPLOYEE2 = "drop table EMPLOYEE2";
    
    private static final String DROP_TABLE_SURVEY = "drop table SURVEY";

    private static final String DROP_TABLE_TEST = "drop table TEST";

    private static final String DROP_TABLE_TIMETEST = "drop table TIME_TEST";

    private static final String INSERT_INTO_EMPLOYEE = "insert into EMPLOYEE2 " +
        "(ID, FIRSTNAME, LASTNAME, SALARY, DATEFIELD, TIMEFIELD, SUPERIOR_ID) " +
        "values (?,?,?,?,?,?,?)";
    
    private static final String INSERT_INTO_TEST_VALUES = "insert into TEST values(?)";

    private static ThreadLocal<Statement> stmtHolder = new ThreadLocal<Statement>();
        
    public static void close() throws SQLException{
        if (stmtHolder.get() != null){
            stmtHolder.get().close();
        }            
        if (connHolder.get() != null){
            connHolder.get().close();
        }    
    }
    
    public static Connection getConnection(){
        return connHolder.get();
    }
    
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
    
    private static Connection getOracle() throws SQLException, ClassNotFoundException{
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        return DriverManager.getConnection(url, "querydsl", "querydsl");
    }
    
    private static Connection getPostgres() throws ClassNotFoundException, SQLException{
        Class.forName("org.postgresql.Driver");
        String url = "jdbc:postgresql://localhost:5432/querydsl";
        return DriverManager.getConnection(url, "querydsl","querydsl");
    }
    
    private static Connection getSQLServer() throws ClassNotFoundException, SQLException{
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String url = "jdbc:jtds:sqlserver://localhost:1433/querydsl";
        return DriverManager.getConnection(url, "querydsl","querydsl");
    }
    
    public static Statement getStatement(){
        return stmtHolder.get();
    }
    
    public static void initDerby() throws SQLException, ClassNotFoundException{
        Connection c = getDerby();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
        // survey
        safeExecute(stmt, DROP_TABLE_SURVEY);    
        stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("insert into SURVEY values (1, 'Hello World')");

        // test
        safeExecute(stmt, DROP_TABLE_TEST);
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
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
        safeExecute(stmt, DROP_TABLE_EMPLOYEE2);
        stmt.execute("create table EMPLOYEE2("
                + "ID int, "
                + "FIRSTNAME VARCHAR(50), " 
                + "LASTNAME VARCHAR(50), "
                + "SALARY decimal(10, 2), " 
                + "DATEFIELD date, "
                + "TIMEFIELD time, "
                + "SUPERIOR_ID int, "                
                + "CONSTRAINT PK_employee PRIMARY KEY (ID), "
                + "CONSTRAINT FK_superior FOREIGN KEY (SUPERIOR_ID) "
                + "REFERENCES EMPLOYEE2(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        safeExecute(stmt, DROP_TABLE_TIMETEST);
        safeExecute(stmt, DROP_TABLE_DATETEST);
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);
    }
    
    public static void initSQLServer() throws SQLException, ClassNotFoundException{
        Connection c = getSQLServer();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
        // survey
        safeExecute(stmt, DROP_TABLE_SURVEY);    
        stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("insert into SURVEY values (1, 'Hello World')");

        // test
        safeExecute(stmt, DROP_TABLE_TEST);
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
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
        safeExecute(stmt, DROP_TABLE_EMPLOYEE2);
        stmt.execute("create table EMPLOYEE2(" 
                + "ID int, "
                + "FIRSTNAME VARCHAR(50), " 
                + "LASTNAME VARCHAR(50), "
                + "SALARY decimal(10, 2), " 
                + "DATEFIELD date, "
                + "TIMEFIELD datetime, "
                + "SUPERIOR_ID int, "                
                + "CONSTRAINT PK_employee PRIMARY KEY (ID), "
                + "CONSTRAINT FK_superior FOREIGN KEY (SUPERIOR_ID) "
                + "REFERENCES EMPLOYEE2(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        safeExecute(stmt, DROP_TABLE_TIMETEST);
        safeExecute(stmt, DROP_TABLE_DATETEST);
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);
    }
    
    public static void initHSQL() throws SQLException, ClassNotFoundException{
        Connection c = getHSQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);

        // survey
        stmt.execute("drop table SURVEY if exists");
        stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("insert into SURVEY values (1, 'Hello World');");

        // test
        stmt.execute("drop table TEST if exists");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
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
        stmt.execute("drop table EMPLOYEE2 if exists");
        stmt.execute("create table EMPLOYEE2(" 
                + "ID int, "
                + "FIRSTNAME VARCHAR(50), " 
                + "LASTNAME VARCHAR(50), "
                + "SALARY decimal(10, 2), "
                + "DATEFIELD date, "
                + "TIMEFIELD time, "
                + "SUPERIOR_ID int, "
                + "CONSTRAINT PK_employee PRIMARY KEY (ID), "
                + "CONSTRAINT FK_superior FOREIGN KEY (SUPERIOR_ID) "
                + "REFERENCES EMPLOYEE2(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table TIME_TEST if exists");
        stmt.execute("drop table DATE_TEST if exists");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);
    }
    
    public static void initMySQL() throws SQLException, ClassNotFoundException{
        Connection c = getMySQL();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
        // survey
        stmt.execute("drop table if exists SURVEY");
        stmt.execute(CREATE_TABLE_SURVEY);
        stmt.execute("insert into SURVEY values (1, 'Hello World');");

        // test
        stmt.execute("drop table if exists TEST");
        stmt.execute(CREATE_TABLE_TEST);
        PreparedStatement pstmt = c.prepareStatement(INSERT_INTO_TEST_VALUES);
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
        stmt.execute("drop table if exists EMPLOYEE2");
        stmt.execute("create table EMPLOYEE2(" 
                + "ID int, "
                + "FIRSTNAME VARCHAR(50), " 
                + "LASTNAME VARCHAR(50), "
                + "SALARY decimal(10, 2), "
                + "DATEFIELD date, "
                + "TIMEFIELD time, "
                + "SUPERIOR_ID int, "
                + "CONSTRAINT PK_employee PRIMARY KEY (ID), "
                + "CONSTRAINT FK_superior FOREIGN KEY (SUPERIOR_ID) "
                + "REFERENCES EMPLOYEE2(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);

        // date_test and time_test
        stmt.execute("drop table if exists TIME_TEST");
        stmt.execute("drop table if exists DATE_TEST");
        stmt.execute(CREATE_TABLE_TIMETEST);
        stmt.execute(CREATE_TABLE_DATETEST);
    }
    
    public static void initOracle() throws SQLException, ClassNotFoundException{
        Connection c = getOracle();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
        // survey
        safeExecute(stmt, DROP_TABLE_SURVEY);
        stmt.execute("create table SURVEY (id number(10,0),name varchar(30))");
        stmt.execute("insert into SURVEY values (1, 'Hello World')");
        
        // test
        safeExecute(stmt, DROP_TABLE_TEST);
        stmt.execute("create table TEST(name varchar(255))");
        String sql  = "insert into TEST values(?)";
        PreparedStatement pstmt = c.prepareStatement(sql);
        for (int i = 0; i < 10000; i++) {
            pstmt.setString(1, "name" + i);
            pstmt.addBatch();
        }
        pstmt.executeBatch();
        
        // employee

        safeExecute(stmt, DROP_TABLE_EMPLOYEE2);
        stmt.execute("create table EMPLOYEE2(" 
                + "ID number(10,0), "
                + "FIRSTNAME VARCHAR(50), " 
                + "LASTNAME VARCHAR(50), "
                + "SALARY decimal(10, 2), " 
                + "DATEFIELD date, "
                + "TIMEFIELD timestamp, "
                + "SUPERIOR_ID number(10,0), "
                + "CONSTRAINT PK_employee PRIMARY KEY (ID), "
                + "CONSTRAINT FK_superior FOREIGN KEY (SUPERIOR_ID) "
                + "REFERENCES EMPLOYEE2(ID))");
        addEmployees(INSERT_INTO_EMPLOYEE);
        
        // date_test and time_test
//      executeSafe("drop table time_test");
//      stmt.execute("create table time_test(time_test time)");
        safeExecute(stmt, DROP_TABLE_DATETEST);
        stmt.execute("create table date_test(date_test date)");        
    }
    
    public static void initPostgres() throws SQLException, ClassNotFoundException{
        // NOTE : unquoted identifiers are converted to lower case in Postgres
        Connection c = getPostgres();
        connHolder.set(c);
        Statement stmt = c.createStatement();
        stmtHolder.set(stmt);
        
        // survey
        safeExecute(stmt, quote(DROP_TABLE_SURVEY,"SURVEY"));
        stmt.execute(quote(CREATE_TABLE_SURVEY,"SURVEY","ID","NAME"));
        stmt.execute("insert into \"SURVEY\" values (1, 'Hello World')");

        // test
        safeExecute(stmt, quote(DROP_TABLE_TEST,"TEST"));
        stmt.execute(quote(CREATE_TABLE_TEST,"TEST","NAME"));
        String sql = quote(INSERT_INTO_TEST_VALUES,"TEST");
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
        safeExecute(stmt, quote(DROP_TABLE_EMPLOYEE2,"EMPLOYEE2"));
        stmt.execute("create table \"EMPLOYEE2\"(" 
                + "\"ID\" int, "
                + "\"FIRSTNAME\" VARCHAR(50), " 
                + "\"LASTNAME\" VARCHAR(50), "
                + "\"SALARY\" decimal(10, 2), " 
                + "\"DATEFIELD\" date, "
                + "\"TIMEFIELD\" time, "
                + "\"SUPERIOR_ID\" int, "                
                + "CONSTRAINT PK_employee PRIMARY KEY (\"ID\"), "
                + "CONSTRAINT FK_superior FOREIGN KEY (\"SUPERIOR_ID\") "
                + "REFERENCES \"EMPLOYEE2\"(\"ID\"))");
        addEmployees("insert into \"EMPLOYEE2\" " +
            "(\"ID\", \"FIRSTNAME\", \"LASTNAME\", \"SALARY\", \"DATEFIELD\", \"TIMEFIELD\", \"SUPERIOR_ID\") " +
            "values (?,?,?,?,?,?,?)");

        // date_test and time_test
        safeExecute(stmt, quote(DROP_TABLE_TIMETEST, "TIME_TEST"));
        safeExecute(stmt, quote(DROP_TABLE_DATETEST,"DATE_TEST"));
        stmt.execute(quote(CREATE_TABLE_TIMETEST, "TIME_TEST"));
        stmt.execute(quote(CREATE_TABLE_DATETEST, "DATE_TEST"));
    }
    
    private static void safeExecute(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }

    static void addEmployee(String sql, int id, String firstName, String lastName,
            double salary, int superiorId) throws SQLException {
        PreparedStatement stmt = connHolder.get().prepareStatement(sql);
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
    
    private static void addEmployees(String sql) throws SQLException {
        addEmployee(sql, 1, "Mike", "Smith", 160000, -1);
        addEmployee(sql, 2, "Mary", "Smith", 140000, -1);

        // Employee under Mike
        addEmployee(sql, 10, "Joe", "Divis", 50000, 1);
        addEmployee(sql, 11, "Peter", "Mason", 45000, 1);
        addEmployee(sql, 12, "Steve", "Johnson", 40000, 1);
        addEmployee(sql, 13, "Jim", "Hood", 35000, 1);

        // Employee under Mike
        addEmployee(sql, 20, "Jennifer", "Divis", 60000, 2);
        addEmployee(sql, 21, "Helen", "Mason", 50000, 2);
        addEmployee(sql, 22, "Daisy", "Johnson", 40000, 2);
        addEmployee(sql, 23, "Barbara", "Hood", 30000, 2);
    }    

    private static String quote(String sql, String... identifiers) {
        String rv = sql;
        for (String id : identifiers){
            rv = rv.replace(id,  "\"" + id +  "\"");
        }
        return rv;
    }
    
    private Connections(){}
}
