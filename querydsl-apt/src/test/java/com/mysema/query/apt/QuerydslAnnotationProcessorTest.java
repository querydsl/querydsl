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

    private static final String PACKAGE_PATH = "src/test/java/com/mysema/query/domain/";
    
    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);
    
    @Test
    public void Process() throws IOException{
        File file = new File(PACKAGE_PATH, "AbstractEntityTest.java");
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(file.getPath()),"qdsl");
    }

    @Test
    public void ProcessInheritance3() throws IOException{
        String path = new File("src/test/java/com/mysema/query/inheritance/Inheritance3Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"inheritance3");
    }

    @Test
    public void ProcessInheritance8() throws IOException{
        String path = new File("src/test/java/com/mysema/query/inheritance/Inheritance8Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"inheritance8");
    }

    @Test
    public void ProcessQueryEmbedded3() throws IOException{
        String path = new File("src/test/java/com/mysema/query/domain/QueryEmbedded3Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"embedded3");
    }
    
    @Test
    public void ProcessQueryEmbedded4() throws IOException{
        String path = new File("src/test/java/com/mysema/query/domain/QueryEmbedded4Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"embedded3");
    }

    @Test
    public void ProcessDelegate() throws IOException{
        String path = new File("src/test/java/com/mysema/query/domain/DelegateTest.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"delegate");
    }

    @Test
    public void ProcessAbstractClasses() throws IOException{
        String path = new File("src/test/java/com/mysema/query/domain/AbstractClassesTest.java").getPath();
        process(JPAAnnotationProcessor.class, Collections.singletonList(path),"abstractClasses");
    }

    @Test
    public void ProcessGenericSignature() throws IOException{
        String path = new File("src/test/java/com/mysema/query/domain/GenericSignatureTest.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"genericSignature");
    }
    
    @Test
    public void ProcessAbstractProperties2Test() throws IOException {
        String path = new File("src/test/java/com/mysema/query/domain/AbstractProperties2Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"abstractProperties");
    }
    
    @Test
    public void EntityInheritanceTest() throws IOException {
        String path = new File("src/test/java/com/mysema/query/domain/EntityInheritanceTest.java").getPath();
        process(JPAAnnotationProcessor.class, Collections.singletonList(path),"entityInheritance");
    }
    
    @Test
    public void QuerydslAnnotationProcessor() throws IOException {
        process(QuerydslAnnotationProcessor.class, CLASSES, "querydsl");
    }
    
    @Test
    public void JPAAnnotationProcessor() throws IOException {
        process(JPAAnnotationProcessor.class, CLASSES, "jpa");
    }
    
    @Test
    public void HibernateAnnotationProcessor() throws IOException {
        process(HibernateAnnotationProcessor.class, CLASSES, "hibernate");
    }
    
    @Test
    public void JDOAnnotationProcessor() throws IOException {
        process(JDOAnnotationProcessor.class, CLASSES, "jdo");
    }
    
}
