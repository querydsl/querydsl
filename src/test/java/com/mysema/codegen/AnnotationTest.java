/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.ElementType;

import org.junit.Test;

@Annotation(prop2 = false, clazz = AnnotationTest.class)
@Annotation2("Hello")
@Annotation3(type = ElementType.ANNOTATION_TYPE)
public class AnnotationTest {

    private StringWriter w = new StringWriter();
    private CodeWriter writer = new JavaWriter(w);

    @Test
    public void ClassAnnotation() throws IOException {
        writer.annotation(getClass().getAnnotation(Annotation.class));
        assertEquals(
                "@com.mysema.codegen.Annotation(clazz=com.mysema.codegen.AnnotationTest.class, prop2=false)",
                w.toString().trim());
    }

    @Test
    public void ClassAnnotation2() throws IOException {
        writer.annotation(getClass().getAnnotation(Annotation2.class));
        assertEquals("@com.mysema.codegen.Annotation2(\"Hello\")", w.toString().trim());
    }

    @Test
    public void ClassAnnotation3() throws IOException {
        writer.annotation(getClass().getAnnotation(Annotation3.class));
        assertEquals(
                "@com.mysema.codegen.Annotation3(type=java.lang.annotation.ElementType.ANNOTATION_TYPE)",
                w.toString().trim());
    }

    @Test
    public void MethodAnnotation() throws IOException, SecurityException, NoSuchMethodException {
        writer.annotation(getClass().getMethod("MethodAnnotation").getAnnotation(Test.class));
        assertEquals("@org.junit.Test", w.toString().trim());
    }

    @Test
    public void Min() throws IOException {
        writer.annotation(new MinImpl(10));
        assertEquals("@javax.validation.constraints.Min(value=10)", w.toString().trim());
    }

    @Test
    public void Max() throws IOException {
        writer.annotation(new MaxImpl(10));
        assertEquals("@javax.validation.constraints.Max(value=10)", w.toString().trim());
    }

    @Test
    public void NotNull() throws IOException {
        writer.annotation(new NotNullImpl());
        assertEquals("@javax.validation.constraints.NotNull", w.toString().trim());
    }

    @Test
    public void Uri_Value() throws IOException {
        writer.annotation(new Annotation2Impl("http://www.example.com#"));
        assertEquals("@com.mysema.codegen.Annotation2(\"http://www.example.com#\")", w.toString()
                .trim());
    }

}
