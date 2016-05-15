/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import javax.tools.JavaCompiler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.mysema.codegen.SimpleCompiler;
import com.querydsl.codegen.BeanSerializer;
import com.querydsl.sql.AbstractJDBCTest;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.types.AbstractType;

public class MetaDataSerializerTest extends AbstractJDBCTest {
    public static class CustomNumber { }

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Override
    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        super.setUp();
        statement.execute("drop table employee if exists");
        statement.execute("drop table survey if exists");
        statement.execute("drop table date_test if exists");
        statement.execute("drop table date_time_test if exists");
        statement.execute("drop table spaces if exists");

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
    public void normal_serialization() throws SQLException {
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setBeanSerializerClass(BeanSerializer.class);
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(folder.getRoot());
        exporter.setNamingStrategy(namingStrategy);
        exporter.export(connection.getMetaData());

        compile(exporter);
    }

    @Test
    public void customized_serialization() throws SQLException {
        String namePrefix = "Q";
        Configuration conf = new Configuration(SQLTemplates.DEFAULT);
        conf.register("EMPLOYEE", "ID", new AbstractType<CustomNumber>(0) {
            @Override
            public Class<CustomNumber> getReturnedClass() {
                return CustomNumber.class;
            }

            @Override
            public CustomNumber getValue(ResultSet rs, int startIndex) throws SQLException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setValue(PreparedStatement st, int startIndex, CustomNumber value) throws SQLException {
                throw new UnsupportedOperationException();
            }
        });
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        // customization of serialization
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setBeanSerializerClass(BeanSerializer.class);
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(folder.getRoot());
        exporter.setNamingStrategy(namingStrategy);
        exporter.setConfiguration(conf);
        exporter.export(connection.getMetaData());

        compile(exporter);
    }

    private void compile(MetaDataExporter exporter) {
        JavaCompiler compiler = new SimpleCompiler();
        Set<String> classes = exporter.getClasses();
        int compilationResult = compiler.run(null, null, null, classes.toArray(new String[classes.size()]));
        if (compilationResult == 0) {
            System.out.println("Compilation is successful");
        } else {
            Assert.fail("Compilation Failed");
        }
    }

}
