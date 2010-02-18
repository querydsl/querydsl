package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
    public void test() throws IOException{
        QSURVEY survey = new QSURVEY("survey");
        SQLQueryImpl query = new SQLQueryImpl(connection, new DerbyTemplates());
        query.from(survey);
        
        query.count();
        assertProjectionEmpty(query);
        query.countDistinct();
        assertProjectionEmpty(query);
        
        query.iterate(survey.id);
        assertProjectionEmpty(query);
        query.iterate(survey.id,survey.name);
        assertProjectionEmpty(query);
        query.iterateDistinct(survey.id);
        assertProjectionEmpty(query);
        query.iterateDistinct(survey.id,survey.name);
        assertProjectionEmpty(query);
        
        query.list(survey.id);
        assertProjectionEmpty(query);
        query.list(survey.id,survey.id);
        assertProjectionEmpty(query);
        query.listDistinct(survey.id);
        assertProjectionEmpty(query);
        query.listDistinct(survey.id,survey.name);
        assertProjectionEmpty(query);
        
        query.listResults(survey.id);
        assertProjectionEmpty(query);
        query.listDistinctResults(survey.id);
        assertProjectionEmpty(query);
        
        query.map(survey.name, survey.id);
        assertProjectionEmpty(query);
        
        query.uniqueResult(survey.id);
        assertProjectionEmpty(query);
        query.uniqueResult(survey.id,survey.name);
        assertProjectionEmpty(query);
        
    }

    private void assertProjectionEmpty(SQLQueryImpl query) throws IOException {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
