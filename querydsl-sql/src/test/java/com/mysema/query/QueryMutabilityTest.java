package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.sql.DerbyTemplates;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.domain.QSURVEY;

public class QueryMutabilityTest{
    
    private Connection connection;
    
    private static Connection getDerbyConnection() throws Exception {
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        String url = "jdbc:derby:target/demoDB;create=true";
        return DriverManager.getConnection(url, "", "");
    }
    
    private static void safeExecute(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // do nothing
        }
    }
    
    @Before
    public void setUp() throws Exception{
        connection = getDerbyConnection();
        Statement stmt = connection.createStatement();
        safeExecute(stmt, "drop table survey");
        stmt.execute("create table survey (id int,name varchar(30))");
    }
    
    @After
    public void tearDown() throws SQLException{
        if (connection != null) connection.close();
    }
    
    @Test
    public void test() throws IOException, SecurityException,
            IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        QSURVEY survey = new QSURVEY("survey");
        SQLQueryImpl query = new SQLQueryImpl(connection, new DerbyTemplates());
        query.from(survey);
        Mutability.test(query, survey.id, survey.name);
    }
    
    @Test
    public void testClone(){
        QSURVEY survey = new QSURVEY("survey");
        SQLQueryImpl query = new SQLQueryImpl(new DerbyTemplates()).from(survey);        
        SQLQueryImpl query2 = query.clone(connection);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(survey.id);
    }


}
