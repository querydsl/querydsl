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
package com.querydsl.codegen;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.querydsl.core.domain.Cat;

public class GenericExporterTest {

    private GenericExporter exporter;

    @Before
    public void setUp() {
        exporter = new GenericExporter();
    }
    
    @Test
    public void Export() {        
        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen1/com/mysema/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

    @Test
    public void Export_With_Keywords() throws IOException {
        exporter.setKeywords(Keywords.JPA);
        exporter.setTargetFolder(new File("target/gen1_jpa"));
        exporter.export(getClass().getPackage());
        String str = Files.toString(new File("target/gen1_jpa/com/mysema/querydsl/codegen/QGroup.java"), Charsets.UTF_8);
        assertTrue(str.contains("QGroup group = new QGroup(\"group1\");"));
    }
    
    @Test
    public void Export_With_Stopclass() {        
        exporter.setTargetFolder(new File("target/gen1_stop"));
        exporter.addStopClass(Examples.Supertype.class);
        exporter.export(getClass().getPackage());
        assertFalse(new File("target/gen1_stop/com/mysema/querydsl/codegen/QExamples_Supertype.java").exists());
    }
    
    @Test
    public void OverrideSerializer() {
        exporter.setTargetFolder(new File("target/gen2"));
        exporter.setSerializerClass(EntitySerializer.class);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen2/com/mysema/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_Package_as_String() {        
        exporter.setTargetFolder(new File("target/gen3"));
        exporter.export(getClass().getPackage().getName());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen3/com/mysema/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_With_Package_Suffix() {        
        exporter.setTargetFolder(new File("target/gen4"));
        exporter.setPackageSuffix("types");
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegentypes/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegentypes/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegentypes/QExampleEntity.java").exists());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegentypes/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegentypes/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen4/com/mysema/querydsl/codegen/subtypes/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_Handle_No_Methods_Nor_Fields() {        
        exporter.setTargetFolder(new File("target/gen5"));
        exporter.setHandleFields(false);
        exporter.setHandleMethods(false);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen5/com/mysema/querydsl/codegen/QExampleEmbeddable.java").exists());
    }
    
    @Test
    public void Export_Domain_Package() {
        exporter.setTargetFolder(new File("target/gen6"));
        exporter.export(Cat.class.getPackage());
    }


}
