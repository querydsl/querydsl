package com.mysema.query.apt;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ExcludedClassesTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/mysema/query/";

    @Test
    public void Process() throws IOException{
        List<String> classes = getFiles(packagePath);
        process(QuerydslAnnotationProcessor.class, classes,"excludedClasses");

        assertFalse(new File("target/excludedClasses/com/mysema/query/domain/QArrayTest_ArrayTestEntity").exists());
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.excludedClasses=com.mysema.query.domain.ArrayTest.ArrayTestEntity");
    }

}
