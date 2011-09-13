package com.mysema.query.apt;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class EmbeddableTest extends AbstractProcessorTest {

    @Test
    public void Process() throws IOException{
        List<String> classes = Collections.singletonList("src/test/java/com/mysema/query/domain/Embeddable2Test.java");
        process(QuerydslAnnotationProcessor.class, classes,"embeddable");
    }

}
