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

import java.util.*;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QCollectionTest_Classes;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.MapPath;
import com.querydsl.core.types.path.SetPath;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

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

    @QueryEntity
    public static class Classes {

        HashMap map1;

        HashMap<?,?> map2;

        HashMap<String, String> map3;

        ArrayList list1;

        ArrayList<?> list2;

        ArrayList<String> list3;

        HashSet set1;

        HashSet<?> set2;

        HashSet<String> set3;

    }
    
    @Test
    public void test() {
//        assertEquals(String.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));
//        assertEquals(Object.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));

        Assert.assertEquals(MapPath.class, QCollectionTest_Classes.classes.map1.getClass());
        assertEquals(MapPath.class, QCollectionTest_Classes.classes.map2.getClass());
        assertEquals(MapPath.class, QCollectionTest_Classes.classes.map3.getClass());

        assertEquals(ListPath.class, QCollectionTest_Classes.classes.list1.getClass());
        assertEquals(ListPath.class, QCollectionTest_Classes.classes.list2.getClass());
        assertEquals(ListPath.class, QCollectionTest_Classes.classes.list3.getClass());

        assertEquals(SetPath.class, QCollectionTest_Classes.classes.set1.getClass());
        assertEquals(SetPath.class, QCollectionTest_Classes.classes.set2.getClass());
        assertEquals(SetPath.class, QCollectionTest_Classes.classes.set3.getClass());

    }

}
