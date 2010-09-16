/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractJDBCTest {

    protected Connection connection;

    protected Statement statement;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:mem:testdb";
        connection = DriverManager.getConnection(url, "sa", "");
        statement = connection.createStatement();
    }

    @After
    public void tearDown() throws SQLException{
        try{
            statement.close();
        }finally{
            connection.close();
        }
    }
    
}
