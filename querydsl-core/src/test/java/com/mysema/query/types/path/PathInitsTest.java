/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PathInitsTest {

    @Test
    public void testDefault(){
        assertFalse(PathInits.DEFAULT.isInitialized("test"));
    }

    @Test
    public void test2(){
        PathInits testInits = new PathInits("test.test2").get("test");
        assertFalse(testInits.isInitialized("test1"));
        assertTrue(testInits.isInitialized("test2"));
    }

    @Test
    public void testWildcard(){
        assertTrue(new PathInits("*").isInitialized("test"));
    }

    @Test
    public void testWildcard2(){
        PathInits testInits = new PathInits("test.*").get("test");
        assertTrue(testInits.isInitialized("test1"));
        assertTrue(testInits.isInitialized("test2"));
    }
}
