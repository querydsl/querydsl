package com.mysema.query.apt;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class IncrementalCompilationTest extends AbstractProcessorTest{
    
    private static final String packagePath = "src/test/java/com/mysema/query/domain/";

    @Test
    public void Does_Not_Overwrite_Unchanged_Files() throws IOException, InterruptedException {
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
    
}
