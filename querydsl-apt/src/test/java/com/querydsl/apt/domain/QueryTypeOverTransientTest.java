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

import org.junit.Test;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.domain.QQueryTypeOverTransientTest_Entity;
import com.querydsl.apt.domain.QQueryTypeOverTransientTest_Entity2;

public class QueryTypeOverTransientTest {
    
    @QueryEntity
    public static class Entity {
        
        @QueryType(PropertyType.ENTITY)
        @QueryTransient
        Entity reference;
        
    }
    
    @QueryEntity 
    public static abstract class  Entity2 {
        
        @QueryType(PropertyType.ENTITY)
        @QueryTransient
        public abstract Entity getReference();
        
    }
    
    @Test
    public void Entity_Reference_Is_Available() {
        assertNotNull(QQueryTypeOverTransientTest_Entity.entity.reference);
    }
    
    @Test
    public void Entity2_Reference_Is_Available() {
        assertNotNull(QQueryTypeOverTransientTest_Entity2.entity2.reference);
    }

}
