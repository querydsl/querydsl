/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class CollectionTest {

    private final QCat cat = new QCat("cat");

    private final QCat other = new QCat("other");

    private List<Cat> cats;

    @Before
    public void setUp() {
        Cat cat1 = new Cat("1");
        cat1.setKittens(Collections.singletonList(cat1));
        Cat cat2 = new Cat("2");
        cat2.setKittens(Arrays.asList(cat1, cat2));
        Cat cat3 = new Cat("3");
        cat3.setKittens(Arrays.asList(cat1, cat2, cat3));
        Cat cat4 = new Cat("4");
        cat4.setKittens(Arrays.asList(cat1, cat2, cat3, cat4));

        cats = Arrays.asList(cat1, cat2, cat3, cat4);
    }

    @Test
    public void join() {
        assertEquals("4", CollQueryFactory.from(cat, cats)
                .innerJoin(cat.kittens, other)
                .where(other.name.eq("4")).select(cat.name).fetchFirst().get());
    }

    @Test
    public void join_from_two_sources() {
        QCat catKittens = new QCat("cat_kittens");
        QCat otherKittens = new QCat("other_kittens");
        assertEquals(30, CollQueryFactory
                .from(cat, cats).from(other, cats)
                .innerJoin(cat.kittens, catKittens)
                .innerJoin(other.kittens, otherKittens)
                .where(catKittens.eq(otherKittens)).fetchCount());
    }

    @Test
    public void any_uniqueResult() {
        assertEquals("4", CollQueryFactory.from(cat, cats)
                .where(cat.kittens.any().name.eq("4")).select(cat.name).fetchOne().get());
    }

    @Test
    public void any_count() {
        assertEquals(4, CollQueryFactory.from(cat, cats)
                .where(cat.kittens.any().name.isNotNull()).fetchCount());
    }

    @Test
    public void any_two_levels() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where(
                cat.kittens.any().kittens.any().isNotNull()).fetchCount());
    }

    @Test
    public void any_two_levels2() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where(
                cat.kittens.any().name.isNotNull(),
                cat.kittens.any().kittens.any().isNotNull()).fetchCount());
    }

    @Test
    public void any_from_two_sources() {
        assertEquals(16, CollQueryFactory.from(cat, cats).from(other, cats).where(
                cat.kittens.any().name.eq(other.kittens.any().name)).fetchCount());
    }

    @Test
    public void list_size() {
        assertEquals(4, CollQueryFactory.from(cat, cats).where(cat.kittens.size().gt(0)).fetchCount());
        assertEquals(2, CollQueryFactory.from(cat, cats).where(cat.kittens.size().gt(2)).fetchCount());
    }

    @Test
    public void list_is_empty() {
        assertEquals(0, CollQueryFactory.from(cat, cats).where(cat.kittens.isEmpty()).fetchCount());
    }

}
