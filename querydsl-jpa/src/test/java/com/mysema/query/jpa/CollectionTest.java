/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.jpa.domain.Cat;

public class CollectionTest extends AbstractQueryTest{

    @Test
    public void InElements(){
        assertToString(":a1 in elements(cat.kittensSet)", cat.kittensSet.contains(new Cat()));
        assertToString(":a1 in elements(cat.kittens)", cat.kittens.contains(new Cat()));

        assertToString("cat in elements(cat1.kittens)", cat.in(cat1.kittens));
        assertToString("cat in elements(cat1.kittensSet)", cat.in(cat1.kittensSet));
    }

    @Test
    public void CollectionOperations() {
        // HQL functions that take collection-valued path expressions: size(),
        // minelement(), maxelement(), minindex(), maxindex(), along with the
        // special elements() and indices functions which may be quantified
        // using some, all, exists, any, in.
        cat.kittens.size();
//        minelement(cat.kittens);
//        maxelement(cat.kittens);
//        minindex(cat.kittens);
//        maxindex(cat.kittens);
        assertToString("cat.kittens[0]", cat.kittens(0));
        assertToString("cat.kittens[0]", cat.kittens.get(0));

        // some, all, exists, any, in.
    }
}
