/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.ddl.CreateTableClause;

public abstract class CreateTableBaseTest extends AbstractBaseTest{
    
    private Connection conn;
    
    private Statement stmt;
    
    private static void safeExecute(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }
    
    @Before
    public void setUp() throws SQLException{
        conn = Connections.getConnection();
        stmt = conn.createStatement();
        for (String table : Arrays.asList("statement","symbol","language","autoinc", "primitives")){
            safeExecute(stmt, "drop table " + table);
        }                   
    }
    
    @After
    public void tearDown() throws SQLException{
        if (stmt != null){
            stmt.close();
        }
    }
    
    @Test
    public void AutoIncrement(){
        createTable("autoinc")
          .column("id", Integer.class).notNull().autoIncrement()
          //.primaryKey("PK_AUTOINC","id")
          .execute();   
    }
    
    @Test
    public void PrimitiveTypes(){
        createTable("primitives")
            .column("id", int.class)
            .execute();
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void Unknown_Types_Throws_IllegalArgumentException(){
        createTable("primitives")
            .column("id", Void.class)
            .execute();
    }
    
    @Test
    public void RdfExample() throws SQLException{               
        createTable("language")
          .column("id", Integer.class).notNull()
          .column("text", String.class).size(256).notNull()
          .primaryKey("PK_LANGUAGE","id")
          .execute();
        
        createTable("symbol")
          .column("id", Long.class).notNull()
          .column("lexical", String.class).size(1024).notNull()
          .column("datatype", Long.class)
          .column("lang", Integer.class)
          .column("intval",Long.class)
          .column("floatval",Double.class)
          .column("datetimeval",Timestamp.class)
          .primaryKey("PK_SYMBOL","id")
          .foreignKey("FK_LANG","lang").references("language","id")
          .execute();
        
        createTable("statement")
          .column("model", Long.class)
          .column("subject", Long.class).notNull()
          .column("predicate", Long.class).notNull()
          .column("object", Long.class).notNull()
          .foreignKey("FK_MODEL","model").references("symbol","id")
          .foreignKey("FK_SUBJECT","subject").references("symbol","id")
          .foreignKey("FK_PREDICATE","predicate").references("symbol","id")
          .foreignKey("FK_OBJECT","object").references("symbol","id")
          .index("msp", "model","subject","predicate")
          .index("mp", "model","predicate")
          .index("mspo", "model", "subject", "predicate","object").unique()
          .execute();
        
        stmt.execute("select id, text from language");
        stmt.execute("select id, lexical, datatype, lang, intval, floatval, datetimeval from symbol");
        stmt.execute("select model, subject, predicate, object from statement");
    }

    private CreateTableClause createTable(String tableName) {
        return new CreateTableClause(conn, templates, tableName);
    }
    
}
