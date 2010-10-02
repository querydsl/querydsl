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
    public void Default(){
        assertFalse(PathInits.DEFAULT.isInitialized(""));
    }

    @Test
    public void test(){
        PathInits Inits = new PathInits(".2").get("");
        assertFalse(Inits.isInitialized("1"));
        assertTrue(Inits.isInitialized("2"));
    }

    @Test
    public void Wildcard(){
        assertTrue(new PathInits("*").isInitialized(""));
    }

    @Test
    public void Wildcard2(){
        PathInits Inits = new PathInits(".*").get("");
        assertTrue(Inits.isInitialized("1"));
        assertTrue(Inits.isInitialized("2"));
    }
}
