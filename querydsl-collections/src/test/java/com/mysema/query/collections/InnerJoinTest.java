/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class InnerJoinTest extends AbstractQueryTest{

    private QCat cat, kitten;

    private List<Cat> cats;

    @Before
    public void setUp(){
        super.setUp();
        cat = new QCat("c");
        kitten = new QCat("k");
        Cat bob = new Cat("Bob");
        Cat bob2 = new Cat("Bob");
        Cat kate = new Cat("Kate");
        Cat kate2 = new Cat("Kate");
        Cat franz = new Cat("Franz");

        bob.setKittens(Collections.singletonList(bob2));
        bob.setKittensByName(Collections.singletonMap(bob2.getName(), bob2));
        kate.setKittens(Collections.singletonList(kate2));
        kate.setKittensByName(Collections.singletonMap(kate2.getName(), kate));
        cats = Arrays.asList(bob, bob2, kate, kate2, franz);
    }

    @Test
    public void List(){
        List<Cat> rv = MiniApi.from(cat, cats)
            .innerJoin(cat.kittens, kitten)
            .where(cat.name.eq(kitten.name))
            .orderBy(cat.name.asc())
            .list(cat);
        assertEquals("Bob", rv.get(0).getName());
        assertEquals("Kate", rv.get(1).getName());

    }
        
    @Test
    public void Alias(){
        Cat cc = alias(Cat.class, "cat1");
        Cat ck = alias(Cat.class, "cat2");
        List<Cat> rv =  MiniApi.from($(cc), cats)
                        .innerJoin($(cc.getKittens()), $(ck))
                        .where($(cc.getName()).eq($(ck.getName())))
                        .list($(cc));
        assertFalse(rv.isEmpty());
    }


    @Test
    public void Map(){
        List<Cat> rv = MiniApi.from(cat, cats)
            .innerJoin(cat.kittensByName, kitten)
            .where(cat.name.eq(kitten.name))
            .orderBy(cat.name.asc())
            .list(cat);
        assertEquals("Bob", rv.get(0).getName());
        assertEquals("Kate", rv.get(1).getName());
    }

}
