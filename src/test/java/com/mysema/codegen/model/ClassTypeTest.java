/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Map;

import org.junit.Test;


public class ClassTypeTest {

    private ClassType stringType = new ClassType(TypeCategory.STRING, String.class);

//    @Test
//    public void asArrayType(){
//        assertEquals(stringType, stringType.asArrayType().getParameter(0));
//    }

    @Test
    public void as(){
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

}
