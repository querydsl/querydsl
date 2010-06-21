package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

public class GeneratedKeysTest {
    
    @Table("GENERATED_KEYS")
    public static class QGeneratedKeysEntity extends PEntity<QGeneratedKeysEntity>{

        private static final long serialVersionUID = 2002306246819687158L;

        public QGeneratedKeysEntity(String name) {
            super(QGeneratedKeysEntity.class, PathMetadataFactory.forVariable(name));
        }
        
        public final PNumber<java.lang.Integer> id = createNumber("ID", java.lang.Integer.class);
        
        public final PString name = createString("NAME");
        
    }
    
    private Connection conn;
    
    private Statement stmt;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.h2.Driver");
        String url = "jdbc:h2:target/h2";
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
        assertTrue(rs.next());
        assertEquals(1, rs.getInt(1));
        assertFalse(rs.next());
        
        insertClause = new SQLInsertClause(conn, new H2Templates(), entity);
        rs = insertClause.set(entity.name, "World").executeWithKeys();
        assertTrue(rs.next());
        assertEquals(2, rs.getInt(1));
        assertFalse(rs.next());
        
    }

}
