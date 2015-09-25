package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Test;

public class SimpleTypeTest {

    public static class Inner {

    }
    
    @Test
    public void PrimitiveArray() {
        Type byteArray = new ClassType(byte[].class);
        Type byteArray2 = new SimpleType(byteArray, byteArray.getParameters());
        assertEquals(
                "byte[]",
                byteArray.getRawName(Collections.singleton("java.lang"),
                        Collections.<String> emptySet()));
        assertEquals(
                "byte[]",
                byteArray2.getRawName(Collections.singleton("java.lang"),
                        Collections.<String> emptySet()));
    }

    @Test
    public void Array_FullName() {
        Type type = new SimpleType(new ClassType(String[].class));
        assertEquals("java.lang.String[]", type.getFullName());
    }

    @Test
    public void GetComponentType() {
        Type type = new SimpleType(new ClassType(String[].class));
        assertEquals("java.lang.String", type.getComponentType().getFullName());
    }

    @Test
    public void GetRawName() {
        assertEquals("String", new SimpleType(Types.STRING).getRawName(
                Collections.<String> emptySet(), Collections.singleton(Types.STRING.getFullName())));
    }

    @Test
    public void GetJavaClass_For_Array() {
        System.out.println(Inner.class.getName());
        assertEquals(byte[].class, new ClassType(byte[].class).getJavaClass());
        assertEquals(byte[].class, new SimpleType(new ClassType(byte[].class)).getJavaClass());
    }

    @Test
    public void GetJavaClass_For_InnerClass() {
        assertEquals(Inner.class, new ClassType(Inner.class).getJavaClass());
        assertEquals(Inner.class, new SimpleType(new ClassType(Inner.class)).getJavaClass());
    }
    
//    @Test
//    public void GetPrimitiveName() {
//        assertEquals("int", Types.INT.getPrimitiveName());
//        assertEquals("int", new SimpleType(Types.INT).getPrimitiveName());
//        
//        assertEquals("int", Types.INTEGER.getPrimitiveName());
//        assertEquals("int", new SimpleType(Types.INTEGER).getPrimitiveName());
//    }

    @Test
    public void GetEnclosingType() {
        assertEquals(new SimpleType(new ClassType(SimpleTypeTest.class)),
                new SimpleType(new ClassType(Inner.class)).getEnclosingType());
        assertNull(new SimpleType(new ClassType(SimpleTypeTest.class)).getEnclosingType());
    }

}
