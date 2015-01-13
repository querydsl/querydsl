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
import java.io.IOException;
import java.sql.SQLException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.sql.AbstractJDBCTest;
import com.querydsl.core.alias.Gender;
import com.querydsl.sql.*;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.types.EnumByNameType;
import com.querydsl.sql.types.StringType;
import com.querydsl.sql.types.UtilDateType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CustomTypesTest extends AbstractJDBCTest{

    private Configuration configuration;

    @Override
    @Before
    public void setUp() throws ClassNotFoundException, SQLException{
        super.setUp();
        // create schema
        statement.execute("drop table person if exists");
        statement.execute("create table person("
            + "id INT, "
            + "firstname VARCHAR(50), "
            + "gender VARCHAR(50), "
            + "securedId VARCHAR(50), "
            + "CONSTRAINT PK_person PRIMARY KEY (id) "
            + ")");

        // create configuration
        configuration = new Configuration(new HSQLDBTemplates());
//        configuration.setJavaType(Types.DATE, java.util.Date.class);
        configuration.register(new UtilDateType());
        configuration.register("PERSON", "SECUREDID", new EncryptedString());
        configuration.register("PERSON", "GENDER",  new EnumByNameType<Gender>(Gender.class));
        configuration.register(new StringType());

    }

    @Test
    public void Export() throws SQLException, IOException{
        // create exporter
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/customExport"));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setConfiguration(configuration);

        // export
        exporter.export(connection.getMetaData());
        String person = Files.toString(new File("target/customExport/test/QPerson.java"), Charsets.UTF_8);
        //System.err.println(person);
        assertTrue(person.contains("createEnum(\"gender\""));
    }

    @Test
    public void Insert_Query_Update() {
        QPerson person = QPerson.person;

        // insert
        SQLInsertClause insert = new SQLInsertClause(connection, configuration, person);
        insert.set(person.id, 10);
        insert.set(person.firstname, "Bob");
        insert.set(person.gender, Gender.MALE);
        assertEquals(1l, insert.execute());

        // querydsl
        SQLQuery query = new SQLQuery(connection, configuration);
        assertEquals(Gender.MALE, query.from(person).where(person.id.eq(10)).uniqueResult(person.gender));

        // update
        SQLUpdateClause update = new SQLUpdateClause(connection, configuration, person);
        update.set(person.gender, Gender.FEMALE);
        update.set(person.firstname, "Jane");
        update.where(person.id.eq(10));
        update.execute();

        // querydsl
        query = new SQLQuery(connection, configuration);
        assertEquals(Gender.FEMALE, query.from(person).where(person.id.eq(10)).uniqueResult(person.gender));
    }

}
