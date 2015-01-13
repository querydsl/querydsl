/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.codegen;

import java.io.File;
import java.sql.SQLException;
import java.util.Set;

import javax.tools.JavaCompiler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mysema.codegen.SimpleCompiler;
import com.querydsl.sql.AbstractJDBCTest;
import com.querydsl.codegen.BeanSerializer;

public class MetaDataSerializerTest extends AbstractJDBCTest{

    @Override
    @Before
    public void setUp() throws SQLException, ClassNotFoundException{
        super.setUp();
        statement.execute("drop table employee if exists");
        statement.execute("drop table survey if exists");
        statement.execute("drop table date_test if exists");
        statement.execute("drop table date_time_test if exists");

        // survey
        statement.execute("create table survey (id int, name varchar(30), "
                + "CONSTRAINT PK_survey PRIMARY KEY (id, name))");
        
        // date_test
        statement.execute("create table date_test (d date)");
        
        // date_time
        statement.execute("create table date_time_test (dt datetime)");
        
        // spaces
        statement.execute("create table spaces (\"spaces  \n 1\" date)");
        
        // employee
        statement.execute("create table employee("
                + "id INT, "
                + "firstname VARCHAR(50), "
                + "lastname VARCHAR(50), "
                + "salary DECIMAL(10, 2), "
                + "datefield DATE, "
                + "timefield TIME, "
                + "superior_id int, "
                + "survey_id int, "
                + "\"123abc\" int,"
                + "survey_name varchar(30), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_survey FOREIGN KEY (survey_id, survey_name) REFERENCES survey(id,name), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");
    }

    @Test
    public void Normal_serialization() throws SQLException{
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setBeanSerializerClass(BeanSerializer.class);
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/cust1"));
        exporter.setNamingStrategy(namingStrategy);
        exporter.export(connection.getMetaData());

        compile(exporter);
    }

    private void compile(MetaDataExporter exporter) {
        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if(compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            Assert.fail("Compilation Failed");
        }
    }

}
