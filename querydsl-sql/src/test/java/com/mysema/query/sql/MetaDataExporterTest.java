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
        test("Q", defaultNaming, "target/1", false, false);
        test("Q", defaultNaming, "target/11", true, false);
    }

    @Test
    public void NormalSettings_with_InnerClasses() throws SQLException{
        test("Q", defaultNaming, "target/1_with_InnerClasses", false, true);
        test("Q", defaultNaming, "target/11_with_InnerClasses", true, true);
    }

    @Test
    public void WithoutPrefix() throws SQLException{
        test("", defaultNaming, "target/2", false, false);
    }

    @Test
    public void WithoutPrefix_with_InnerClasses() throws SQLException{
        test("", defaultNaming, "target/2_with_InnerClasses", false, true);
    }

    @Test
    public void WithLongPrefix() throws SQLException{
        test("QDSL", defaultNaming, "target/3",false, false);
    }

    @Test
    public void WithLongPrefix_with_InnerClasses() throws SQLException{
        test("QDSL", defaultNaming, "target/3_with_InnerClasses",false, true);
    }

    @Test
    public void WithDifferentNamingStrategy() throws SQLException{
        test("Q", originalNaming, "target/4",false, false);
    }

    @Test
    public void WithDifferentNamingStrategy_with_InnerClasses() throws SQLException{
        test("Q", originalNaming, "target/4_with_InnerClasses",false, true);
    }

    @Test
    public void WithoutPrefix2() throws SQLException{
        test("", originalNaming, "target/5", false, false);
    }

    @Test
    public void WithoutPrefix2_with_InnerClasses() throws SQLException{
        test("", originalNaming, "target/5_with_InnerClasses", false, true);
    }

    @Test
    public void WithLongPrefix2() throws SQLException{
        test("QDSL", originalNaming, "target/6", false, false);
    }

    @Test
    public void WithLongPrefix2_with_InnerClasses() throws SQLException{
        test("QDSL", originalNaming, "target/6_with_InnerClasses", false, true);
    }


    @Test
    public void Explicit_Configuration() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix("Q");
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/7"));
        exporter.setNamingStrategy(new DefaultNamingStrategy());
        exporter.setBeanSerializer(new BeanSerializer());
        exporter.export(connection.getMetaData());
    }

    @Test
    public void Minimal_Configuration() throws SQLException{
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/8"));
        exporter.export(connection.getMetaData());
    }

    private void test(String namePrefix, NamingStrategy namingStrategy, String target, boolean withBeans, boolean withInnerClasses) throws SQLException{
        statement.execute("drop table employee if exists");

        // reserved words
        statement.execute("drop table reserved if exists");
        statement.execute("create table reserved (id int, while int)");

        // default instance clash
        statement.execute("drop table definstance if exists");
        statement.execute("create table definstance (id int, definstance int, definstance1 int)");
        
        // class with pk and fk classes
        statement.execute("drop table pkfk if exists");
        statement.execute("create table pkfk (id int primary key, pk int, fk int)");

        // camel case
        statement.execute("drop table \"camelCase\" if exists");
        statement.execute("create table \"camelCase\" (id int)");

        statement.execute("drop table \"vwServiceName\" if exists");
        statement.execute("create table \"vwServiceName\" (id int)");

        // simple types
        statement.execute("drop table date_test if exists");
        statement.execute("create table date_test (d date)");

        statement.execute("drop table date_time_test if exists");
        statement.execute("create table date_time_test (dt datetime)");

        // complex type
//        statement.execute("drop table employee if exists");

        statement.execute("drop table survey if exists");
        statement.execute("create table survey (id int, name varchar(30))");

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

        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy, withInnerClasses);
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File(target));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSerializer(serializer);
        if (withBeans){
            exporter.setBeanSerializer(new BeanSerializer());
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
