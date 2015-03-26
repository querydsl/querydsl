package com.querydsl.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class IntegerExtensionsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/apt/com/querydsl/";

    @Test
    public void Process() throws IOException {
        List<String> sources = Arrays.asList(
                new File(packagePath, "IntegerExtensions.java").getPath(),
                new File(packagePath, "ExampleEntity2.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources, "integerExtensions");
        String qtypeContent = Files.toString(new File("target/integerExtensions/com/querydsl/QExampleEntity2.java"), Charsets.UTF_8);
        //The superclass' id property is inherited, but can't be assigned to the custom QInteger
        assertTrue(qtypeContent.contains("public final ext.java.lang.QInteger id = new ext.java.lang.QInteger(_super.id);"));
    }

}
