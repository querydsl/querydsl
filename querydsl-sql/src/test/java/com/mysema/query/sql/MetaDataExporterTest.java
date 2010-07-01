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

/**
 * MetaDataExporterTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MetaDataExporterTest extends AbstractJDBCTest{

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
        statement.execute("drop table employee if exists");

        statement.execute("drop table survey if exists");
        statement.execute("create table survey (id int, name varchar(30))");

        statement.execute("drop table date_test if exists");
        statement.execute("create table date_test (d date)");

        statement.execute("drop table date_time_test if exists");
        statement.execute("create table date_time_test (dt datetime)");

        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy);
        MetaDataExporter exporter = new MetaDataExporter(namePrefix, "test", null, null, new File(target), namingStrategy, serializer);
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
