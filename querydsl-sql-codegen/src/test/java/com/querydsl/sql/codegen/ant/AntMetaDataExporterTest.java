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
package com.querydsl.sql.codegen.ant;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.BeforeClass;
import org.junit.Test;

public class AntMetaDataExporterTest {

    private static final String url = "jdbc:h2:./target/dbs/h2_AntMetaDataExporterTest";

    @BeforeClass
    public static void setUp() throws SQLException {
        Connection conn = DriverManager.getConnection(url, "sa", "");
        try {
          Statement stmt = conn.createStatement();
          try {
              stmt.execute("drop table test if exists");
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
        exporter.setJdbcDriver("org.h2.Driver");
        exporter.setJdbcUser("sa");
        exporter.setJdbcUrl(url);
        exporter.setPackageName("test");
        exporter.setTargetFolder("target/AntMetaDataExporterTest");
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest").exists());
        assertTrue(new File("target/AntMetaDataExporterTest/test/QTest.java").exists());
    }

    @Test
    public void Execute_With_Beans() {
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriver("org.h2.Driver");
        exporter.setJdbcUser("sa");
        exporter.setJdbcUrl(url);
        exporter.setPackageName("test");
        exporter.setTargetFolder("target/AntMetaDataExporterTest2");
        exporter.setExportBeans(true);
        exporter.setNamePrefix("Q");
        exporter.setNameSuffix("");
        exporter.setBeanPrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest2").exists());
        assertTrue(new File("target/AntMetaDataExporterTest2/test/QTest.java").exists());
        assertTrue(new File("target/AntMetaDataExporterTest2/test/TestBean.java").exists());
    }


    @Test
    public void Execute_With_Import() {
        AntMetaDataExporter exporter = new AntMetaDataExporter();
        exporter.setJdbcDriver("org.h2.Driver");
        exporter.setJdbcUser("sa");
        exporter.setJdbcUrl(url);
        exporter.setPackageName("test");
        exporter.setTargetFolder("target/AntMetaDataExporterTest3");
        exporter.setExportBeans(true);
        exporter.setNamePrefix("Q");
        exporter.setNameSuffix("");
        exporter.setBeanPrefix("");
        exporter.setBeanSuffix("Bean");
        exporter.setImports(new String[]{"com.pck1" , "com.pck2" , "com.Q1" , "com.Q2"});
        exporter.execute();

        assertTrue(new File("target/AntMetaDataExporterTest3").exists());
        assertTrue(new File("target/AntMetaDataExporterTest3/test/QTest.java").exists());
        assertTrue(new File("target/AntMetaDataExporterTest3/test/TestBean.java").exists());
    }

    @Test
    public void Execute_inside_Ant() {
        File buildFile = new File(getClass().getResource("/build.xml").getFile());
        Project p = new Project();
        p.setUserProperty("ant.file", buildFile.getAbsolutePath());
        p.init();
        ProjectHelper helper = ProjectHelper.getProjectHelper();
        p.addReference("ant.projectHelper", helper);
        helper.parse(p, buildFile);
        p.executeTarget(p.getDefaultTarget());

        assertTrue(new File("target/AntMetaDataExporterTest4").exists());
        assertTrue(new File("target/AntMetaDataExporterTest4/test/QTest.java").exists());
    }
}
