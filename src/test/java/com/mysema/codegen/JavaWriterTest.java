/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class JavaWriterTest {

    private static void match(String resource, String text) throws IOException{
        String expected = IOUtils.toString(JavaWriterTest.class.getResourceAsStream(resource),"UTF-8").replace("\r\n", "\n").trim();
        String actual = text.trim();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testBasic() throws IOException {
        StringWriter w = new StringWriter();
        CodeWriter writer = new JavaWriter(w);
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
    public void testJavadoc() throws IOException{
        StringWriter w = new StringWriter();
        CodeWriter writer = new JavaWriter(w);
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.javadoc("JavaWriterTest is a test class");
        writer.beginClass("JavaWriterTest");
        writer.end();
                
        match("/testJavadoc", w.toString());
    }
    
    @Test
    public void testAnnotations() throws IOException{
        StringWriter w = new StringWriter();
        CodeWriter writer = new JavaWriter(w);
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
    public void testFields() throws IOException{
        StringWriter w = new StringWriter();
        CodeWriter writer = new JavaWriter(w);
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
        StringWriter w = new StringWriter();
        CodeWriter writer = new JavaWriter(w);
        writer.beginClass("MethodTests");
        // private
        
        // protected
        
        // method
        
        // public
        
        writer.end();
        
        match("/testMethods", w.toString());
    }
    
}
