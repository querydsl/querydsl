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
package com.querydsl.core.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.PathBuilder;
import com.querydsl.core.types.path.PathBuilderFactory;
import com.querydsl.core.types.path.StringPath;


public class QBeanFieldAccessTest {

    public static class Entity {

        String name;

        String name2;

        int age;

        boolean married;
    }

    private PathBuilder<Entity> entity;

    private StringPath name, name2;

    private NumberPath<Integer> age;

    private BooleanPath married;

    @Before
    public void setUp() {
        entity = new PathBuilderFactory().create(Entity.class);
        name = entity.getString("name");
        name2 = entity.getString("name2");
        age = entity.getNumber("age", Integer.class);
        married = entity.getBoolean("married");
    }

    @Test
    public void with_Class_and_Exprs_using_fields() {
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.name);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);
    }

    @Test
    public void with_Path_and_Exprs_using_fields() {
        QBean<Entity> beanProjection = new QBean<Entity>(entity, true, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.name);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);
    }

    @Test
    public void with_Class_and_Map_using_fields() {
        Map<String,Expression<?>> bindings = new LinkedHashMap<String,Expression<?>>();
        bindings.put("name", name);
        bindings.put("age", age);
        bindings.put("married", married);
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, bindings);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.name);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);
    }

    @Test
    public void with_Class_and_Alias_using_fields() {
        StringPath name2 = new StringPath("name2");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, name.as(name2), age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertNull(bean.name);
        assertEquals("Fritz", bean.name2);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);
    }

    @Test
    public void with_Nested_FactoryExpression() {
        Map<String,Expression<?>> bindings = new LinkedHashMap<String,Expression<?>>();
        bindings.put("age", age);
        bindings.put("name", new Concatenation(name, name2));
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, bindings);
        FactoryExpression<Entity> wrappedProjection = FactoryExpressionUtils.wrap(beanProjection);
        Entity bean = wrappedProjection.newInstance(30, "Fri","tz");
        assertEquals("Fritz", bean.name);

    }
}
