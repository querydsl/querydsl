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
package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.domain.Cat;

public class GenericExporterTest {

    private GenericExporter exporter;

    @Before
    public void setUp() {
        exporter = new GenericExporter();
    }
    
    @Test
    public void Keywords() throws IOException {
        exporter.setKeywords(Keywords.JPA);
        exporter.setTargetFolder(new File("target/gen1-jpa"));
        exporter.export(getClass().getPackage());
        String str = FileUtils.readFileToString(new File("target/gen1-jpa/com/mysema/query/codegen/QGroup.java"));
        assertTrue(str.contains("QGroup group = new QGroup(\"group1\");"));
    }
    
    @Test
    public void Export() {        
        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/sub/QExampleEntity2.java").exists());
    }
    
    @Test
    public void OverrideSerializer() {
        exporter.setTargetFolder(new File("target/gen2"));
        exporter.setSerializerClass(EntitySerializer.class);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen2/com/mysema/query/codegen/sub/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_Package_as_String() {        
        exporter.setTargetFolder(new File("target/gen3"));
        exporter.export(getClass().getPackage().getName());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen3/com/mysema/query/codegen/sub/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_With_Package_Suffix() {        
        exporter.setTargetFolder(new File("target/gen4"));
        exporter.setPackageSuffix("types");
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen4/com/mysema/query/codegentypes/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen4/com/mysema/query/codegentypes/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen4/com/mysema/query/codegentypes/QExampleEntity.java").exists());
        assertTrue(new File("target/gen4/com/mysema/query/codegentypes/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen4/com/mysema/query/codegentypes/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen4/com/mysema/query/codegen/subtypes/QExampleEntity2.java").exists());
    }
    
    @Test
    public void Export_Handle_No_Methods_Nor_Fields() {        
        exporter.setTargetFolder(new File("target/gen5"));
        exporter.setHandleFields(false);
        exporter.setHandleMethods(false);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen5/com/mysema/query/codegen/QExampleEmbeddable.java").exists());
    }
    
    @Test
    public void Export_Domain_Package() {
        exporter.setTargetFolder(new File("target/gen6"));
        exporter.export(Cat.class.getPackage());
    }


}
