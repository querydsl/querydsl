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
    
    private TypeModelFactory factory = new TypeModelFactory();
    
    @Test
    public void test(){        
        TypeModel blob = factory.create(Blob.class);
//        assertEquals("Blob", blob.getLocalName());
        assertEquals("Blob", blob.getSimpleName());
        assertEquals("java.sql.Blob", blob.getFullName());
        assertEquals("java.sql", blob.getPackageName());
        
        TypeModel bo = factory.create(boolean.class);
//        assertEquals("Boolean", bo.getLocalName());
        assertEquals("Boolean", bo.getSimpleName());
        assertEquals("java.lang.Boolean", bo.getFullName());
        assertEquals("java.lang", bo.getPackageName());
    }
    
    @Test
    public void getFieldType(){
        for (Class<?> cl : Arrays.<Class<?>>asList(Blob.class, Clob.class, Locale.class, Class.class, Serializable.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.SIMPLE, factory.create(cl).getCategory());
        }
        
        for (Class<?> cl : Arrays.<Class<?>>asList(Byte.class, Integer.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.NUMERIC, factory.create(cl).getCategory());
        }
        
        assertEquals(TypeCategory.BOOLEAN, factory.create(boolean.class).getCategory());
    }

}
