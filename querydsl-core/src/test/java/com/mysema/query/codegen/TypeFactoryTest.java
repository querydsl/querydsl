/*
 * Copyright (c) 2010 Mysema Ltd.
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

import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;

public class TypeFactoryTest {
    
    enum EnumExample { FIRST, SECOND}

    private TypeFactory factory = new TypeFactory();

    @Test
    public void Blob(){
        Type blob = factory.create(Blob.class);
        assertEquals("Blob", blob.getSimpleName());
        assertEquals("java.sql.Blob", blob.getFullName());
        assertEquals("java.sql", blob.getPackageName());
    }
    
    @Test
    public void Boolean(){
        Type bo = factory.create(boolean.class);
        assertEquals(TypeCategory.BOOLEAN, bo.getCategory());
        assertEquals("Boolean", bo.getSimpleName());
        assertEquals("java.lang.Boolean", bo.getFullName());
        assertEquals("java.lang", bo.getPackageName());
    }

    @Test
    public void SimpleType(){
        for (Class<?> cl : Arrays.<Class<?>>asList(Blob.class, Clob.class, Locale.class, Class.class, Serializable.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.SIMPLE, factory.create(cl).getCategory());
        }
    }
    
    @Test
    public void NumberType(){
        for (Class<?> cl : Arrays.<Class<?>>asList(Byte.class, Integer.class)){
            assertEquals("wrong type for " + cl.getName(), TypeCategory.NUMERIC, factory.create(cl).getCategory());
        }
    }
    
    @Test
    public void EnumType(){
        assertEquals(TypeCategory.ENUM, factory.create(EnumExample.class).getCategory());
    }

    @Test
    public void UnknownAsEntity(){
        assertEquals(TypeCategory.SIMPLE, factory.create(TypeFactoryTest.class).getCategory());
        
        factory = new TypeFactory();
        factory.setUnknownAsEntity(true);
        assertEquals(TypeCategory.ENTITY, factory.create(TypeFactoryTest.class).getCategory());
    }

}
