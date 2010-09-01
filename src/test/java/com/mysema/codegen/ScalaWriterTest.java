package com.mysema.codegen;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Types;

public class ScalaWriterTest {
    
    private Writer w = new StringWriter();
    
    private ScalaWriter writer = new ScalaWriter(w);
    
    @Test
    public void beanAccessors() throws IOException{
        writer.beginClass(new SimpleType("Person"));        
        writer.beginPublicMethod(Types.STRING, "getName");
        writer.line("\"Daniel Spiewak\"");
        writer.end();        
        writer.beginPublicMethod(Types.VOID, "setName", new Parameter("name",Types.STRING));
        writer.line("//");
        writer.end();        
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void mainMethod() throws IOException{
//        def main(args: Array[String]) {
        writer.beginClass(new SimpleType("Main"));
        writer.beginPublicMethod(Types.VOID, "main", new Parameter("args",Types.STRING.asArrayType()));
        writer.line("//");
        writer.end();        
        writer.end();
        
        System.out.println(w);
    }
    
    @Test
    public void trait() throws IOException{
        writer.beginInterface(new SimpleType("MyTrait"));
        writer.line("//");
        writer.end();
        
        System.out.println(w);
    }

}
