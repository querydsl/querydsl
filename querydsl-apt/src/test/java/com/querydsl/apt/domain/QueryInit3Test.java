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

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;

public class QueryInit3Test {

    @QueryEntity
    public static class Entity {

        @QueryInit("*.*")
        Entity prop1;

        @QueryInit("*")
        Entity prop2;

        List<Entity> entityList;

        Map<String,Entity> entityMap;
    }
    
    @Test
    public void test() {
        assertEquals("entity.prop1.prop2.prop1", QQueryInit3Test_Entity.entity.prop1.prop2.prop1.toString());
    }
    
}
