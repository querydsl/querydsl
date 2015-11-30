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

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OrderTest extends AbstractQueryTest {

    @Test
    public void test() {
        query().from(cat, cats).orderBy(cat.name.asc()).select(cat.name).fetch();
        assertArrayEquals(new Object[] {"Alex", "Bob", "Francis", "Kitty"}, last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.desc()).select(cat.name).fetch();
        assertArrayEquals(new Object[] {"Kitty", "Francis", "Bob", "Alex"}, last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.substring(1).asc()).select(cat.name).fetch();
        assertArrayEquals(new Object[] {"Kitty", "Alex", "Bob", "Francis"}, last.res.toArray());

        query().from(cat, cats).from(otherCat, cats)
               .orderBy(cat.name.asc(), otherCat.name.desc())
               .select(cat.name, otherCat.name).fetch();

        // TODO : more tests
    }

    @Test
    public void test2() {
        List<String> orderedNames = Arrays.asList("Alex","Bob","Francis","Kitty");
        assertEquals(orderedNames, query().from(cat,cats).orderBy(cat.name.asc()).select(cat.name).fetch());
        assertEquals(orderedNames, query().from(cat,cats).orderBy(cat.name.asc()).distinct().select(cat.name).fetch());
    }

    @Test
    public void with_count() {
        CollQuery<?> q = new CollQuery<Void>();
        q.from(cat, cats);
        long size = q.distinct().fetchCount();
        assertTrue(size > 0);
        q.offset(0).limit(10);
        q.orderBy(cat.name.asc());
        assertEquals(Arrays.asList("Alex","Bob","Francis","Kitty"), q.distinct().select(cat.name).fetch());
    }

    @Test
    public void with_null() {
        List<Cat> cats = Arrays.asList(new Cat(), new Cat("Bob"));
        assertEquals(cats, query().from(cat, cats).orderBy(cat.name.asc()).select(cat).fetch());
        assertEquals(Arrays.asList(cats.get(1), cats.get(0)), query().from(cat, cats).orderBy(cat.name.desc()).select(cat).fetch());

    }
}
