/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.util.SimpleCompiler;


/**
 * MetaDataExporterTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporterTest {

    private Connection conn;
    
    private Statement stmt;
    
    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        Class.forName("org.hsqldb.jdbcDriver");
        String url = "jdbc:hsqldb:data/tutorial";
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
    public void testGeneration() throws Exception {
        NamingStrategy defaultNaming = new DefaultNamingStrategy();
        NamingStrategy originalNaming = new OriginalNamingStrategy();
        
        // TODO : test for name conflicts
        
        // normal settings
        test("Q", defaultNaming, "target/1");
        
        // without prefix
        test("", defaultNaming, "target/2");
        
        // with long prefix
        test("QDSL", defaultNaming, "target/3");
        
        // with different namingStrategy
        test("Q", originalNaming, "target/4");
        
        // without prefix
        test("", originalNaming, "target/5");
        
        // with long prefix
        test("QDSL", originalNaming, "target/6");
    }
    
    private void test(String namePrefix, NamingStrategy namingStrategy, String target) throws SQLException{
        stmt.execute("drop table survey if exists");
        stmt.execute("create table survey (id int,name varchar(30));");               
        MetaDataExporter exporter = new MetaDataExporter(namePrefix, "test", null, null, target, namingStrategy);
        exporter.export(conn.getMetaData());   
        
        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }
    

}
