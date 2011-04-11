/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.Types;
import com.mysema.query.types.Expression;

public class TypeFactoryTest {

    Expression<?> field;

    Expression<Object> field2;

    Expression field3;

    List<? extends Expression> field4;

    enum EnumExample { FIRST, SECOND}

    class Entity<A> {

        List<? extends A> field;

    }

    private TypeFactory factory = new TypeFactory();

    @Test
    public void InnerClass_Field() throws SecurityException, NoSuchFieldException{
        Field field = Entity.class.getDeclaredField("field");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
        System.out.println(type.getParameters().get(0));
        assertEquals(Types.OBJECT, type.getParameters().get(0));
    }

    @Test
    public void Generics_WildCard() throws SecurityException, NoSuchFieldException{
        Field field = getClass().getDeclaredField("field");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
        assertNull(type.getParameters().get(0));
    }

    @Test
    public void Generics_Object() throws SecurityException, NoSuchFieldException{
        Field field = getClass().getDeclaredField("field2");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
        assertEquals(Types.OBJECT, type.getParameters().get(0));
    }

    @Test
    public void RawField() throws SecurityException, NoSuchFieldException{
        Field field = getClass().getDeclaredField("field3");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
//        assertEquals(Types.OBJECT, type.getParameters().get(0));
    }

    @Test
    public void Extends() throws SecurityException, NoSuchFieldException{
        Field field = getClass().getDeclaredField("field4");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
//        assertEquals(Types.OBJECT, type.getParameters().get(0));
    }

    @Test
    public void ClassName(){
        Type type = factory.create(EnumExample.class);
        assertEquals("com.mysema.query.codegen.TypeFactoryTest.EnumExample", type.getFullName());
    }

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
