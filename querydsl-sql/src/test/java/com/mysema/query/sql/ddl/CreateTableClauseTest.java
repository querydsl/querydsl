/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.ddl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLTemplates;

public class CreateTableClauseTest {
    
    private Connection conn;
    
    private SQLTemplates templates = new H2Templates();

    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:target/h2";
        conn = DriverManager.getConnection(url, "sa", "");
        
        Statement stmt = conn.createStatement();
        try {
            stmt.execute("drop table language if exists");
            stmt.execute("drop table symbol if exists");
            stmt.execute("drop table statement if exists");    
        }finally{
            stmt.close();    
        }
    }

    @After
    public void tearDown() throws SQLException{
        conn.close();
    }
    
    @Test
    public void test() throws SQLException{
        new  CreateTableClause(conn, templates,  "language")
        .column("id", Integer.class).notNull()
        .column("text", String.class).size(256).notNull()
        .primaryKey("PK_LANGUAGE","id")
        .execute();
        
        new  CreateTableClause(conn, templates,  "symbol")
        .column("id", Long.class).notNull()
        .column("lexical", String.class).size(1024).notNull()
        .column("datatype", String.class)
        .column("lang", Integer.class)
        .column("integer",Long.class)
        .column("floating",Double.class)
        .column("datetime",Timestamp.class)
        .primaryKey("PK_SYMBOL","id")
        .foreignKey("FK_LANG","lang").references("language","id")
        .execute();
        
        new  CreateTableClause(conn, templates,  "statement")
        .column("model", Long.class)
        .column("subject", Long.class).notNull()
        .column("predicate", Long.class).notNull()
        .column("object", Long.class).notNull()
        .foreignKey("FK_MODEL","model").references("symbol","id")
        .foreignKey("FK_SUBJECT","subject").references("symbol","id")
        .foreignKey("FK_PREDICATE","predicate").references("symbol","id")
        .foreignKey("FK_OBJECT","object").references("symbol","id")
        .execute();
        
        Statement stmt = conn.createStatement();
        try{
            stmt.execute("select id, text from language");
            stmt.execute("select id, lexical, datatype, lang, integer, floating, datetime from symbol");
            stmt.execute("select model, subject, predicate, object from statement");
        }finally{
            stmt.close();
        }
    }

}
