package com.mysema.query;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.H2Templates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;

@Ignore
public class QueryPerformanceTest {
    
    private static final int iterations = 1000000;
    
    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException {
        Connections.initH2();        
        Connection conn = Connections.getConnection();        
        Statement stmt = conn.createStatement();
        stmt.execute("create table companies (id identity, name varchar(30) unique not null);");        
        for (int i = 0; i < iterations; i++) {
            stmt.execute("insert into companies (name) values ('" + i + "')");
        }        
        stmt.close();
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        Connection conn = Connections.getConnection();        
        Statement stmt = conn.createStatement();
        stmt.execute("drop table companies");
        stmt.close();
        Connections.close();    }
    
    
    @Test
    public void JDBC() throws SQLException {
        Connection conn = Connections.getConnection();        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            PreparedStatement stmt = conn.prepareStatement("select name from companies where id = ?");
            try {
                stmt.setInt(1, i);
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
        System.err.println("jdbc by id " + (System.currentTimeMillis() - start));                    
    }
    
    @Test
    public void JDBC2() throws SQLException {
        Connection conn = Connections.getConnection();        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            PreparedStatement stmt = conn.prepareStatement("select id from companies where name = ?");
            try {
                stmt.setString(1, String.valueOf(i));
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
        System.err.println("jdbc by name " + (System.currentTimeMillis() - start));                    
    }
    
    @Test
    public void Querydsl1() {
        Connection conn = Connections.getConnection();        
        Configuration conf = new Configuration(new H2Templates());
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {            
            QCompanies companies = QCompanies.companies;
            SQLQuery query = new SQLQueryImpl(conn, conf);
            query.from(companies).where(companies.id.eq((long)i)).list(companies.name);            
        }
        System.err.println("qdsl by id " + (System.currentTimeMillis() - start));    
    }
    
    @Test
    public void Querydsl2() {
        Connection conn = Connections.getConnection();        
        Configuration conf = new Configuration(new H2Templates());
        
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {            
            QCompanies companies = QCompanies.companies;
            SQLQuery query = new SQLQueryImpl(conn, conf);
            query.from(companies).where(companies.name.eq(String.valueOf(i))).list(companies.name);            
        }
        System.err.println("qdsl by name " + (System.currentTimeMillis() - start));    
    }
    
    @Test
    public void Serialization() {
        QCompanies companies = QCompanies.companies;
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, companies);
        md.addWhere(companies.id.eq(1l));
        md.addProjection(companies.name);
        
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
        QCompanies companies = QCompanies.companies;
        QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, companies);
        md.addWhere(companies.id.eq(1l));
        md.addProjection(companies.name);
        
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
