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
        ClassModelFactory factory = new ClassModelFactory(new TypeModelFactory(QueryEntity.class));
        ClassModel type = factory.create(TestType.class, "Q");
        assertEquals(1, type.getEntityMaps().size());
        assertEquals(1, type.getSimpleMaps().size());
        assertEquals(2, type.getEntityCollections().size());
        assertEquals(2, type.getSimpleCollections().size());
        assertEquals(1, type.getEntityLists().size());
        assertEquals(1, type.getSimpleLists().size());
        assertEquals(1, type.getEntityFields().size());
        assertEquals(1, type.getStringFields().size());
        assertEquals(2, type.getNumericFields().size());
        assertEquals(3, type.getSimpleFields().size());
    }

    /**
     * The Class TestType.
     */
    @QueryEntity
    public static class TestType {
        // entity map
        /** The map1. */
        public Map<String, TestType> map1;
        // simple map
        /** The map2. */
        public Map<String, String> map2;
        // entity col
        /** The col1. */
        public Collection<TestType> col1;
        
        /** The set1. */
        public Set<TestType> set1;
        // simple col
        /** The col2. */
        public Collection<Object> col2;
        
        /** The set2. */
        public Set<Object> set2;
        // entity list
        /** The list1. */
        public List<TestType> list1;
        // simple list
        /** The list2. */
        public List<Object> list2;
        // entity
        /** The ref. */
        public TestType ref;
        // string
        /** The str. */
        public String str;
        // numeric
        /** The int field. */
        public int intField;
        
        /** The int field2. */
        public Integer intField2;
        // simple
        /** The cl. */
        public Class<?> cl;
        
        /** The o. */
        public Object o;
        
        /** The l. */
        public Locale l;
    }

}
