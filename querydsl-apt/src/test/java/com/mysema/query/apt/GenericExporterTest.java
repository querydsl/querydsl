package com.mysema.query.apt;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.domain.EntityTest;

@Ignore
public class GenericExporterTest {
    
    private GenericExporter exporter;

    @Before
    public void setUp() {
        exporter = new GenericExporter();
    }

    @Test
    public void Export() throws IOException {
        FileUtils.cleanDirectory(new File("target/gen1"));
        
        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(EntityTest.class.getPackage());        
        
        int total = 0;
        Set<String> failures = new TreeSet<String>();
        for (File file : new File("target/gen1/com/mysema/query/domain").listFiles()){
            if (file.isDirectory()){
                continue;
            }
            total++;
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(
                new File("target/generated-test-sources/java/com/mysema/query/domain", file.getName()), "UTF-8");
            if (!result1.equals(result2)){
                failures.add(file.getName());
            }
        }
        
        if (!failures.isEmpty()){
            for (String failure : failures){
                System.err.println(failure);
            }
            fail("Failed with " + failures.size() + " failures of " + total);
        }
    }

}
