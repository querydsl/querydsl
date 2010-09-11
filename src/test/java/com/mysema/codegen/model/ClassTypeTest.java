/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Map;

import org.junit.Test;


public class ClassTypeTest {

    private ClassType stringType = new ClassType(TypeCategory.STRING, String.class);

//    @Test
//    public void asArrayType(){
//        assertEquals(stringType, stringType.asArrayType().getParameter(0));
//    }

    @Test
    public void As(){
        assertEquals(TypeCategory.COMPARABLE, stringType.as(TypeCategory.COMPARABLE).getCategory());
    }

    @Test
    public void getParameters(){
        ClassType mapType = new ClassType(TypeCategory.MAP, Map.class, stringType, stringType);
        assertEquals(2, mapType.getParameters().size());
        assertEquals(stringType, mapType.getParameters().get(0));
        assertEquals(stringType, mapType.getParameters().get(1));
//        assertEquals(stringType, mapType.getSelfOrValueType());
        assertFalse(mapType.isPrimitive());
    }

    @Test
    public void Primitive_Arrays(){
        ClassType byteArray = new ClassType(byte[].class);
        assertEquals("byte[]", byteArray.getRawName(Collections.singleton("java.lang"), Collections.<String>emptySet()));
        assertEquals("byte[]", byteArray.getSimpleName());
        assertEquals("byte[]", byteArray.getFullName());
    }
    
    @Test
    public void Array(){
        ClassType byteArray = new ClassType(Byte[].class);
        assertEquals("Byte[]", byteArray.getRawName(Collections.singleton("java.lang"), Collections.<String>emptySet()));
        assertEquals("Byte[]", byteArray.getSimpleName());
        assertEquals("java.lang.Byte[]", byteArray.getFullName());
    }
}
