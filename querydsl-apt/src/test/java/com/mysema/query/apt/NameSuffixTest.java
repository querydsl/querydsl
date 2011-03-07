package com.mysema.query.apt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NameSuffixTest extends AbstractProcessorTest{
    
    private static final String packagePath = "src/test/java/com/mysema/query/domain/";

    @Test
    public void ProcessAll() throws IOException{
        // works only in Eclipse for the moment
        List<String> classes = getFiles(packagePath);

        // default Processor
        process(QuerydslAnnotationProcessor.class, classes,"suffix");
        
        assertTrue(new File("target/suffix/com/mysema/query/domain/QAnimalTest_AnimalType.java").exists());
    }
    
    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.suffix=Type");
    }
    
}
