package com.mysema.query.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class CollectionTest {
    
    @QueryEntity
    public class Person {

        Map<String, ?> map1;
    
        Map<?, String> map2;
        
        Map<?,?> map3;
        
        Map map4;
        
        List<?> list1;
        
        List list2;
        
        Collection<?> collection1;
        
        Collection collection2;
        
        Set<?> set1;
        
        Set set2;
    }
    
    @Test
    public void test() {
//        assertEquals(String.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));
//        assertEquals(Object.class, QMapWithUndefinedValueTest_Person.person.appData.getParameter(1));
    }

}
