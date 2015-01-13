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

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.apt.domain.QSuperclass5Test_Embeddable;
import com.querydsl.apt.domain.QSuperclass5Test_SuperClass;
import com.querydsl.core.types.PathMetadataFactory;

public class Superclass5Test {
    
    public static class SuperClass {
        
        String superClassProperty;
        
    }
    
    @QueryEmbeddable
    public static class Embeddable extends SuperClass {
        
        String embeddableProperty;
        
    }

    @Test
    public void SuperClass_Properties() {
        QSuperclass5Test_SuperClass qtype = new QSuperclass5Test_SuperClass(PathMetadataFactory.forVariable("var"));
        assertNotNull(qtype.superClassProperty);
    }
    
    @Test
    public void Entity_Properties() {
        QSuperclass5Test_Embeddable qtype = new QSuperclass5Test_Embeddable(PathMetadataFactory.forVariable("var"));
        assertNotNull(qtype.superClassProperty);
        assertNotNull(qtype.embeddableProperty);
    }

}
