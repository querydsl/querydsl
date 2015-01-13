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
package com.querydsl.sql;

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
    public void setUp() throws ClassNotFoundException, SQLException {
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
