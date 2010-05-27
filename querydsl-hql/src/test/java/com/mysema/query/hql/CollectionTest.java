/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import org.junit.Test;

import com.mysema.query.hql.domain.Cat;

public class CollectionTest extends AbstractQueryTest{
    
    @Test
    public void test(){
        assertToString(":a1 member of cat.kittensSet", cat.kittensSet.contains(new Cat()));
        assertToString(":a1 member of cat.kittens", cat.kittens.contains(new Cat()));
        
        assertToString("cat member of cat1.kittens", cat.in(cat1.kittens));
        assertToString("cat member of cat1.kittensSet", cat.in(cat1.kittensSet));
    }

    @Test
    public void testCollectionOperations() {
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
