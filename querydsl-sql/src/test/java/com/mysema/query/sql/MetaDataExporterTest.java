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

    @Test
    public void Generation() throws Exception {
        NamingStrategy defaultNaming = new DefaultNamingStrategy();
        NamingStrategy originalNaming = new OriginalNamingStrategy();

        // TODO : test for name conflicts

        // normal settings
        test("Q", defaultNaming, "target/1", false);
        test("Q", defaultNaming, "target/11", true);

        // without prefix
        test("", defaultNaming, "target/2", false);

        // with long prefix
        test("QDSL", defaultNaming, "target/3",false);

        // with different namingStrategy
        test("Q", originalNaming, "target/4",false);

        // without prefix
        test("", originalNaming, "target/5",false);

        // with long prefix
        test("QDSL", originalNaming, "target/6",false);
    }

    private void test(String namePrefix, NamingStrategy namingStrategy, String target, boolean withBeans) throws SQLException{
        statement.execute("drop table employee if exists");

        statement.execute("drop table survey if exists");
        statement.execute("create table survey (id int, name varchar(30))");

        statement.execute("drop table date_test if exists");
        statement.execute("create table date_test (d date)");

        statement.execute("drop table date_time_test if exists");
        statement.execute("create table date_time_test (dt datetime)");
        
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
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0){
            System.out.println("Compilation is successful");
        }else{
            Assert.fail("Compilation Failed");
        }
    }

}
