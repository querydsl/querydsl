/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;


/**
 * ClassModelTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class ClassModelTest {

    /**
     * Test.
     */
    @Test
    public void test() {        
        BeanModelFactory factory = new BeanModelFactory(new TypeModelFactory(QueryEntity.class));
        BeanModel type = factory.create(TestType.class, "Q");
//        assertEquals(1, type.getEntityMaps().size());
//        assertEquals(1, type.getSimpleMaps().size());
//        assertEquals(2, type.getEntityCollections().size());
//        assertEquals(2, type.getSimpleCollections().size());
//        assertEquals(1, type.getEntityLists().size());
//        assertEquals(1, type.getSimpleLists().size());
        assertEquals(1, type.getEntityProperties().size());
//        assertEquals(1, type.getStringProperties().size());
//        assertEquals(2, type.getNumericProperties().size());
//        assertEquals(3, type.getSimpleProperties().size());
    }

    /**
     * The Class TestType.
     */
    @QueryEntity
    public static class TestType {
        // entity map
        public Map<String, TestType> map1;
        // simple map
        public Map<String, String> map2;
        // entity col
        public Collection<TestType> col1;
        
        public Set<TestType> set1;
        // simple col
        public Collection<Object> col2;
        
        public Set<Object> set2;
        // entity list
        public List<TestType> list1;
        // simple list
        public List<Object> list2;
        // entity
        public TestType ref;
        // string
        public String str;
        // numeric
        public int intField;
        
        public Integer intField2;
        // simple
        public Class<?> cl;
        
        public Object o;
        
        public Locale l;
    }

}
