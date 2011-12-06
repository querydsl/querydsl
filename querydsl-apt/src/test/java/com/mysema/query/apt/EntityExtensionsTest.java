package com.mysema.query.apt;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class EntityExtensionsTest extends AbstractProcessorTest{
    
    private static final String packagePath = "src/test/java/com/mysema/query/apt/";
    
    @Test
    public void Handles_Entity_Extensions_Correctly() throws IOException, InterruptedException{
        File source = new File(packagePath,  "EntityWithExtensions.java");
        File source2 = new File(packagePath, "EntityExtensions.java");
        List<String> sources = Arrays.asList(source.getPath(), source2.getPath());
        File qType = new File("target/overwrite2/com/mysema/query/apt/QEntityWithExtensions.java");
        
        // QEntityWithExtensions is generated
        process(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue(qType.exists());
        long modified = qType.lastModified();
        Thread.sleep(1000);        
        System.out.println(FileUtils.readFileToString(qType).contains("extension()"));
        
        // EntityWithExtensions has not changed, QEntityWithExtensions is not overwritten
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertEquals(modified, qType.lastModified());

        // EntityWithExtensions is updated, QEntityWithExtensions is overwritten
        FileUtils.touch(source);
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue("" + modified + " >= " + qType.lastModified(), modified < qType.lastModified());
        assertTrue(FileUtils.readFileToString(qType).contains("extension()"));
        
        // QEntityWithExtensions is deleted and regenerated 
        assertTrue(qType.delete());
        compile(QuerydslAnnotationProcessor.class, sources, "overwrite2");
        assertTrue(qType.exists());
        assertTrue(FileUtils.readFileToString(qType).contains("extension()"));
    }
    
    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-AdefaultOverwrite=true");
    }

}
