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
package com.querydsl.sql.h2;

import java.sql.*;
import java.util.Collections;

import com.querydsl.sql.QGeneratedKeysEntity;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.dml.SQLInsertClause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GeneratedKeysH2Test {

    private Connection conn;

    private Statement stmt;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:~/dbs/h2-gen";
        conn = DriverManager.getConnection(url, "sa", "");
        stmt = conn.createStatement();
    }

    @After
    public void tearDown() throws SQLException{
        try{
            stmt.close();
        }finally{
            conn.close();
        }
    }

    @Test
    public void test() throws SQLException{
        stmt.execute("drop table GENERATED_KEYS if exists");
        stmt.execute("create table GENERATED_KEYS(" +
                 "ID int AUTO_INCREMENT PRIMARY KEY, " +
                 "NAME varchar(30))");

        QGeneratedKeysEntity entity = new QGeneratedKeysEntity("entity");
        SQLInsertClause insertClause = new SQLInsertClause(conn, new H2Templates(), entity);
        ResultSet rs = insertClause.set(entity.name, "Hello").executeWithKeys();
        ResultSetMetaData md = rs.getMetaData();
        System.out.println(md.getColumnName(1));
        
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());

        insertClause = new SQLInsertClause(conn, new H2Templates(), entity);
        rs = insertClause.set(entity.name, "World").executeWithKeys();
        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        assertFalse(rs.next());

        insertClause = new SQLInsertClause(conn, new H2Templates(), entity);
        assertEquals(3, insertClause.set(entity.name, "World").executeWithKey(entity.id).intValue());
        
        insertClause = new SQLInsertClause(conn, new H2Templates(), entity);
        assertEquals(Collections.singletonList(4), insertClause.set(entity.name, "World").executeWithKeys(entity.id));
    }

}
