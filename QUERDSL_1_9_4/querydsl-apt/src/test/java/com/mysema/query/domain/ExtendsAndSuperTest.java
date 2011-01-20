/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class ExtendsAndSuperTest {

    @QueryEntity
    public class ExtendsAndSuper<A>{
        // col
        Collection<? extends A> extendsCol;
        Collection<? extends CharSequence> extendsCol2;
        Collection<? super A> superCol;
        Collection<? super String> superCol2;

        // list
        List<? extends A> extendsList;
        List<? extends CharSequence> extendsList2;
        List<? super A> superList;
        List<? super String> superList2;

        // set
        Set<? extends A> extendsSet;
        Set<? extends CharSequence> extendsSet2;
        Set<? super A> superSet;
        Set<? super String> superSet2;

        // map
        Map<String,? super A> superMap;
        Map<? super A, String> superMap2;
        Map<String,? extends A> extendsMap;
        Map<? extends A, String> extendsMap2;
    }

    @Test
    public void validate(){
        QExtendsAndSuperTest_ExtendsAndSuper var = QExtendsAndSuperTest_ExtendsAndSuper.extendsAndSuper;
        assertEquals(Object.class, var.extendsCol.getElementType());
        assertEquals(CharSequence.class, var.extendsCol2.getElementType());

        assertEquals(Object.class, var.superCol.getElementType());
        assertEquals(Object.class, var.superCol2.getElementType());
    }

    @Test
    public void test(){
        QExtendsAndSuperTest_ExtendsAndSuper var = QExtendsAndSuperTest_ExtendsAndSuper.extendsAndSuper;
        ExtendsAndSuper<Object> entity = new ExtendsAndSuper<Object>();
        var.eq(entity);

        var.extendsMap.containsKey("");
        var.extendsMap2.containsValue("");
    }

}
