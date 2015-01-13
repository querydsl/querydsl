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

import java.util.LinkedHashMap;
import java.util.Map;

import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.path.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class QBeanTest {

    public static class Entity {

        private String name;

        private String name2;

        private int age;

        private boolean married;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isMarried() {
            return married;
        }

        public void setMarried(boolean married) {
            this.married = married;
        }

        public String getName2() {
            return name2;
        }

        public void setName2(String name2) {
            this.name2 = name2;
        }

    }
    
    public static class SubEntity extends Entity{
        
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
    public void with_Class_and_Exprs() {
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }

    @Test
    public void with_Path_and_Exprs() {
        QBean<Entity> beanProjection = new QBean<Entity>(entity, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }

    @Test
    public void with_unknown_properties() {
        QBean<Entity> beanProjection = new QBean<Entity>(entity, name, age, Expressions.booleanPath("unknown"));
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
    }

    @Test
    public void with_Class_and_Map() {
        Map<String,Expression<?>> bindings = new LinkedHashMap<String,Expression<?>>();
        bindings.put("name", name);
        bindings.put("age", age);
        bindings.put("married", married);
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, bindings);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }

    @Test
    public void with_Class_and_Alias() {
        StringPath name2 = new StringPath("name2");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, name.as(name2), age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertNull(bean.getName());
        assertEquals("Fritz", bean.getName2());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }

    @Test
    public void with_Nested_FactoryExpression() {
        Map<String,Expression<?>> bindings = new LinkedHashMap<String,Expression<?>>();
        bindings.put("age", age);
        bindings.put("name", new Concatenation(name, name2));
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, bindings);
        FactoryExpression<Entity> wrappedProjection = FactoryExpressionUtils.wrap(beanProjection);
        Entity bean = wrappedProjection.newInstance(30, "Fri","tz");
        assertEquals("Fritz", bean.getName());

    }

    @Test
    public void with_Nested_FactoryExpression2() {
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class,
                age, ExpressionUtils.as(new Concatenation(name, name2), "name"));
        FactoryExpression<Entity> wrappedProjection = FactoryExpressionUtils.wrap(beanProjection);
        Entity bean = wrappedProjection.newInstance(30, "Fri","tz");
        assertEquals("Fritz", bean.getName());
    }
    
    @Test
    public void Supertype_Population() {
        QBean<SubEntity> beanProjection = new QBean<SubEntity>(SubEntity.class, true, name, age, married);
        SubEntity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }

    @Test
    public void skipNulls() {
        QBean bean = Projections.bean(Object.class);
        assertEquals(bean, bean);
        assertEquals(bean.skipNulls(), bean.skipNulls());
        assertFalse(bean.skipNulls().equals(bean));
        assertFalse(bean.equals(bean.skipNulls()));
    }

}
