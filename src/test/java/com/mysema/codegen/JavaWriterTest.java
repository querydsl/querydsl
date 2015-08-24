/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.io.Resources;
import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;

public class JavaWriterTest {

    private static final Function<Parameter, Parameter> transformer = new Function<Parameter, Parameter>() {
        @Override
        public Parameter apply(Parameter input) {
            return input;
        }
    };

    private StringWriter w;

    private CodeWriter writer;

    private Type testType, testType2, testSuperType, testInterface1, testInterface2;

    private static void match(String resource, String text) throws IOException {
        // TODO : try to compile ?
        String expected = Resources.toString(JavaWriterTest.class.getResource(resource), Charsets.UTF_8)
                .replace("\r\n", "\n").trim();
        String actual = text.trim();
        assertEquals(expected, actual);
    }

    @Before
    public void setUp() {
        w = new StringWriter();
        writer = new JavaWriter(w);
        testType = new ClassType(JavaWriterTest.class);
        testType2 = new SimpleType("com.mysema.codegen.Test", "com.mysema.codegen", "Test");
        testSuperType = new SimpleType("com.mysema.codegen.Superclass", "com.mysema.codegen",
                "Superclass");
        testInterface1 = new SimpleType("com.mysema.codegen.TestInterface1", "com.mysema.codegen",
                "TestInterface1");
        testInterface2 = new SimpleType("com.mysema.codegen.TestInterface2", "com.mysema.codegen",
                "TestInterface2");
    }

    @Test
    public void Arrays() throws IOException {
        writer.beginClass(new SimpleType("Main"));
        writer.field(Types.STRING.asArrayType(), "stringArray");
        writer.beginPublicMethod(Types.VOID, "main",
                new Parameter("args", Types.STRING.asArrayType()));
        writer.line("//");
        writer.end();
        writer.beginPublicMethod(Types.VOID, "main2", new Parameter("args", new ClassType(
                TypeCategory.ARRAY, String[].class)));
        writer.line("//");
        writer.end();
        writer.end();

        System.out.println(w);
        assertTrue(w.toString().contains("String[] stringArray;"));
        assertTrue(w.toString().contains("public void main(String[] args) {"));
        assertTrue(w.toString().contains("public void main2(String[] args) {"));
    }

    @Test
    public void Primitive_Arrays() {
        ClassType byteArray = new ClassType(byte[].class);
        assertEquals("byte[]", writer.getRawName(byteArray));
    }

    @Test
    public void Basic() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginClass(testType);
        writer.annotation(Test.class);
        writer.beginPublicMethod(Types.VOID, "test");
        writer.line("// TODO");
        writer.end();
        writer.end();

        match("/testBasic", w.toString());
    }

    @Test
    public void Extends() throws IOException {
        writer.beginClass(testType2, testSuperType);
        writer.end();

        match("/testExtends", w.toString());
    }

    @Test
    public void Implements() throws IOException {
        writer.beginClass(testType2, null, testInterface1, testInterface2);
        writer.end();

        match("/testImplements", w.toString());
    }

    @Test
    public void Interface() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.beginInterface(testType);
        writer.end();

        match("/testInterface", w.toString());
    }

    @Test
    public void Interface2() throws IOException {
        writer.beginInterface(testType2, testInterface1);
        writer.end();

        match("/testInterface2", w.toString());
    }

    @Test
    public void Interface3() throws IOException {
        writer.beginInterface(testType, testType2, testInterface1, testInterface2);
        writer.end();

        assertTrue(w.toString().contains(
                "public interface JavaWriterTest extends Test, TestInterface1, TestInterface2 {"));
    }

    @Test
    public void Javadoc() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class, Test.class);
        writer.javadoc("JavaWriterTest is a test class");
        writer.beginClass(testType);
        writer.end();

        match("/testJavadoc", w.toString());
    }

    @Test
    public void Annotations() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class, StringWriter.class);
        writer.annotation(Entity.class);
        writer.beginClass(testType);
        writer.annotation(Test.class);
        writer.beginPublicMethod(Types.VOID, "test");
        writer.end();
        writer.end();

        match("/testAnnotations", w.toString());
    }

    @Test
    public void Annotations2() throws IOException {
        writer.packageDecl("com.mysema.codegen");
        writer.imports(IOException.class.getPackage(), StringWriter.class.getPackage());
        writer.annotation(Entity.class);
        writer.beginClass(testType);
        writer.annotation(new Test() {
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
            }
        });
        writer.beginPublicMethod(Types.VOID, "test");
        writer.end();
        writer.end();

        match("/testAnnotations2", w.toString());
    }

    @Test
    public void Annotation_With_ArrayMethod() throws IOException {
        Target annotation = new Target() {
            @Override
            public ElementType[] value() {
                return new ElementType[] { ElementType.FIELD, ElementType.METHOD };
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Target.class;
            }
        };

        writer.imports(Target.class.getPackage());
        writer.annotation(annotation);
        assertTrue(w.toString().contains("@Target({FIELD, METHOD})"));
    }

    @Test
    public void ClassConstants() {
        assertEquals("SomeClass.class", writer.getClassConstant("SomeClass"));
    }

    @Test
    public void Fields() throws IOException {
        writer.beginClass(testType);
        // private
        writer.privateField(Types.STRING, "privateField");
        writer.privateStaticFinal(Types.STRING, "privateStaticFinal", "\"val\"");
        // protected
        writer.protectedField(Types.STRING, "protectedField");
        // field
        writer.field(Types.STRING, "field");
        // public
        writer.publicField(Types.STRING, "publicField");
        writer.publicStaticFinal(Types.STRING, "publicStaticFinal", "\"val\"");
        writer.publicFinal(Types.STRING, "publicFinalField");
        writer.publicFinal(Types.STRING, "publicFinalField2", "\"val\"");

        writer.end();

        match("/testFields", w.toString());
    }

    @Test
    public void Methods() throws IOException {
        writer.beginClass(testType);
        // private

        // protected

        // method

        // public
        writer.beginPublicMethod(Types.STRING, "publicMethod",
                Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();

        writer.beginStaticMethod(Types.STRING, "staticMethod",
                Arrays.asList(new Parameter("a", Types.STRING)), transformer);
        writer.line("return null;");
        writer.end();

        writer.end();

        match("/testMethods", w.toString());
    }

    @Test
    public void Constructors() throws IOException {
        writer.beginClass(testType);

        writer.beginConstructor(
                Arrays.asList(new Parameter("a", Types.STRING), new Parameter("b", Types.STRING)),
                transformer);
        writer.end();

        writer.beginConstructor(new Parameter("a", Types.STRING));
        writer.end();

        writer.end();

        match("/testConstructors", w.toString());

    }

    @Test
    public void Inner_Classes() throws IOException {
        writer.beginClass(testType);

        writer.beginClass(testType2);
        writer.end();

        writer.beginConstructor(new Parameter("a", Types.STRING));
        writer.end();

        writer.end();

        match("/testInnerClasses", w.toString());
    }

    @Test
    public void Imports() throws IOException {
        writer.staticimports(Arrays.class);

        match("/testImports", w.toString());
    }

    @Test
    public void Imports2() throws IOException {
        writer.importPackages("java.lang.reflect", "java.util");

        match("/testImports2", w.toString());
    }

    @Test
    public void Imports3() throws IOException {
        writer.importClasses("java.util.Locale");

        assertTrue(w.toString().contains("import java.util.Locale;"));
    }

    @Test
    public void SuppressWarnings() throws IOException {
        writer.suppressWarnings("unused");
        writer.privateField(Types.STRING, "test");

        match("/testSuppressWarnings", w.toString());
    }

    @Test
    public void SuppressWarnings2() throws IOException {
        writer.suppressWarnings("all", "unused");
        writer.privateField(Types.STRING, "test");

        match("/testSuppressWarnings2", w.toString());
    }
}
