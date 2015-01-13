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

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QQueryEmbeddedTest_Parent;
import com.querydsl.apt.domain.QQueryEmbeddedTest_Parent2;

public class QueryEmbeddedTest {

    @QueryEntity
    public static class Parent {
    
        String parentProperty;
        
        @QueryEmbedded
        Child child;
        
    }

    @QueryEntity
    public static class Parent2 {
    
        String parentProperty;
        
        @QueryEmbedded
        List<Child> children;
        
        @QueryEmbedded
        Map<String,Child> children2;
        
    }
    
    public static class Child {
        
        String childProperty;
        
    }
    
    @Test
    public void Parent_Child_ChildProperty() {
        assertNotNull(QQueryEmbeddedTest_Parent.parent.child.childProperty);
    }
    
    @Test
    public void Parent_Children_Any_ChildrenProperty() {
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children.any().childProperty);
    }
    
    @Test
    public void Parent_Children2_MapAccess() {
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children2.containsKey("XXX"));
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children2.get("XXX").childProperty);
    }
}
