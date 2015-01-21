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
package com.querydsl.sql.codegen.ant;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AntMetaDataExporterTest {

//    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();
    private final String url = "jdbc:h2:~/dbs/h2" + System.currentTimeMillis();

    @Before
    public void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection(url, "sa", "");
        try {
          Statement stmt = conn.createStatement();
          try {
              stmt.execute("create table test (id int)");    
          } finally {
              stmt.close();
          }                           
        } finally {
            conn.close();  
        }
    }
    
    @Test
    public void Execute() {
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriverClass("org.h2.Driver");
        exporter.setDbUserName("sa");
        exporter.setDbUrl(url);
        exporter.setTargetPackage("test");
        exporter.setTargetSourceFolder("target/AntMetaDataExporterTest");
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest").exists());
    }
    
    @Test
    public void Execute_With_Beans() {
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriverClass("org.h2.Driver");
        exporter.setDbUserName("sa");
        exporter.setDbUrl(url);
        exporter.setTargetPackage("test");
        exporter.setTargetSourceFolder("target/AntMetaDataExporterTest2");
        exporter.setExportBeans(true);
        exporter.setNamePrefix("");
        exporter.setNameSuffix("Q");
        exporter.setBeanPrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest2").exists());
    }


    @Test
    public void Execute_With_Import() {
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriverClass("org.h2.Driver");
        exporter.setDbUserName("sa");
        exporter.setDbUrl(url);
        exporter.setTargetPackage("test");
        exporter.setTargetSourceFolder("target/AntMetaDataExporterTest3");
        exporter.setExportBeans(true);
        exporter.setNamePrefix("");
        exporter.setNameSuffix("Q");
        exporter.setBeanPrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.setImports(new String[]{"com.pck1" , "com.pck2" , "com.Q1" , "com.Q2"});
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest3").exists());
    }
}
