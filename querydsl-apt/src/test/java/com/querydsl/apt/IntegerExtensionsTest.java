package com.querydsl.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class IntegerExtensionsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/apt/com/querydsl/";

    @Test
    public void process() throws IOException {
        List<String> sources = Arrays.asList(
                new File(packagePath, "IntegerExtensions.java").getPath(),
                new File(packagePath, "ExampleEntity2.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources, "integerExtensions");
                String qtypeContent = new String(Files.readAllBytes(Paths.get("target", "integerExtensions", "com", "querydsl", "QExampleEntity2.java")), StandardCharsets.UTF_8);
        //The superclass' id property is inherited, but can't be assigned to the custom QInteger
        assertTrue(qtypeContent.contains("public final ext.java.lang.QInteger id = new ext.java.lang.QInteger(_super.id);"));
    }

}
