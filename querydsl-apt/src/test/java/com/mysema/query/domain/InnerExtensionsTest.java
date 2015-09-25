package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.mysema.query.apt.AbstractProcessorTest;
import com.mysema.query.apt.QuerydslAnnotationProcessor;

public class InnerExtensionsTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/apt/com/mysema/query/";

    @Test
    public void Process() throws IOException {
        List<String> sources = Arrays.asList(
                new File(packagePath, "InnerExtensions.java").getPath(),
                new File(packagePath, "ExampleEntity2.java").getPath());
        process(QuerydslAnnotationProcessor.class, sources, "innerextensions");
        String qtypeContent = Files.toString(new File("target/innerextensions/com/mysema/query/QExampleEntity2.java"), Charsets.UTF_8);
        assertTrue(qtypeContent.contains("return InnerExtensions.ExampleEntity2Extensions.isZero(this);"));
    }
}
