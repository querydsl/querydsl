package com.querydsl.sql;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.testutil.Benchmark;
import com.querydsl.core.testutil.Performance;
import com.querydsl.core.testutil.Runner;

@Category(Performance.class)
public class QueryPerformanceTest {
    
    private static final String QUERY = "select COMPANIES.NAME\n" +
    		"from COMPANIES COMPANIES\n" +
    		"where COMPANIES.ID = ?";
    
    private static final SQLTemplates templates = new H2Templates();
    
    private static final Configuration conf = new Configuration(templates);
    
    private final Connection conn = Connections.getConnection();
    
    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException {
        Connections.initH2();        
        Connection conn = Connections.getConnection();               
        Statement stmt = conn.createStatement();
        stmt.execute("create or replace table companies (id identity, name varchar(30) unique not null);");
        
        PreparedStatement pstmt = conn.prepareStatement("insert into companies (name) values (?)");
        final int iterations = 1000000;
        for (int i = 0; i < iterations; i++) {
            pstmt.setString(1, String.valueOf(i));
            pstmt.execute();
            pstmt.clearParameters();
        }        
        pstmt.close();
        stmt.close();
        
        conn.setAutoCommit(false);
    }
    
    @AfterClass
    public static void tearDownClass() throws SQLException {
        Connection conn = Connections.getConnection();        
        Statement stmt = conn.createStatement();
        stmt.execute("drop table companies");
        stmt.close();
        Connections.close();    
    }
    
    
    @Test
    public void JDBC() throws Exception {
        Runner.run("jdbc by id", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    PreparedStatement stmt = conn.prepareStatement(QUERY);
                    try {
                        stmt.setLong(1, i);
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
            }            
        });                      
    }
        
    @Test
    public void JDBC2() throws Exception {
        Runner.run("jdbc by name", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    PreparedStatement stmt = conn.prepareStatement(QUERY);                                                           
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
            }
        });               
    }
        
    @Test
    public void Querydsl1() throws Exception {
        Runner.run("qdsl by id", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    query.from(companies).where(companies.id.eq((long)i))
                        .list(companies.name);            
                }                
            }            
        });    
    }
    
    @Test
    public void Querydsl12() throws Exception {
        Runner.run("qdsl by id (iterated)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    CloseableIterator<String> it = query.from(companies)
                            .where(companies.id.eq((long)i)).iterate(companies.name);
                    try {
                        while (it.hasNext()) {
                            it.next();
                        }    
                    } finally {
                        it.close();
                    }                       
                }
            }            
        });
    }
    
    @Test
    public void Querydsl13() throws Exception {
        Runner.run("qdsl by id (result set access)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    ResultSet rs = query.from(companies)
                            .where(companies.id.eq((long)i)).getResults(companies.name);          
                    try {
                        while (rs.next()) {
                            rs.getString(1);
                        }
                    } finally {
                        rs.close();
                    }                 
                }
            }            
        });
    }
    
    @Test
    public void Querydsl14() throws Exception {
        Runner.run("qdsl by id (no validation)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf, new DefaultQueryMetadata().noValidate());
                    query.from(companies).where(companies.id.eq((long)i))
                        .list(companies.name);            
                }                
            }            
        });    
    }
        
    @Test
    public void Querydsl15() throws Exception {
        Runner.run("qdsl by id (two cols)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    query.from(companies).where(companies.id.eq((long)i))
                        .list(companies.id, companies.name);            
                }                
            }            
        });            
    }
    
    @Test
    public void Querydsl2() throws Exception {
        Runner.run("qdsl by name", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    query.from(companies).where(companies.name.eq(String.valueOf(i)))
                        .list(companies.name);            
                }                
            }            
        });    
    }
    
    @Test
    public void Querydsl22() throws Exception {
        Runner.run("qdsl by name (iterated)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf);
                    CloseableIterator<String> it = query.from(companies)
                            .where(companies.name.eq(String.valueOf(i)))
                            .iterate(companies.name);
                    try {
                        while (it.hasNext()) {
                            it.next();
                        }    
                    } finally {
                        it.close();
                    }                       
                }
            }            
        });
    }
    
    @Test
    public void Querydsl23() throws Exception {
        Runner.run("qdsl by name (no validation)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    QCompanies companies = QCompanies.companies;
                    SQLQuery query = new SQLQuery(conn, conf, new DefaultQueryMetadata().noValidate());
                    query.from(companies)
                        .where(companies.name.eq(String.valueOf(i)))
                        .list(companies.name);            
                }                
            }            
        });    
    }
    
    @Test
    public void Serialization() throws Exception {
        QCompanies companies = QCompanies.companies;
        final QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, companies);
        md.addWhere(companies.id.eq(1l));
        md.addProjection(companies.name);
        
        Runner.run("ser1", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    SQLSerializer serializer = new SQLSerializer(conf);
                    serializer.serialize(md, false);
                    serializer.getConstants();
                    serializer.getConstantPaths();
                    assertNotNull(serializer.toString());     
                }                
            }            
        });   
    }
    
    @Test
    public void Serialization2() throws Exception {
        QCompanies companies = QCompanies.companies;
        final QueryMetadata md = new DefaultQueryMetadata();
        md.addJoin(JoinType.DEFAULT, companies);
        md.addWhere(companies.id.eq(1l));
        md.addProjection(companies.name);
        
        Runner.run("ser2 (non normalized)", new Benchmark() {
            @Override
            public void run(int times) throws Exception {
                for (int i = 0; i < times; i++) {            
                    SQLSerializer serializer = new SQLSerializer(conf);
                    serializer.setNormalize(false);
                    serializer.serialize(md, false);
                    serializer.getConstants();
                    serializer.getConstantPaths();
                    assertNotNull(serializer.toString());     
                }       
            }
        });    
    }

}
