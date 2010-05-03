/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class JavaWriterTest {
    
    private StringWriter w;
    
    private CodeWriter writer;

    private static void match(String resource, String text) throws IOException{
        // TODO : try to compile ?
        String expected = IOUtils.toString(JavaWriterTest.class.getResourceAsStream(resource),"UTF-8").replace("\r\n", "\n").trim();
        String actual = text.trim();
        assertEquals(expected, actual);
    }

    @Before
    public void setUp(){
        w = new StringWriter();
        writer = new JavaWriter(w);   
    }

    
    @Test
    public void testBasic() throws IOException {        
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginClass("JavaWriterTest");
        writer.annotation(Test.class);
        writer.beginPublicMethod("void", "test");
        writer.line("// TODO");
        writer.end();
        writer.end();
        
        match("/testBasic", w.toString());
    }
    
    @Test
    public void testExtends() throws IOException{
        writer.beginClass("Test", "Superclass");
        writer.end();
        
        match("/testExtends", w.toString());
    }
    
    @Test
    public void testImplements() throws IOException{
        writer.beginClass("Test", null, "TestInterface1","TestInterface2");
        writer.end();
        
        match("/testImplements", w.toString());
    }
    
    @Test
    public void testInterface() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginInterface("JavaWriterTest");
        writer.end();
        
        match("/testInterface", w.toString());
    }
    
    @Test
    public void testInterface2() throws IOException{
        writer.beginInterface("Test", "Test1");
        writer.end();
        
        match("/testInterface2", w.toString());
    }
    
    @Test
    public void testJavadoc() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.javadoc("JavaWriterTest is a test class");
        writer.beginClass("JavaWriterTest");
        writer.end();
                
        match("/testJavadoc", w.toString());
    }

    
    @Test
    public void testAnnotations() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class);
        writer.annotation(Entity.class);
        writer.beginClass("JavaWriterTest");
        writer.annotation(Test.class);
        writer.beginPublicMethod("void", "test");
        writer.end();
        writer.end();
                
        match("/testAnnotations", w.toString());
    }
    
    @Test
    public void testAnnotations2() throws IOException{
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class.getPackage(), StringWriter.class.getPackage());
        writer.annotation(Entity.class);
        writer.beginClass("JavaWriterTest");
        writer.annotation(new Test(){
            @Override
            public Class<? extends Throwable> expected() {
                // TODO Auto-generated method stub
                return null;
            }
            @Override
            public long timeout() {

                return 0;
            }
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }});
        writer.beginPublicMethod("void", "test");
        writer.end();
        writer.end();
                
        match("/testAnnotations2", w.toString());
    }
    
    @Test
    public void testFields() throws IOException{
        writer.beginClass("FieldTests");
        // private
        writer.privateField("String", "privateField");
        writer.privateStaticFinal("String", "privateStaticFinal", "\"val\"");
        // protected
        writer.protectedField("String","protectedField");
        // field
        writer.field("String","field");
        // public
        writer.publicField("String","publicField");
        writer.publicStaticFinal("String", "publicStaticFinal", "\"val\"");
        writer.end();
        
        match("/testFields", w.toString());
    }
    
    @Test
    public void testMethods() throws IOException{
        writer.beginClass("MethodTests");
        // private
        
        // protected
        
        // method
        
        // public
        
        writer.end();
        
        match("/testMethods", w.toString());
    }
    
}
