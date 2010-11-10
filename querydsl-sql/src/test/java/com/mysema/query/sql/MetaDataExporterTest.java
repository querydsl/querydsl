/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.io.File;
import java.sql.SQLException;
import java.util.Set;

import javax.tools.JavaCompiler;

import junit.framework.Assert;

import org.junit.Test;

import com.mysema.codegen.SimpleCompiler;
import com.mysema.query.AbstractJDBCTest;
import com.mysema.query.codegen.BeanSerializer;

/**
 * MetaDataExporterTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporterTest extends AbstractJDBCTest{

    private static final NamingStrategy defaultNaming = new DefaultNamingStrategy();
    
    private static final NamingStrategy originalNaming = new OriginalNamingStrategy();

    @Test
    public void NormalSettings() throws SQLException{
        test("Q", defaultNaming, "target/1", false);
        test("Q", defaultNaming, "target/11", true);        
    }

    @Test
    public void WithoutPrefix() throws SQLException{
        test("", defaultNaming, "target/2", false);        
    }
    
    @Test
    public void WithLongPrefix() throws SQLException{
        test("QDSL", defaultNaming, "target/3",false);        
    }
    
    @Test
    public void WithDifferentNamingStrategy() throws SQLException{
        test("Q", originalNaming, "target/4",false);
    }
    
    @Test
    public void WithoutPrefix2() throws SQLException{
        test("", originalNaming, "target/5",false);
    }
    
    @Test
    public void WithLongPrefix2() throws SQLException{
        test("QDSL", originalNaming, "target/6",false); 
    }


    private void test(String namePrefix, NamingStrategy namingStrategy, String target, boolean withBeans) throws SQLException{
        // reserved words
        statement.execute("drop table reserved if exists");
        statement.execute("create table reserved (id int, while int)");
        
        // default instance clash
        statement.execute("drop table definstance if exists");
        statement.execute("create table definstance (id int, definstance int, definstance1 int)");
        
        // camel case
        statement.execute("drop table \"camelCase\" if exists");
        statement.execute("create table \"camelCase\" (id int)");
        
        statement.execute("drop table \"vwServiceName\" if exists");
        statement.execute("create table \"vwServiceName\" (id int)");

        // simple types
        statement.execute("drop table survey if exists");
        statement.execute("create table survey (id int, name varchar(30))");

        statement.execute("drop table date_test if exists");
        statement.execute("create table date_test (d date)");

        statement.execute("drop table date_time_test if exists");
        statement.execute("create table date_time_test (dt datetime)");

        // complex type
        statement.execute("drop table employee if exists");
        statement.execute("create table employee("
                + "id INT, "
                + "firstname VARCHAR(50), "
                + "lastname VARCHAR(50), "
                + "salary DECIMAL(10, 2), "
                + "datefield DATE, "
                + "timefield TIME, "
                + "superior_id int, "
                + "survey_id int, "
                + "survey_name varchar(30), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");       

        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy);
        MetaDataExporter exporter;
        if (withBeans){
            exporter = new MetaDataExporter(namePrefix, "test", new File(target), namingStrategy, serializer, new BeanSerializer());
        }else{
            exporter = new MetaDataExporter(namePrefix, "test", new File(target), namingStrategy, serializer);
        }
        exporter.export(connection.getMetaData());

        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, System.out, System.err, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }

}
