package com.mysema.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

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
        safeExecute(stmt,"drop table statement");
        safeExecute(stmt,"drop table symbol");
        safeExecute(stmt,"drop table language");                   
    }
    
    @After
    public void tearDown() throws SQLException{
        if (stmt != null){
            stmt.close();
        }
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
        
        stmt.execute("select id, text from language");
        stmt.execute("select id, lexical, datatype, lang, integer, floating, datetime from symbol");
        stmt.execute("select model, subject, predicate, object from statement");
    }

}
