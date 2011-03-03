/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.AbstractJDBCTest;
import com.mysema.query.sql.DefaultNamingStrategy;


public class KeyDataFactoryTest extends AbstractJDBCTest{
        
    @Test
    public void test() throws SQLException{
        statement.execute("drop table employee if exists");
        statement.execute("drop table survey if exists");
        statement.execute("drop table date_test if exists");
        statement.execute("drop table date_time_test if exists");

        statement.execute("create table survey (id int, name varchar(30), "
                + "CONSTRAINT PK_survey PRIMARY KEY (id, name))");

        statement.execute("create table employee("
                + "id INT, "
                + "superior_id int, "
                + "survey_id int, "
                + "survey_name varchar(30), "
                + "CONSTRAINT PK_employee PRIMARY KEY (id), "
                + "CONSTRAINT FK_survey FOREIGN KEY (survey_id, survey_name) REFERENCES survey(id,name), "
                + "CONSTRAINT FK_superior FOREIGN KEY (superior_id) REFERENCES employee(id))");
        
        KeyDataFactory keyDataFactory = new KeyDataFactory(new DefaultNamingStrategy(), "Q","","test");
        
        DatabaseMetaData md = connection.getMetaData();
        
        // EMPLOYEE
        
        // primary key
        Map<String, PrimaryKeyData> primaryKeys = keyDataFactory.getPrimaryKeys(md, null, "EMPLOYEE");
        assertFalse(primaryKeys.isEmpty());
        // inverse foreign keys
        Map<String, InverseForeignKeyData> exportedKeys = keyDataFactory.getExportedKeys(md, null, "EMPLOYEE");
        assertFalse(exportedKeys.isEmpty());
        assertTrue(exportedKeys.containsKey("FK_SUPERIOR"));
        // foreign keys
        Map<String, ForeignKeyData> importedKeys = keyDataFactory.getImportedKeys(md, null, "EMPLOYEE");
        assertFalse(importedKeys.isEmpty());
        assertTrue(importedKeys.containsKey("FK_SUPERIOR"));
        assertTrue(importedKeys.containsKey("FK_SURVEY"));
        
        // SURVEY
        
        // primary key
        primaryKeys = keyDataFactory.getPrimaryKeys(md, null, "SURVEY");
        assertFalse(primaryKeys.isEmpty());
        // inverse foreign keys
        exportedKeys = keyDataFactory.getExportedKeys(md, null, "SURVEY");
        assertFalse(exportedKeys.isEmpty());
        assertTrue(exportedKeys.containsKey("FK_SURVEY"));
        // foreign keys
        importedKeys = keyDataFactory.getImportedKeys(md, null, "SURVEY");
        assertTrue(importedKeys.isEmpty());
        
    }

}
