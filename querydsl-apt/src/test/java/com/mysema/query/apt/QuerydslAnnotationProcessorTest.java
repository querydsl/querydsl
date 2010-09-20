package com.mysema.query.apt;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static junit.framework.Assert.*;

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
    public void processInheritance3() throws IOException{
        String path = new File("src/test/java/com/mysema/query/inheritance/Inheritance3Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"inheritance3");
    }
    
    @Test
    public void processInheritance8() throws IOException{
        String path = new File("src/test/java/com/mysema/query/inheritance/Inheritance8Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"inheritance8");
    }

    @Test
    public void processInheritance10() throws IOException{
        String path = new File("src/test/java/com/mysema/query/inheritance/Inheritance10Test.java").getPath();
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path),"inheritance10");
    }
    
    @Test
    public void doesNotOverwriteUnchangedFiles() throws IOException, InterruptedException {
        File source = new File(packagePath, "ExampleEntity.java");
        String path = source.getPath();
        File qType = new File("target/overwrite/com/mysema/query/domain/QExampleEntity.java");
        
        // QTestEntity is generated
        process(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue(qType.exists());
        long modified = qType.lastModified();
        Thread.sleep(1000);
        
        // TestEntity has not changed, QTestEntity is not overwritten
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertEquals(modified, qType.lastModified());

        // TestEntity is updated, QTestEntity is overwritten
        FileUtils.touch(source);
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue("" + modified + " >= " + qType.lastModified(), modified < qType.lastModified());
        
        // QTestEntity is deleted and regenerated 
        assertTrue(qType.delete());
        compile(QuerydslAnnotationProcessor.class, Collections.singletonList(path), "overwrite");
        assertTrue(qType.exists());
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
