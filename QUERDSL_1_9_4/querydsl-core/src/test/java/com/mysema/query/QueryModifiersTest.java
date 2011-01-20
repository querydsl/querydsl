/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QueryModifiersTest {

    @Test
    public void testLimit() {
        QueryModifiers modifiers = QueryModifiers.limit(12l);
        assertEquals(Long.valueOf(12), modifiers.getLimit());
        assertNull(modifiers.getOffset());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void testOffset() {
        QueryModifiers modifiers = QueryModifiers.offset(12l);
        assertEquals(Long.valueOf(12), modifiers.getOffset());
        assertNull(modifiers.getLimit());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void testBoth(){
        QueryModifiers modifiers = new QueryModifiers(1l,2l);
        assertEquals(Long.valueOf(1), modifiers.getLimit());
        assertEquals(Long.valueOf(2), modifiers.getOffset());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void testEmpty(){
        QueryModifiers modifiers = new QueryModifiers(null, null);
        assertNull(modifiers.getLimit());
        assertNull(modifiers.getOffset());
        assertFalse(modifiers.isRestricting());
    }

    @Test
    public void testHashCode(){
    QueryModifiers modifiers1 = new QueryModifiers(null, null);
    QueryModifiers modifiers2 = new QueryModifiers(1l, null);
    QueryModifiers modifiers3 = new QueryModifiers(null, 1l);

    assertEquals(modifiers1.hashCode(), new QueryModifiers().hashCode());
    assertEquals(modifiers2.hashCode(), QueryModifiers.limit(1l).hashCode());
    assertEquals(modifiers3.hashCode(), QueryModifiers.offset(1l).hashCode());
    }

    @Test(expected=IllegalArgumentException.class)
    public void illegalLimit(){
        QueryModifiers.limit(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void illegalOffset(){
        QueryModifiers.offset(-1);
    }
}
