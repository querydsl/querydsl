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

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.apt.domain.QEntityInheritanceTest_TestEntity;

public class EntityInheritanceTest {
    
    @MappedSuperclass
    public static class TreeEntity<T extends TreeEntity<T>> {
        
        Integer id;
        
        T parent;
        
    }

    @Entity
    public static class TestEntity extends TreeEntity<TestEntity> {
        
        String name;
        
    }
    
    @Test
    public void test() {
        Assert.assertEquals(
                QEntityInheritanceTest_TestEntity.class,
                QEntityInheritanceTest_TestEntity.testEntity.parent.getClass());
    }

}
