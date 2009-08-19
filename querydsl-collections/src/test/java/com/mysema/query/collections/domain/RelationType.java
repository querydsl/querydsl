package com.mysema.query.collections.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.mysema.query.annotations.Entity;
import com.mysema.query.collections.domain2.RelationType2;

@Entity
@SuppressWarnings("unchecked")
public class RelationType {
    // list
    List<RelationType> list;
    List<? extends RelationType> list2;
    List<String> list3;    
    List<RelationType2> list4;

    // set
    Set<RelationType> set;
    SortedSet<RelationType> sortedSet;
    Set<String> set2;
    Set<RelationType2> set3;

    // .. of Object
    List<Object> listOfObjects;
    Set<Object> setOfObjects;

    // collection
    Collection<RelationType> collection;
    Collection<RelationType2> collection2;
    Collection<String> set4;

    // map
    Map<String, RelationType> map;
    Map<RelationType, RelationType> map2;
    Map<RelationType, String> map3;
    
    Map<String, RelationType2> map4;
    Map<RelationType2, RelationType2> map5;
    Map<RelationType2, String> map6;
}