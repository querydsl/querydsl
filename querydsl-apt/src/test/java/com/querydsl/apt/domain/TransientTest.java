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

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryType;
import com.querydsl.apt.domain.QTransientTest_ExampleEntity;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class TransientTest {
    
    @Entity
    public static class ExampleEntity {
        
        @QueryType(PropertyType.SIMPLE)
        @Transient
        String property1;
        
        @Transient
        String property2;
        
        @QueryType(PropertyType.SIMPLE)
        transient String property3;
        
        transient String property4;
    }
    
    @Test
    public void test() {
        assertNotNull(QTransientTest_ExampleEntity.exampleEntity.property1);
        assertNotNull(QTransientTest_ExampleEntity.exampleEntity.property3);
    }

}
