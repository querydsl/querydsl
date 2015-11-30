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
    public void export() {
        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen1/com/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen1/com/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen1/com/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen1/com/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen1/com/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen1/com/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

    @Test
    public void export_with_keywords() throws IOException {
        exporter.setKeywords(Keywords.JPA);
        exporter.setTargetFolder(new File("target/gen1_jpa"));
        exporter.export(getClass().getPackage());
        String str = Files.toString(new File("target/gen1_jpa/com/querydsl/codegen/QGroup.java"), Charsets.UTF_8);
        assertTrue(str.contains("QGroup group = new QGroup(\"group1\");"));
    }

    @Test
    public void export_with_stopClass() {
        exporter.setTargetFolder(new File("target/gen1_stop"));
        exporter.addStopClass(Examples.Supertype.class);
        exporter.export(getClass().getPackage());
        assertFalse(new File("target/gen1_stop/com/querydsl/codegen/QExamples_Supertype.java").exists());
    }

    @Test
    public void override_serializer() {
        exporter.setTargetFolder(new File("target/gen2"));
        exporter.setSerializerClass(EntitySerializer.class);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen2/com/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen2/com/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen2/com/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen2/com/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen2/com/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen2/com/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

    @Test
    public void export_package_as_string() {
        exporter.setTargetFolder(new File("target/gen3"));
        exporter.export(getClass().getPackage().getName());
        assertTrue(new File("target/gen3/com/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen3/com/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen3/com/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen3/com/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen3/com/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen3/com/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

    @Test
    public void export_with_package_suffix() {
        exporter.setTargetFolder(new File("target/gen4"));
        exporter.setPackageSuffix("types");
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen4/com/querydsl/codegentypes/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen4/com/querydsl/codegentypes/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen4/com/querydsl/codegentypes/QExampleEntity.java").exists());
        assertTrue(new File("target/gen4/com/querydsl/codegentypes/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen4/com/querydsl/codegentypes/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen4/com/querydsl/codegen/subtypes/QExampleEntity2.java").exists());
    }

    @Test
    public void export_handle_no_methods_nor_fields() {
        exporter.setTargetFolder(new File("target/gen5"));
        exporter.setHandleFields(false);
        exporter.setHandleMethods(false);
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen5/com/querydsl/codegen/QExampleEmbeddable.java").exists());
    }

    @Test
    public void export_domain_package() {
        exporter.setTargetFolder(new File("target/gen6"));
        exporter.export(Cat.class.getPackage());
    }

    @Test
    public void export_serializerConfig() {
        exporter.setTargetFolder(new File("target/gen7"));
        exporter.setSerializerConfig(new SimpleSerializerConfig(true, true, true, true, ""));
        exporter.export(getClass().getPackage());
        assertTrue(new File("target/gen7/com/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen7/com/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen7/com/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen7/com/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen7/com/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen7/com/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

    @Test
    public void export_useFieldTypes() {
        exporter.setTargetFolder(new File("target/gen8"));
        exporter.export(getClass().getPackage());
        exporter.setUseFieldTypes(true);
        assertTrue(new File("target/gen8/com/querydsl/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen8/com/querydsl/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen8/com/querydsl/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen8/com/querydsl/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen8/com/querydsl/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen8/com/querydsl/codegen/sub/QExampleEntity2.java").exists());
    }

}
