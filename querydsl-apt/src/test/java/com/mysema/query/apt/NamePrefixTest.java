package com.mysema.query.apt;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class NamePrefixTest extends AbstractProcessorTest{
    
    private static final String packagePath = "src/test/java/com/mysema/query/domain/";

    @Test
    public void ProcessAll() throws IOException{
        // works only in Eclipse for the moment
        List<String> classes = getFiles(packagePath);

        // default Processor
        process(QuerydslAnnotationProcessor.class, classes,"prefix");
        
        assertTrue(new File("target/prefix/com/mysema/query/domain/QTAnimalTest_Animal.java").exists());
    }
    
    @Override
    protected Collection<String> getAPTOptions() {
        return Arrays.asList("-Aquerydsl.prefix=QT");
    }
    
}
