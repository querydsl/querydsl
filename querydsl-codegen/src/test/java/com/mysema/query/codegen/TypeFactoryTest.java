/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Type;
import com.mysema.codegen.model.TypeCategory;
import com.mysema.codegen.model.TypeExtends;
import com.mysema.codegen.model.Types;
import com.mysema.query.types.Expression;

public class TypeFactoryTest {

    Expression<?> field;

    Expression<Object> field2;

    Expression<?> field3;

    List<? extends Expression<?>> field4;

    enum EnumExample { FIRST, SECOND}

    static class Entity<A> {

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
    public void Parameters() {
        EntityType type = factory.createEntityType(Examples.Complex.class);
        assertEquals(1, type.getParameters().size());
        assertEquals(TypeExtends.class, type.getParameters().get(0).getClass());
    }
    
    @Test
    public void Map_Field_Parameters() throws SecurityException, NoSuchFieldException {
        Field field = Examples.ComplexCollections.class.getDeclaredField("map2");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(2, type.getParameters().size());
        Type valueType = type.getParameters().get(1);
        assertEquals(1, valueType.getParameters().size());
        assertEquals(TypeExtends.class, valueType.getParameters().get(0).getClass());
    }
    
    @Test
    public void OrderBys() throws SecurityException, NoSuchFieldException {
        Field field = Examples.OrderBys.class.getDeclaredField("orderBy");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
    }
    
    @Test
    public void SubEntity() {
        Type type = factory.create(Examples.SubEntity.class);
        assertEquals(0, type.getParameters().size());
    }
    
    @Test
    public void AbstractEntity_Code() throws SecurityException, NoSuchFieldException {
        Field field = EmbeddedTest.AbstractEntity.class.getDeclaredField("code");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertTrue(type instanceof TypeExtends);
        assertEquals("C", ((TypeExtends)type).getVarName());
    }
    
    @Test
    public void SimpleTypes_classList5() throws SecurityException, NoSuchFieldException {
        Field field = Examples.SimpleTypes.class.getDeclaredField("classList5");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(TypeCategory.LIST, type.getCategory());
        Type parameter = type.getParameters().get(0);
        assertEquals(ClassType.class, parameter.getClass());
        assertEquals(TypeExtends.class, parameter.getParameters().get(0).getClass());
    }
    
    @Test
    public void Collection_Of_Collection() throws SecurityException, NoSuchFieldException {
        Field field = Examples.GenericRelations.class.getDeclaredField("col3");
        Type type = factory.create(field.getType(), field.getGenericType());
        assertEquals(1, type.getParameters().size());
        Type valueType = type.getParameters().get(0);
        assertEquals(TypeExtends.class, valueType.getParameters().get(0).getClass());
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
        assertEquals(TypeCategory.CUSTOM, factory.create(TypeFactoryTest.class).getCategory());
    }

}
