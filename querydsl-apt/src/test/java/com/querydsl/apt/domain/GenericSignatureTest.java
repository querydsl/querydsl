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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;

public class GenericSignatureTest {
    
    @QueryEntity
    @SuppressWarnings("unchecked")
    public static class Entity<T extends Entity<T>> {
        
        // collection        
        Collection<Entity> rawCollection;       
        
        Collection<Entity<T>> genericCollection;        
        
        Collection<T> genericCollection2;
        
        // list
        List<Entity> rawList;        
        
        List<Entity<T>> genericList;        
        
        List<T> genericList2;
        
        // set
        Set<Entity> rawSet;        
        
        Set<Entity<T>> genericSet;        
        
        Set<T> genericSet2;
        
        // map
        Map<String,Entity> rawMap;        
        
        Map<String,Entity<T>> genericMap;        
        
        Map<String,T> genericMap2;
    }
    
    @Test
    public void test() {
        QGenericSignatureTest_Entity entity = QGenericSignatureTest_Entity.entity;
        // collection
        assertEquals(Entity.class, entity.rawCollection.getParameter(0));
        assertEquals(Entity.class, entity.genericCollection.getParameter(0));
        assertEquals(Entity.class, entity.genericCollection2.getParameter(0));
        
        // list
        assertEquals(Entity.class, entity.rawList.getParameter(0));
        assertEquals(Entity.class, entity.genericList.getParameter(0));
        assertEquals(Entity.class, entity.genericList2.getParameter(0));
        
        // set
        assertEquals(Entity.class, entity.rawSet.getParameter(0));
        assertEquals(Entity.class, entity.genericSet.getParameter(0));
        assertEquals(Entity.class, entity.genericSet2.getParameter(0));
        
        // map
        assertEquals(Entity.class, entity.rawMap.getParameter(1));
        assertEquals(Entity.class, entity.genericMap.getParameter(1));
        assertEquals(Entity.class, entity.genericMap2.getParameter(1));
    }

}
