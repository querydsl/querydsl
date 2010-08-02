package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

@TestAnnotation(prop2 = false, clazz = AnnotationTest.class)
@TestAnnotation2("Hello")
public class AnnotationTest {
    
    
    private StringWriter w = new StringWriter();
    private CodeWriter writer = new JavaWriter(w);
    
    @Test
    public void testClassAnnotation() throws IOException{
        writer.annotation(getClass().getAnnotation(TestAnnotation.class));
        assertEquals("@com.mysema.codegen.TestAnnotation(clazz=com.mysema.codegen.AnnotationTest.class, prop2=false)", w.toString().trim());
    }
    
    @Test
    public void testClassAnnotation2() throws IOException{
        writer.annotation(getClass().getAnnotation(TestAnnotation2.class));
        assertEquals("@com.mysema.codegen.TestAnnotation2(\"Hello\")", w.toString().trim());
    }
    
    
    @Test
    public void testMethodAnnotation() throws IOException, SecurityException, NoSuchMethodException {        
        writer.annotation(getClass().getMethod("testMethodAnnotation").getAnnotation(Test.class));
        assertEquals("@org.junit.Test", w.toString().trim());
    }

}
