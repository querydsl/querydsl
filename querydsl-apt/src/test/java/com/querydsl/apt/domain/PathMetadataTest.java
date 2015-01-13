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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.apt.domain.QAnimalTest_Animal;
import com.querydsl.apt.domain.QAnimalTest_Cat;
import com.querydsl.apt.domain.QConstructorTest_Category;
import com.querydsl.apt.domain.QSimpleTypesTest_SimpleTypes;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.expr.StringExpression;

@Ignore
public class PathMetadataTest {

    @Before
    public void setUp() {
        assertNotNull(QAnimalTest_Animal.animal);
        assertNotNull(QAnimalTest_Cat.cat);
        assertNotNull(QConstructorTest_Category.category);
        assertNotNull(QSimpleTypesTest_SimpleTypes.simpleTypes);
    }

    @SuppressWarnings("unchecked")
    @Test    
    public void test() throws Exception {
        Field field = ConstantImpl.class.getDeclaredField("STRINGS");
        field.setAccessible(true);
        Map<String, StringExpression> cache = (Map) field.get(null);
        System.out.println(cache.size() + " entries in ConstantImpl string cache");

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
