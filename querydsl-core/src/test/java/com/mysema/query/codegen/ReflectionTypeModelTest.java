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


public class ReflectionTypeModelTest {
    
    @Test
    public void test(){
        TypeModel blob = get(Blob.class);
        assertEquals("Blob", blob.getLocalName());
        assertEquals("Blob", blob.getSimpleName());
        assertEquals("java.sql.Blob", blob.getName());
        assertEquals("java.sql", blob.getPackageName());
        
        TypeModel bo = get(boolean.class);
        assertEquals("Boolean", bo.getLocalName());
        assertEquals("Boolean", bo.getSimpleName());
        assertEquals("java.lang.Boolean", bo.getName());
        assertEquals("java.lang", bo.getPackageName());
    }
    
    @Test
    public void getFieldType(){
        for (Class<?> cl : Arrays.<Class<?>>asList(Blob.class, Clob.class, Locale.class, Class.class, Serializable.class)){
            assertEquals(FieldType.SIMPLE, get(cl).getFieldType());
        }
        
        for (Class<?> cl : Arrays.<Class<?>>asList(Number.class, Integer.class)){
            assertEquals(FieldType.NUMERIC, get(cl).getFieldType());
        }
        
        assertEquals(FieldType.BOOLEAN, get(boolean.class).getFieldType());
    }

    private TypeModel get(Class<?> cl){
        return ReflectionTypeModel.get(cl);
    }
}
