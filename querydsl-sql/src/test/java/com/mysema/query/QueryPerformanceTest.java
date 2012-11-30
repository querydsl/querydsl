package com.mysema.query;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.domain.QSurvey;

@Ignore
public class QueryPerformanceTest {
    
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        Connections.initH2();
    }
    
    @After
    public void tearDown() throws SQLException {
        Connections.close();
    }
    
    private int iterations = 1000000;
    
    @Test
    public void JDBC() throws SQLException {
        Connection conn = Connections.getConnection();        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            PreparedStatement stmt = conn.prepareStatement("select name from survey where id = ?");
            try {
                stmt.setInt(1, 1);
                ResultSet rs = stmt.executeQuery();                
                try {
                    while (rs.next()) {
                        rs.getString(1);
                    }
                } finally {
                    rs.close();
                }
                
            } finally {
                stmt.close();
            }            
        }
        System.err.println("jdbc " + (System.currentTimeMillis() - start));    
                
    }
    
    @Test
    public void Querydsl() {
        Connection conn = Connections.getConnection();        
        Configuration conf = new Configuration(new H2Templates());
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {            
            QSurvey survey = QSurvey.survey;
            SQLQuery query = new SQLQueryImpl(conn, conf);
            query.from(survey).where(survey.id.eq(1)).list(survey.name);            
        }
        System.err.println("qdsl " + (System.currentTimeMillis() - start));    
    }
    
    @Test
    public void Serialization() {
        QSurvey survey = QSurvey.survey;
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, survey);
        md.addWhere(survey.id.eq(1));
        md.addProjection(survey.name);
        
        SQLTemplates templates = new H2Templates();
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {            
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());     
        }
        System.err.println("ser1 " + (System.currentTimeMillis() - start));    
    }
    
    @Test
    public void Serialization2() {
        QSurvey survey = QSurvey.survey;
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, survey);
        md.addWhere(survey.id.eq(1));
        md.addProjection(survey.name);
        
        SQLTemplates templates = new H2Templates();
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {            
            SQLSerializer serializer = new SQLSerializer(templates);
            serializer.setNormalize(false);
            serializer.serialize(md, false);
            serializer.getConstants();
            serializer.getConstantPaths();
            assertNotNull(serializer.toString());     
        }
        System.err.println("ser2 " + (System.currentTimeMillis() - start));    
    }

}
