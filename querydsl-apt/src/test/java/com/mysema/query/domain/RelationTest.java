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
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PSimple;

public class RelationTest extends AbstractTest{
    
    public enum MyEnum {
        VAR1, VAR2
    }

    @QueryEntity
    public static class Reference {

    }
    
    @QueryEntity
    public class GenericRelations{
        Collection<Collection<Reference>> col1;
        Collection<List<Reference>> col2;
        Collection<Collection<? extends Reference>> col3;
        Collection<List<? extends Reference>> col4;
        
        Set<List<Reference>> set1;
        Set<Collection<Reference>> set2;
        Set<List<? extends Reference>> set3;
        Set<Collection<? extends Reference>> set4;
        
        Map<String,List<String>> map1;
        Map<List<String>,String> map2;
        Map<String,List<? extends String>> map3;
        Map<List<? extends String>,String> map4;
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
    public void test() throws SecurityException, NoSuchFieldException{
        cl = QRelationType.class;
        match(PSimple.class, "enumProperty");
        match(PList.class, "enumList");
        match(PMap.class, "enumMap1");
        match(PMap.class, "enumMap");
        
        match(PList.class, "list");
        match(PList.class, "list2");
        match(PList.class, "list3");
        match(PList.class, "list4");
        match(PList.class, "list5");
        
        match(PCollection.class, "set");
        match(PCollection.class, "sortedSet");
        match(PCollection.class, "set2");
        match(PCollection.class, "set3");
        match(PCollection.class, "set4");
        
        match(PList.class, "listOfObjects");
        match(PCollection.class, "setOfObjects");
        match(PCollection.class, "setOfObjects2");
        
        match(PCollection.class, "collection");
        match(PCollection.class, "collection2");
        match(PCollection.class, "collection3");
        match(PCollection.class, "collection4");
        
        match(PMap.class, "map");
        match(PMap.class, "map2");
        match(PMap.class, "map3");
        match(PMap.class, "map4");
        match(PMap.class, "map5");
        match(PMap.class, "map6");
        match(PMap.class, "map7");
        match(PMap.class, "map8");
        match(PMap.class, "map9");
    }
    
    @Test
    public void listUsage(){
        String expected = "relationType.list.get(0).set"; 
        assertEquals(expected, QRelationType.relationType.list.get(0).set.toString());
        assertEquals(expected, QRelationType.relationType.list(0).set.toString());
        
        assertEquals(List.class, QRelationType.relationType.list.getType());
    }

}
