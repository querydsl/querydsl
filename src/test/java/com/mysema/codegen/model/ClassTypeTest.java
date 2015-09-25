/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class ClassTypeTest {

    public class Inner {
        public class Inner2 {
            public class Inner3 {
            }
        }
    }

    private final ClassType stringType = new ClassType(TypeCategory.STRING, String.class);

    // @Test
    // public void asArrayType(){
    // assertEquals(stringType, stringType.asArrayType().getParameter(0));
    // }

    @Test
    public void InnerClass_Name() {
        assertEquals("com.mysema.codegen.model.ClassTypeTest.Inner",
                new ClassType(Inner.class).getFullName());
        assertEquals("com.mysema.codegen.model.ClassTypeTest.Inner[]", new ClassType(Inner.class)
                .asArrayType().getFullName());
    }

    @Test
    public void ArrayType() {
        Type type = new ClassType(TypeCategory.ARRAY, String[].class);
        assertEquals("java.lang", type.getPackageName());
    }

    @Test
    public void ArrayType_Equals_SimpleType() {
        Type type = new ClassType(TypeCategory.ARRAY, String[].class);
        Type type2 = new SimpleType("java.lang.String[]", "java.lang", "String[]");
        assertEquals(type, type2);
    }

    @Test
    public void As() {
        assertEquals(TypeCategory.COMPARABLE, stringType.as(TypeCategory.COMPARABLE).getCategory());
    }

    @Test
    public void GetParameters() {
        ClassType mapType = new ClassType(TypeCategory.MAP, Map.class, stringType, stringType);
        assertEquals(2, mapType.getParameters().size());
        assertEquals(stringType, mapType.getParameters().get(0));
        assertEquals(stringType, mapType.getParameters().get(1));
        // assertEquals(stringType, mapType.getSelfOrValueType());
        assertFalse(mapType.isPrimitive());
    }

    @Test
    public void GetComponentType() {
        assertEquals("java.lang.String", new ClassType(String[].class).getComponentType()
                .getFullName());
    }

    @Test
    public void Primitive_Arrays() {
        ClassType byteArray = new ClassType(byte[].class);
        assertEquals(
                "byte[]",
                byteArray.getRawName(Collections.singleton("java.lang"),
                        Collections.<String> emptySet()));
        assertEquals("byte[]", byteArray.getSimpleName());
        assertEquals("byte[]", byteArray.getFullName());
    }

    @Test
    public void Array() {
        ClassType byteArray = new ClassType(Byte[].class);
        assertEquals(
                "Byte[]",
                byteArray.getRawName(Collections.singleton("java.lang"),
                        Collections.<String> emptySet()));
        assertEquals("Byte[]", byteArray.getSimpleName());
        assertEquals("java.lang.Byte[]", byteArray.getFullName());
    }

    @Test
    public void IsPrimitive() {
        assertTrue(Types.CHAR.isPrimitive());
        assertTrue(Types.DOUBLE_P.isPrimitive());
        assertTrue(Types.FLOAT_P.isPrimitive());
        assertTrue(Types.INT.isPrimitive());
        assertTrue(Types.LONG_P.isPrimitive());
        assertTrue(Types.SHORT_P.isPrimitive());
    }

//    @Test
//    public void GetPrimitiveName() {
//        assertEquals("char", Types.CHARACTER.getPrimitiveName());
//        assertEquals("double", Types.DOUBLE.getPrimitiveName());
//        assertEquals("float", Types.FLOAT.getPrimitiveName());
//        assertEquals("int", Types.INTEGER.getPrimitiveName());
//        assertEquals("long", Types.LONG.getPrimitiveName());
//        assertEquals("short", Types.SHORT.getPrimitiveName());
//    }

    @Test
    public void GetEnclosingType() {
        Type outer = new ClassType(ClassTypeTest.class);
        Type inner = new ClassType(ClassTypeTest.Inner.class);
        Type inner2 = new ClassType(ClassTypeTest.Inner.Inner2.class);
        Type inner3 = new ClassType(ClassTypeTest.Inner.Inner2.Inner3.class);

        assertEquals(inner2, inner3.getEnclosingType());
        assertEquals(inner, inner2.getEnclosingType());
        assertEquals(outer, inner.getEnclosingType());
        assertNull(outer.getEnclosingType());

        assertEquals("ClassTypeTest.Inner.Inner2.Inner3",
                inner3.getRawName(ImmutableSet.of(outer.getPackageName()), ImmutableSet.<String>of()));
        assertEquals("Inner2.Inner3",
                inner3.getRawName(ImmutableSet.<String>of(), ImmutableSet.of(inner2.getFullName())));
        assertEquals("Inner3",
                inner3.getRawName(ImmutableSet.<String>of(), ImmutableSet.of(inner3.getFullName())));
    }
}
