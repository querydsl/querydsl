package com.mysema.query.apt;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        exporter.setTargetFolder(new File("target/gen1"));
        exporter.export(EntityTest.class.getPackage());        
        
        int total = 0;
        List<String> failures = new ArrayList<String>();
        for (File file : new File("target/gen1/com/mysema/query/domain").listFiles()){
            if (file.isDirectory()){
                continue;
            }
            total++;
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(
                new File("target/generated-test-sources/java/com/mysema/query/domain", file.getName()));
            if (!result1.equals(result2)){
                System.err.println(file.getName());
                failures.add(file.getName());
            }
        }
        
        if (!failures.isEmpty()){
            fail("Failed with " + failures.size() + " failures of " + total);
        }
    }

}
