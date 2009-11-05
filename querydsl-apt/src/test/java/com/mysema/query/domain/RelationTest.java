package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.domain.rel.RelationType2;
import com.mysema.query.types.path.PComponentCollection;
import com.mysema.query.types.path.PComponentList;
import com.mysema.query.types.path.PComponentMap;
import com.mysema.query.types.path.PEntityCollection;
import com.mysema.query.types.path.PEntityList;
import com.mysema.query.types.path.PEntityMap;
import com.mysema.query.types.path.PSimple;

public class RelationTest extends AbstractTest{
    
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
        
        List<List<Reference>> list6;
        List<Collection<Reference>> list7;

        // set
        Set<RelationType> set;
        SortedSet<RelationType> sortedSet;
        Set<String> set2;
        Set<RelationType2<?>> set3;
        Set<Reference> set4;
        
        Set<List<Reference>> set5;
        Set<Collection<Reference>> set6;

        // .. of Object
        List<Object> listOfObjects;
        Set<Object> setOfObjects;
        Set<Reference> setOfObjects2;        
        

        // collection
        Collection<RelationType> collection;
        Collection<RelationType2<?>> collection2;
        Collection<String> collection3;
        Collection<Reference> collection4;
        
        Collection<Collection<Reference>> col5;
        Collection<List<Reference>> col6;

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
        
        Map<String,List<String>> map10;
        Map<List<String>,String> map11;
    }
    
    @Test
    public void test() throws SecurityException, NoSuchFieldException{
        cl = QRelationType.class;
        match(PSimple.class, "enumProperty");
        match(PComponentList.class, "enumList");
        match(PComponentMap.class, "enumMap1");
        match(PComponentMap.class, "enumMap");
        
        match(PEntityList.class, "list");
        match(PEntityList.class, "list2");
        match(PComponentList.class, "list3");
        match(PEntityList.class, "list4");
        match(PEntityList.class, "list5");
        
        match(PEntityCollection.class, "set");
        match(PEntityCollection.class, "sortedSet");
        match(PComponentCollection.class, "set2");
        match(PEntityCollection.class, "set3");
        match(PEntityCollection.class, "set4");
        
        match(PComponentList.class, "listOfObjects");
        match(PComponentCollection.class, "setOfObjects");
        match(PEntityCollection.class, "setOfObjects2");
        
        match(PEntityCollection.class, "collection");
        match(PEntityCollection.class, "collection2");
        match(PComponentCollection.class, "collection3");
        match(PEntityCollection.class, "collection4");
        
        match(PEntityMap.class, "map");
        match(PEntityMap.class, "map2");
        match(PComponentMap.class, "map3");
        match(PEntityMap.class, "map4");
        match(PEntityMap.class, "map5");
        match(PComponentMap.class, "map6");
        match(PEntityMap.class, "map7");
        match(PEntityMap.class, "map8");
        match(PComponentMap.class, "map9");
    }
    
    @Test
    public void listUsage(){
        String expected = "relationType.list.get(0).set"; 
        assertEquals(expected, QRelationType.relationType.list.get(0).set.toString());
        assertEquals(expected, QRelationType.relationType.list(0).set.toString());
    }

}
