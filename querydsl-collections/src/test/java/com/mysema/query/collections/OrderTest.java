/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertArrayEquals;

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
        assertArrayEquals(new Object[] { "Alex", "Bob", "Francis", "Kitty" },
                last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.desc()).list(cat.name);
        assertArrayEquals(new Object[] { "Kitty", "Francis", "Bob", "Alex" },
                last.res.toArray());

        query().from(cat, cats).orderBy(cat.name.substring(1).asc()).list(
                cat.name);
        assertArrayEquals(new Object[] { "Kitty", "Alex", "Bob", "Francis" },
                last.res.toArray());

        query().from(cat, cats).from(otherCat, cats).orderBy(cat.name.asc(),
                otherCat.name.desc()).list(cat.name, otherCat.name);

        // TODO : more tests
    }

}
