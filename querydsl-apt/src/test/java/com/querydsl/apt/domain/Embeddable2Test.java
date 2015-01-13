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

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QuerySupertype;
import com.querydsl.core.domain.MyEmbeddable;
import com.querydsl.apt.domain.QEmbeddable2Test_SomeEntityClassHavingMyEmbeddable;
import com.querydsl.apt.domain.QEmbeddable2Test_SomeMappedSuperClassHavingMyEmbeddable;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.PathInits;

public class Embeddable2Test {
    
    @QuerySupertype
    public static abstract class SomeMappedSuperClassHavingMyEmbeddable {

        @QueryEmbedded
        MyEmbeddable embeddable;
    }
    
    @QueryEntity
    public static abstract class SomeEntityClassHavingMyEmbeddable {
        
        @QueryEmbedded
        MyEmbeddable embeddable;
        
    }
    
    @QueryEntity
    public static class SomeEntity extends SomeMappedSuperClassHavingMyEmbeddable {
        
    }
    
    @Test
    @Ignore
    public void MappedSuperClassConstructors() throws SecurityException, NoSuchMethodException {
        assertNotNull(QEmbeddable2Test_SomeMappedSuperClassHavingMyEmbeddable.class.getConstructor(
                Class.class, PathMetadata.class, PathInits.class));
    }
    
    @Test
    @Ignore
    public void EntityConstructors() throws SecurityException, NoSuchMethodException {
        assertNotNull(QEmbeddable2Test_SomeEntityClassHavingMyEmbeddable.class.getConstructor(
                Class.class, PathMetadata.class, PathInits.class));
    }

}
