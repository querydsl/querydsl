/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.AbstractJDBCTest;
import com.mysema.query.alias.AliasTest.Gender;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.sql.types.EnumByNameType;
import com.mysema.query.sql.types.StringType;
import com.mysema.query.sql.types.UtilDateType;

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
        configuration.register("person", "secureId", new EncryptedString());
        configuration.register("person", "gender",  new EnumByNameType<Gender>(Gender.class));
        configuration.register(new StringType());

    }

    @Test
    public void Export() throws SQLException, IOException{
        // create exporter
        String namePrefix = "Q";
        NamingStrategy namingStrategy = new DefaultNamingStrategy();
        MetaDataSerializer serializer = new MetaDataSerializer(namePrefix, namingStrategy);
        MetaDataExporter exporter = new MetaDataExporter();
        exporter.setNamePrefix(namePrefix);
        exporter.setPackageName("test");
        exporter.setTargetFolder(new File("target/customExport"));
        exporter.setNamingStrategy(namingStrategy);
        exporter.setSerializer(serializer);
        exporter.setConfiguration(configuration);
        
        // export
        exporter.export(connection.getMetaData());
        String person = FileUtils.readFileToString(new File("target/customExport/test/QPerson.java"));
        assertTrue(person.contains("createEnum(\"GENDER\""));              
    }
    
    @Test
    public void Insert_Query_Update(){
        QPerson person = QPerson.person;
        
        // insert
        SQLInsertClause insert = new SQLInsertClause(connection, configuration, person);
        insert.set(person.id, 10);
        insert.set(person.firstname, "Bob");
        insert.set(person.gender, Gender.MALE);
        assertEquals(1l, insert.execute());
        
        // query
        SQLQuery query = new SQLQueryImpl(connection, configuration);
        assertEquals(Gender.MALE, query.from(person).where(person.id.eq(10)).uniqueResult(person.gender));
        
        // update
        SQLUpdateClause update = new SQLUpdateClause(connection, configuration, person);
        update.set(person.gender, Gender.FEMALE);
        update.set(person.firstname, "Jane");
        update.where(person.id.eq(10));
        update.execute();
        
        // query
        query = new SQLQueryImpl(connection, configuration);
        assertEquals(Gender.FEMALE, query.from(person).where(person.id.eq(10)).uniqueResult(person.gender));
    }
    
}
