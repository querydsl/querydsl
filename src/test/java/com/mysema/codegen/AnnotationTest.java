package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

@TestAnnotation(prop2 = false)
public class AnnotationTest {
    
    private StringWriter w = new StringWriter();
    private CodeWriter writer = new JavaWriter(w);
    
    @Test
    public void testClassAnnotation() throws IOException{
        writer.annotation(getClass().getAnnotation(TestAnnotation.class));
        assertEquals("@com.mysema.codegen.TestAnnotation(prop2=false)", w.toString().trim());
    }
    
    @Test
    public void testMethodAnnotation() throws IOException, SecurityException, NoSuchMethodException {        
        writer.annotation(getClass().getMethod("testMethodAnnotation").getAnnotation(Test.class));
        assertEquals("@org.junit.Test", w.toString().trim());
    }

}
