package com.mysema.query.codegen;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

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
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEmbeddable.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEmbedded.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEntity.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleEntityInterface.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/QExampleSupertype.java").exists());
        assertTrue(new File("target/gen1/com/mysema/query/codegen/sub/QExampleEntity2.java").exists());
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

}
