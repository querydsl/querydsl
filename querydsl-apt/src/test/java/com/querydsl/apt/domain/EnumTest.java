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

import static org.junit.Assert.*;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QEnumTest_Gender;

public class EnumTest {
    
    @QueryEntity
    public enum Gender {
        MALE,
        FEMALE
    }
    
    @QueryEmbeddable
    public enum Gender2 {
        MALE,
        FEMALE
    }
    
    @QueryEntity
    public static class Bean {
        Gender gender;
    }
    
    @Test
    public void Enum_as_Comparable() {
        assertNotNull(QEnumTest_Gender.gender.asc());
    }
    
    @Test
    public void EnumOrdinal_as_Comparable() {
        assertNotNull(QEnumTest_Gender.gender.ordinal().asc());        
    }

}
