/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;


public class QBeanFieldAccessTest {
    
    public static class Entity {
        
        String name;
        
        String name2;
        
        int age;
        
        boolean married;
    }
    
    private PathBuilder<Entity> entity;
    
    private StringPath name;
    
    private NumberPath<Integer> age;
    
    private BooleanPath married;
    
    @Before
    public void setUp(){
        entity = new PathBuilderFactory().create(Entity.class);
        name = entity.getString("name");
        age = entity.getNumber("age", Integer.class);
        married = entity.getBoolean("married");
    }
        
    @Test
    public void with_Class_and_Exprs_using_fields(){                
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.name);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);        
    }

    @Test
    public void with_Path_and_Exprs_using_fields(){                
        QBean<Entity> beanProjection = new QBean<Entity>(entity, true, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.name);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);        
    }
        
    @Test
    public void with_Class_and_Map_using_fields(){
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
    public void with_Class_and_Alias_using_fields(){
        StringPath name2 = new StringPath("name2");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, true, name.as(name2), age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertNull(bean.name);
        assertEquals("Fritz", bean.name2);
        assertEquals(30, bean.age);
        assertEquals(true, bean.married);
    }
}
