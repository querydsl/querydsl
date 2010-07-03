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

import com.mysema.query.types.expr.QBean;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;
import com.mysema.query.types.path.PathBuilderFactory;


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
    
    private PathBuilder<Entity> entity;
    
    private PString name;
    
    private PNumber<Integer> age;
    
    private PBoolean married;
    
    @Before
    public void setUp(){
        entity = new PathBuilderFactory().create(Entity.class);
        name = entity.getString("name");
        age = entity.getNumber("age", Integer.class);
        married = entity.getBoolean("married");
    }
    
    @Test
    public void with_Class_and_Exprs(){                
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());        
    }

    @Test
    public void with_Path_and_Exprs(){                
        QBean<Entity> beanProjection = new QBean<Entity>(entity, name, age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertEquals("Fritz", bean.getName());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());        
    }
    
    @Test
    public void with_Class_and_Map(){
        Map<String,Expr<?>> bindings = new LinkedHashMap<String,Expr<?>>();
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
    public void with_Class_and_Alias(){
        PString name2 = new PString("name2");
        QBean<Entity> beanProjection = new QBean<Entity>(Entity.class, name.as(name2), age, married);
        Entity bean = beanProjection.newInstance("Fritz", 30, true);
        assertNull(bean.getName());
        assertEquals("Fritz", bean.getName2());
        assertEquals(30, bean.getAge());
        assertEquals(true, bean.isMarried());
    }
}
