/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.apt.general;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.apt.model.Field;
import com.mysema.query.apt.model.Type;

/**
 * TypeHelperTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class TypeHelperTest {

    @Test
    public void test() {
        Type type = TypeFactory.createType(TestType.class);
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
