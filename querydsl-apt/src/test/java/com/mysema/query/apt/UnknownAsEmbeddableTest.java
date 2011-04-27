package com.mysema.query.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class UnknownAsEmbeddableTest extends AbstractProcessorTest {

    private static final String packagePath = "src/test/java/com/mysema/query/domain/custom";

    @Test
    public void Process() throws IOException{
        List<String> classes = getFiles(packagePath);
        process(QuerydslAnnotationProcessor.class, classes,"unknownAsEmbeddable");

        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/query/domain/custom/QEntity.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/query/domain/custom/QEmbeddedType.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/query/domain/custom/QEmbeddedType2.java").exists());
        assertTrue(new File("target/unknownAsEmbeddable/com/mysema/query/domain/custom/QEmbeddedType3.java").exists());
    }

    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.unknownAsEmbeddable=true");
    }
    
}
