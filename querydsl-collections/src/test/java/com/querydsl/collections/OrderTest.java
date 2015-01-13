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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OrderTest extends AbstractQueryTest {

    @Test
    public void test() {
        query().from(cat, cats).orderBy(cat.name.asc()).list(cat.name);
        assertArrayEquals(new Object[] { "Alex", "Bob", "Francis", "Kitty" }, last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.desc()).list(cat.name);
        assertArrayEquals(new Object[] { "Kitty", "Francis", "Bob", "Alex" }, last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.substring(1).asc()).list(cat.name);
        assertArrayEquals(new Object[] { "Kitty", "Alex", "Bob", "Francis" }, last.res.toArray());

        query().from(cat, cats).from(otherCat, cats).orderBy(cat.name.asc(), otherCat.name.desc()).list(cat.name, otherCat.name);

        // TODO : more tests
    }
    
    @Test
    public void test2() {
        List<String> orderedNames = Arrays.asList("Alex","Bob","Francis","Kitty"); 
        assertEquals(orderedNames, query().from(cat,cats).orderBy(cat.name.asc()).list(cat.name));
        assertEquals(orderedNames, query().from(cat,cats).orderBy(cat.name.asc()).distinct().list(cat.name));
    }
    
    @Test
    public void With_count() {
        CollQuery q = new CollQuery();
        q.from(cat, cats);
        long size = q.distinct().count();
        assertTrue(size > 0);
        q.offset(0).limit(10);
        q.orderBy(cat.name.asc());
        assertEquals(Arrays.asList("Alex","Bob","Francis","Kitty"), q.distinct().list(cat.name));
    }

    @Test
    public void With_null() {
        List<Cat> cats = Arrays.asList(new Cat(), new Cat("Bob"));        
        assertEquals(cats, query().from(cat, cats).orderBy(cat.name.asc()).list(cat));
        assertEquals(Arrays.asList(cats.get(1), cats.get(0)), query().from(cat, cats).orderBy(cat.name.desc()).list(cat));

    }
}
