package com.mysema.query.collections.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.mysema.query.annotations.Entity;

@Entity
public class RelationType {
    // list
    List<RelationType> list;
    List<? extends RelationType> list2;
    List<String> list3;

    // set
    Set<RelationType> set;
    SortedSet<RelationType> sortedSet;
    Set<String> set2;

    // .. of Object
    List<Object> listOfObjects;
    Set<Object> setOfObjects;

    // collection
    Collection<RelationType> collection;
    Collection<String> set3;

    // map
    Map<String, RelationType> map;
    Map<RelationType, RelationType> map2;
    Map<RelationType, String> map3;
}