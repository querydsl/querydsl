package com.mysema.codegen;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class JavaWriterTest {

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
        System.out.println(w);
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
        System.out.println(w);
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
        System.out.println(w);
    }
    
}
