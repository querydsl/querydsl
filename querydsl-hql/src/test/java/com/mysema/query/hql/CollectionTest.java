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
        assertToString(":a1 in elements(cat.kittensSet)", cat.kittensSet.contains(new Cat()));
        assertToString(":a1 in elements(cat.kittens)", cat.kittens.contains(new Cat()));
        
        assertToString("cat in elements(cat1.kittens)", cat.in(cat1.kittens));
        assertToString("cat in elements(cat1.kittensSet)", cat.in(cat1.kittensSet));
    }

}
