package com.mysema.query.apt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class BooleanExtensionsTest extends AbstractProcessorTest{

    private static final String packagePath = "src/test/apt/com/mysema/query/";

    @Test
    public void Process() throws IOException{
        List<String> sources = Arrays.asList(
                new File(packagePath, "BooleanExtensions.java").getPath(),
                new File(packagePath, "ExampleEntity.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources,"booleanExtensions");
        String qtypeContent = FileUtils.readFileToString(new File("target/booleanExtensions/com/mysema/query/QExampleEntity.java"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp2"));
    }
    
    @Test
    public void Process2() throws IOException{
        List<String> sources = Arrays.asList(
                new File(packagePath, "BooleanExtensions2.java").getPath(),
                new File(packagePath, "ExampleEntity.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources,"booleanExtensions2");
        String qtypeContent = FileUtils.readFileToString(new File("target/booleanExtensions2/com/mysema/query/QExampleEntity.java"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp"));
        assertTrue(qtypeContent.contains("ext.java.lang.QBoolean booleanProp2"));
    }
    
}
