/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;


public class InspectingTypeModelTest {
    
    @Test
    public void test(){
        TypeModel blob = TypeModel.create(Blob.class);
        assertEquals("Blob", blob.getLocalName());
        assertEquals("Blob", blob.getSimpleName());
        assertEquals("java.sql.Blob", blob.getName());
        assertEquals("java.sql", blob.getPackageName());
        
        TypeModel bo = TypeModel.create(boolean.class);
        assertEquals("Boolean", bo.getLocalName());
        assertEquals("Boolean", bo.getSimpleName());
        assertEquals("java.lang.Boolean", bo.getName());
        assertEquals("java.lang", bo.getPackageName());
    }
    
    @Test
    public void getFieldType(){
        for (Class<?> cl : Arrays.<Class<?>>asList(Blob.class, Clob.class, Locale.class, Class.class, Serializable.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.SIMPLE, TypeModel.create(cl).getTypeCategory());
        }
        
        for (Class<?> cl : Arrays.<Class<?>>asList(Byte.class, Integer.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.NUMERIC, TypeModel.create(cl).getTypeCategory());
        }
        
        assertEquals(TypeCategory.BOOLEAN, TypeModel.create(boolean.class).getTypeCategory());
    }

}
