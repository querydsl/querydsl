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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QExtendsAndSuperTest_ExtendsAndSuper;

public class ExtendsAndSuperTest {

    @QueryEntity
    public static class ExtendsAndSuper<A>{
        // col
        Collection<? extends A> extendsCol;
        Collection<? extends CharSequence> extendsCol2;
        Collection<? super A> superCol;
        Collection<? super String> superCol2;

        // list
        List<? extends A> extendsList;
        List<? extends CharSequence> extendsList2;
        List<? super A> superList;
        List<? super String> superList2;

        // set
        Set<? extends A> extendsSet;
        Set<? extends CharSequence> extendsSet2;
        Set<? super A> superSet;
        Set<? super String> superSet2;

        // map
        Map<String,? super A> superMap;
        Map<? super A, String> superMap2;
        Map<String,? extends A> extendsMap;
        Map<? extends A, String> extendsMap2;
    }

    @Test
    public void Validate() {
        QExtendsAndSuperTest_ExtendsAndSuper var = QExtendsAndSuperTest_ExtendsAndSuper.extendsAndSuper;
        assertEquals(Object.class, var.extendsCol.getElementType());
        assertEquals(CharSequence.class, var.extendsCol2.getElementType());

        assertEquals(Object.class, var.superCol.getElementType());
        assertEquals(Object.class, var.superCol2.getElementType());
    }

    @Test
    public void test() {
        QExtendsAndSuperTest_ExtendsAndSuper var = QExtendsAndSuperTest_ExtendsAndSuper.extendsAndSuper;
        ExtendsAndSuper<Object> entity = new ExtendsAndSuper<Object>();
        assertNotNull(var.eq(entity));
        assertNotNull(var.extendsMap.containsKey(""));
        assertNotNull(var.extendsMap2.containsValue(""));
    }

}
