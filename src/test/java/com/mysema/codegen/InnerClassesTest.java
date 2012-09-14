package com.mysema.codegen;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;

public class InnerClassesTest {

    public static class Entity {

    }

    @Test
    public void DirectParameter() throws IOException {
        Type entityType = new ClassType(Entity.class);
        Type type = new SimpleType("com.mysema.codegen.gen.QEntity", "com.mysema.codegen.gen",
                "QEntity", entityType);

        StringWriter str = new StringWriter();
        JavaWriter writer = new JavaWriter(str);
        writer.beginClass(type);
        writer.end();

        System.err.println(str.toString());
    }
    
    @Test
    public void Java() {
        StringWriter str = new StringWriter();
        JavaWriter writer = new JavaWriter(str);
        
        assertEquals("com.mysema.codegen.InnerClassesTest.Entity", 
                writer.getRawName(new ClassType(Entity.class)));
    }
    
    @Test
    public void Scala() {
        StringWriter str = new StringWriter();
        ScalaWriter writer = new ScalaWriter(str);
        
        assertEquals("com.mysema.codegen.InnerClassesTest$Entity", 
                writer.getRawName(new ClassType(Entity.class)));
    }

}
