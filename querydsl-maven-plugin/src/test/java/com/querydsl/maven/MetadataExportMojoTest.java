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
package com.querydsl.maven;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.Serializable;
import java.util.Collections;

import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.sql.codegen.ExtendedBeanSerializer;
import com.querydsl.sql.codegen.OriginalNamingStrategy;
import com.querydsl.sql.codegen.support.NumericMapping;
import com.querydsl.sql.codegen.support.RenameMapping;
import com.querydsl.sql.codegen.support.TypeMapping;
import com.querydsl.sql.types.BytesType;
import com.querydsl.sql.types.DateTimeType;
import com.querydsl.sql.types.LocalDateType;
import com.querydsl.sql.types.LocalTimeType;

public class MetadataExportMojoTest {

    private final String url = "jdbc:h2:mem:testdb" + System.currentTimeMillis();

    private final MavenProject project = new MavenProject();

    private final MetadataExportMojo mojo = new MetadataExportMojo();

    @Before
    public void setUp() {
        mojo.setProject(project);
        mojo.setJdbcDriver("org.h2.Driver");
        mojo.setJdbcUrl(url);
        mojo.setJdbcUser("sa");
        mojo.setNamePrefix("Q"); // default value
        mojo.setPackageName("com.example");
    }

    @Test
    public void execute() throws Exception {
        mojo.setTargetFolder("target/export");
        mojo.execute();

        assertEquals(Collections.singletonList("target/export"), project.getCompileSourceRoots());
        assertTrue(new File("target/export").exists());
    }

    @Test
    public void execute_with_customTypes() throws Exception {
        mojo.setTargetFolder("target/export2");
        mojo.setCustomTypes(new String[]{BytesType.class.getName()});
        mojo.execute();

        assertEquals(Collections.singletonList("target/export2"), project.getCompileSourceRoots());
        assertTrue(new File("target/export2").exists());
    }

    @Test
    public void execute_with_jodaTypes() throws Exception {
        mojo.setTargetFolder("target/export3");
        mojo.setCustomTypes(new String[]{LocalDateType.class.getName(), LocalTimeType.class.getName(), DateTimeType.class.getName()});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export3"), project.getCompileSourceRoots());
        assertTrue(new File("target/export3").exists());
    }

    @Test
    public void execute_with_typeMappings() throws Exception {
        mojo.setTargetFolder("target/export4");
        TypeMapping mapping = new TypeMapping();
        mapping.setTable("CATALOGS");
        mapping.setColumn("CATALOG_NAME");
        mapping.setType(Object.class.getName());
        mojo.setTypeMappings(new TypeMapping[]{mapping});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export4"), project.getCompileSourceRoots());
        assertTrue(new File("target/export4").exists());
    }

    @Test
    public void executeWithNumericMappings() throws Exception {
        mojo.setTargetFolder("target/export5");
        NumericMapping mapping = new NumericMapping();
        mapping.setTotal(1);
        mapping.setDecimal(1);
        mapping.setJavaType(Number.class.getName());
        mojo.setNumericMappings(new NumericMapping[]{mapping});

        mojo.execute();

        assertEquals(Collections.singletonList("target/export5"), project.getCompileSourceRoots());
        assertTrue(new File("target/export5").exists());
    }

    @Test
    public void executeWithBeans() throws Exception {
        mojo.setTargetFolder("target/export6");
        mojo.setExportBeans(true);
        mojo.execute();

        assertTrue(new File("target/export6").exists());
    }

    @Test
    @Ignore
    public void executeWithScalaSources() throws Exception {
        mojo.setTargetFolder("target/export7");
        mojo.setCreateScalaSources(true);
        mojo.execute();

        assertTrue(new File("target/export7").exists());
    }

    @Test
    public void executeWithNamingStrategy() throws Exception {
        mojo.setTargetFolder("target/export8");
        mojo.setNamingStrategyClass(OriginalNamingStrategy.class.getName());
        mojo.execute();

        assertTrue(new File("target/export8").exists());
    }

    @Test
    public void executeWithBeans2() throws Exception {
        mojo.setTargetFolder("target/export9");
        mojo.setExportBeans(true);
        mojo.setBeanSerializerClass(ExtendedBeanSerializer.class.getName());
        mojo.execute();

        assertTrue(new File("target/export9").exists());
    }

    @Test
    public void executeWithBeans3() throws Exception {
        mojo.setTargetFolder("target/export10");
        mojo.setExportBeans(true);
        mojo.setBeanInterfaces(new String[]{Serializable.class.getName()});
        mojo.execute();

        assertTrue(new File("target/export10").exists());
    }

    @Test
    public void executeWithImport1() throws Exception {
        mojo.setTargetFolder("target/export11");
        mojo.setImports(new String[]{"com.pck1" , "com.pck2" , "com.Q1" , "com.Q2"});
        mojo.execute();

        assertTrue(new File("target/export11").exists());
    }

    @Test
    public void executeWithImportAndBeans1() throws Exception {
        mojo.setTargetFolder("target/export12");
        mojo.setImports(new String[]{"com.pck1" , "com.pck2" , "com.Q1" , "com.Q2"});
        mojo.setExportBeans(true);
        mojo.execute();

        assertTrue(new File("target/export12").exists());
    }

    @Test
    public void executeWithRenames() throws Exception {
        RenameMapping mapping = new RenameMapping();
        mapping.setFromSchema("ABC");
        mapping.setToSchema("DEF");

        mojo.setTargetFolder("target/export13");
        mojo.setRenameMappings(new RenameMapping[]{mapping});
        mojo.execute();

        assertEquals(Collections.singletonList("target/export13"), project.getCompileSourceRoots());
        assertTrue(new File("target/export13").exists());
    }
}
