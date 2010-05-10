package com.mysema.query;

import static org.junit.Assert.*;


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
}
