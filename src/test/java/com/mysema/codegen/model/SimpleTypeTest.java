package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void PrimitiveArray(){
        Type byteArray = new ClassType(byte[].class);
        Type byteArray2 = new SimpleType(byteArray, byteArray.getParameters());
        assertEquals("byte[]", byteArray.getRawName(Collections.singleton("java.lang"), Collections.<String>emptySet()));
        assertEquals("byte[]", byteArray2.getRawName(Collections.singleton("java.lang"), Collections.<String>emptySet()));
    }

    @Test
    public void Array_FullName(){
        Type type = new SimpleType(new ClassType(String[].class));
        assertEquals("java.lang.String[]", type.getFullName());
    }

    @Test
    public void GetComponenType(){
        Type type = new SimpleType(new ClassType(String[].class));
        assertEquals("java.lang.String", type.getComponentType().getFullName());
    }

}
