package com.mysema.query.apt;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.codegen.GenericExporter;
import com.mysema.query.domain.AbstractEntityTest;

@Ignore // FIXME
public class GenericExporterTest extends AbstractProcessorTest{
    
    private static final String PACKAGE_PATH = "src/test/java/com/mysema/query/domain/";
    
    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);
    
    @Test
    public void Execute() throws IOException {
        // #1
        process(QuerydslAnnotationProcessor.class, CLASSES, "QuerydslAnnotationProcessor");
        
        // #2
        GenericExporter exporter = new GenericExporter();
        exporter.setTargetFolder(new File("target/GenericExporterTest"));
        exporter.export(AbstractEntityTest.class.getPackage());
        
        List<String> expected = new ArrayList<String>();
        // delegates are not supported
        expected.add("QDelegateTest_SimpleUser.java");
        expected.add("QDelegateTest_SimpleUser2.java");
        expected.add("QDelegateTest_User.java");
        expected.add("QDelegate2Test_Entity.java");
        // projections are not supported
        expected.add("QQueryProjectionTest_EntityWithProjection.java");
        
        List<String> failures = new ArrayList<String>();
        int successes = 0;
        for (File file : new File("target/GenericExporterTest/com/mysema/query/domain").listFiles()){
            File other = new File("target/QuerydslAnnotationProcessor/com/mysema/query/domain", file.getName());
            if (!other.exists() || !other.isFile()) continue;
            String result1 = FileUtils.readFileToString(file, "UTF-8");
            String result2 = FileUtils.readFileToString(other, "UTF-8");
            if (!result1.equals(result2)){
                if (!expected.contains(file.getName())) {
                    System.err.println(file.getName());
                    failures.add(file.getName());    
                }                
            } else {
                successes++;
            }
        }
        if (!failures.isEmpty()){
            fail("Failed with " + failures.size() + " failures, " + successes + " succeeded");
        }
    }

}
