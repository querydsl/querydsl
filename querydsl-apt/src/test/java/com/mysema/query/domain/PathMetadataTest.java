/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.expr.StringConstant;
import com.mysema.query.types.expr.StringExpression;

public class PathMetadataTest {

    @Before
    public void setUp(){
        assertNotNull(QAnimalTest_Animal.animal);
        assertNotNull(QAnimalTest_Cat.cat);
        assertNotNull(QConstructorTest_Category.category);
        assertNotNull(QSimpleTypesTest_SimpleTypes.simpleTypes);
    }

    @SuppressWarnings("unchecked")
    @Test
    @Ignore
    public void test() throws Exception{
        Field field = StringConstant.class.getDeclaredField("CACHE");
        field.setAccessible(true);
        Map<String, StringExpression> cache = (Map) field.get(null);
        System.out.println(cache.size() + " entries in EString cache");

        // numbers
        assertTrue(cache.containsKey("0"));
        assertTrue(cache.containsKey("10"));

        // variables
        assertTrue(cache.containsKey("animal"));
        assertTrue(cache.containsKey("cat"));
        assertTrue(cache.containsKey("category"));
        assertTrue(cache.containsKey("simpleTypes"));

        // properties
        assertTrue(cache.containsKey("mate"));

    }

}
