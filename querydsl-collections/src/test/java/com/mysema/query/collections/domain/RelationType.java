/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.mysema.query.annotations.Entity;
import com.mysema.query.collections.domain.Host.Reference;
import com.mysema.query.collections.domain2.RelationType2;

@Entity
@SuppressWarnings("unchecked")
public class RelationType {

    Host.MyEnum enumProperty;
    List<Host.MyEnum> enumList;
    Map<String, Host.MyEnum> enumMap1;
    Map<Host.MyEnum, String> enumMap;

    // list
    List<RelationType> list;
    List<? extends RelationType> list2;
    List<String> list3;
    List<RelationType2> list4;
    List<Reference> list5;

    // set
    Set<RelationType> set;
    SortedSet<RelationType> sortedSet;
    Set<String> set2;
    Set<RelationType2> set3;
    Set<Reference> set4;

    // .. of Object
    List<Object> listOfObjects;
    Set<Object> setOfObjects;
    Set<Reference> setOfObjects2;

    // collection
    Collection<RelationType> collection;
    Collection<RelationType2> collection2;
    Collection<String> collection3;
    Collection<Reference> collection4;

    // map
    Map<String, RelationType> map;
    Map<RelationType, RelationType> map2;
    Map<RelationType, String> map3;

    Map<String, RelationType2> map4;
    Map<RelationType2, RelationType2> map5;
    Map<RelationType2, String> map6;

    Map<String, Reference> map7;
    Map<Reference, Reference> map8;
    Map<Reference, String> map9;
}