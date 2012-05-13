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
package com.mysema.query.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class CollectionTest {
    
    @QueryEntity
    public static class Person {

        Map<String, ?> map1;
    
        Map<?, String> map2;
        
        Map<?,?> map3;
        
        Map map4;
        
        List<?> list1;
        
        List list2;
        
        Collection<?> collection1;
        
        Collection collection2;
        
        Collection<Collection<Person>> collectionOfCOllection;
        
        Collection<Set<String>> collectionOfSet;
        
        Set<?> set1;
        
        Set set2;
    }
    
    @Test
    public void test() {
//        assertEquals(String.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));
//        assertEquals(Object.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));
    }

}
