/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * OrderTest provides
 *
 * @author tiwe
 * @version $Id$
 */
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
    public void test_with_null(){
        List<Cat> cats = Arrays.asList(new Cat(), new Cat("Bob"));        
        assertEquals(cats, query().from(cat, cats).orderBy(cat.name.asc()).list(cat));
        assertEquals(Arrays.asList(cats.get(1), cats.get(0)), query().from(cat, cats).orderBy(cat.name.desc()).list(cat));

    }
}
