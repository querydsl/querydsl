package com.mysema.query.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.rel.RelationType2;

public class RelationTest {
    
    public enum MyEnum {
        VAR1, VAR2
    }

    @QueryEntity
    public static class Reference {

    }
    
    @QueryEntity
    public class RelationType {

        MyEnum enumProperty;
        List<MyEnum> enumList;
        Map<String, MyEnum> enumMap1;
        Map<MyEnum, String> enumMap;

        // list
        List<RelationType> list;
        List<? extends RelationType> list2;
        List<String> list3;
        List<RelationType2<?>> list4;
        List<Reference> list5;

        // set
        Set<RelationType> set;
        SortedSet<RelationType> sortedSet;
        Set<String> set2;
        Set<RelationType2<?>> set3;
        Set<Reference> set4;

        // .. of Object
        List<Object> listOfObjects;
        Set<Object> setOfObjects;
        Set<Reference> setOfObjects2;

        // collection
        Collection<RelationType> collection;
        Collection<RelationType2<?>> collection2;
        Collection<String> collection3;
        Collection<Reference> collection4;

        // map
        Map<String, RelationType> map;
        Map<RelationType, RelationType> map2;
        Map<RelationType, String> map3;

        Map<String, RelationType2<?>> map4;
        Map<RelationType2<?>, RelationType2<?>> map5;
        Map<RelationType2<?>, String> map6;

        Map<String, Reference> map7;
        Map<Reference, Reference> map8;
        Map<Reference, String> map9;
    }
    
    @Test
    public void test(){
        
    }

}
