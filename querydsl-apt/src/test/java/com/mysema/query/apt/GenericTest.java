package com.mysema.query.apt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class GenericTest extends AbstractProcessorTest {
    
    @Test
    public void test() throws IOException {
        List<String> classes = Collections.singletonList("src/test/java/com/mysema/query/domain/Generic7Test.java");
        process(QuerydslAnnotationProcessor.class, classes,"GenericTest");
    }

}
