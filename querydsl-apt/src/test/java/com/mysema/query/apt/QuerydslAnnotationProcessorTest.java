package com.mysema.query.apt;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.mysema.query.apt.hibernate.HibernateAnnotationProcessor;
import com.mysema.query.apt.jdo.JDOAnnotationProcessor;
import com.mysema.query.apt.jpa.JPAAnnotationProcessor;

public class QuerydslAnnotationProcessorTest extends AbstractProcessorTest{

    private static final String packagePath = "src/test/java/com/mysema/query/domain/";

    @Test
    public void process() throws IOException{
        File file = new File(packagePath, "AbstractEntityTest.java");
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(file.getPath()),"qdsl");
    }

    @Test
    public void processAll() throws IOException{
        // works only in Eclipse for the moment
        List<String> classes = getFiles(packagePath);

        // default processor
        process(QuerydslAnnotationProcessor.class, classes,"querydsl");

        // JPA
        process(JPAAnnotationProcessor.class, classes,"jpa");

        // Hibernate
        process(HibernateAnnotationProcessor.class, classes,"hibernate");

        // JDO
        process(JDOAnnotationProcessor.class, classes,"jdo");
    }

}
