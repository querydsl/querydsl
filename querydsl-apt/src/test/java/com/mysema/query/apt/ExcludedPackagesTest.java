package com.mysema.query.apt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class ExcludedPackagesTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/mysema/query/";

    @Test
    public void Process() throws IOException{
        List<String> classes = getFiles(packagePath);
        process(QuerydslAnnotationProcessor.class, classes,"excludedPackages");

        assertFalse(new File("target/excludedPackages/com/mysema/query/domain/p1").exists());
        assertTrue(new File("target/excludedPackages/com/mysema/query/domain/p2").exists());
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.excludedPackages=com.mysema.query.domain.p1");
    }

}
