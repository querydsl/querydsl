package com.querydsl.codegen.utils.model;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

public class SimpleTypeTest {

    public static class Inner {
        public class Inner2 {
            public class Inner3 {
            }
        }
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
        Type outer = new SimpleType(new ClassType(SimpleTypeTest.class));
        Type inner = new SimpleType(new ClassType(SimpleTypeTest.Inner.class));
        Type inner2 = new SimpleType(new ClassType(SimpleTypeTest.Inner.Inner2.class));
        Type inner3 = new SimpleType(new ClassType(SimpleTypeTest.Inner.Inner2.Inner3.class));

        assertEquals(inner2, inner3.getEnclosingType());
        assertEquals(inner, inner2.getEnclosingType());
        assertEquals(outer, inner.getEnclosingType());
        assertNull(outer.getEnclosingType());

        assertEquals("SimpleTypeTest.Inner.Inner2.Inner3",
                inner3.getRawName(Collections.singleton(outer.getPackageName()), Collections.emptySet()));
        assertEquals("Inner2.Inner3",
                inner3.getRawName(Collections.emptySet(), Collections.singleton(inner2.getFullName())));
        assertEquals("Inner3",
                inner3.getRawName(Collections.emptySet(), Collections.singleton(inner3.getFullName())));
    }
    @Test
    public void IsMember() {
        assertTrue(new SimpleType(new ClassType(SimpleTypeTest.Inner.class)).isMember());
        assertFalse(new SimpleType(new ClassType(SimpleType.class)).isMember());
    }

}
