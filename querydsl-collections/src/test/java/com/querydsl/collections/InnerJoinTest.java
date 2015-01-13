/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.collections;

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
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
    public void setUp() {
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
    public void List() {
        List<Cat> rv = CollQueryFactory.from(cat, cats)
            .innerJoin(cat.kittens, kitten)
            .where(cat.name.eq(kitten.name))
            .orderBy(cat.name.asc())
            .list(cat);
        assertEquals("Bob", rv.get(0).getName());
        assertEquals("Kate", rv.get(1).getName());

    }
        
    @Test
    public void Alias() {
        Cat cc = alias(Cat.class, "cat1");
        Cat ck = alias(Cat.class, "cat2");
        List<Cat> rv =  CollQueryFactory.from($(cc), cats)
                        .innerJoin($(cc.getKittens()), $(ck))
                        .where($(cc.getName()).eq($(ck.getName())))
                        .list($(cc));
        assertFalse(rv.isEmpty());
    }


    @Test
    public void Map() {
        List<Cat> rv = CollQueryFactory.from(cat, cats)
            .innerJoin(cat.kittensByName, kitten)
            .where(cat.name.eq(kitten.name))
            .orderBy(cat.name.asc())
            .list(cat);
        assertEquals("Bob", rv.get(0).getName());
        assertEquals("Kate", rv.get(1).getName());
    }

}
