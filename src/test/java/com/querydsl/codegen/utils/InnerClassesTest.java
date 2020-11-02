package com.querydsl.codegen.utils;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import com.querydsl.codegen.utils.model.ClassType;
import com.querydsl.codegen.utils.model.SimpleType;
import org.junit.Test;

import com.querydsl.codegen.utils.model.Type;

public class InnerClassesTest {

    public static class Entity {

    }

    @Test
    public void DirectParameter() throws IOException {
        Type entityType = new ClassType(Entity.class);
        Type type = new SimpleType("com.querydsl.codegen.utils.gen.QEntity", "com.querydsl.codegen.utils.gen",
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
        
        assertEquals("com.querydsl.codegen.utils.InnerClassesTest.Entity",
                writer.getRawName(new ClassType(Entity.class)));
    }
    
    @Test
    public void Scala() {
        StringWriter str = new StringWriter();
        ScalaWriter writer = new ScalaWriter(str);
        
        assertEquals("com.querydsl.codegen.utils.InnerClassesTest$Entity",
                writer.getRawName(new ClassType(Entity.class)));
    }

}
