/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;


public class ParameterModelTest {

    @Test
    public void test(){
        ParameterModel param1 = new ParameterModel("test", new ClassTypeModel(TypeCategory.STRING, String.class));
        ParameterModel param2 = new ParameterModel("test2", new ClassTypeModel(TypeCategory.STRING, String.class));
        ParameterModel param3 = new ParameterModel("test2", new ClassTypeModel(TypeCategory.NUMERIC, Integer.class));
        
        assertTrue(param1.equals(param2));
        assertFalse(param1.equals(param3));
        assertFalse(param2.equals(param3));
    }
}
