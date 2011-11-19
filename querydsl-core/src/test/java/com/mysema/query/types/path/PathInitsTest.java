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
    public void IsInitialized(){
        PathInits inits = new PathInits(".2").get("");
        assertFalse(inits.isInitialized("1"));
        assertTrue(inits.isInitialized("2"));
    }
    
    @Test
    public void Wildcard(){
        assertTrue(new PathInits("*").isInitialized(""));
    }

    @Test
    public void Wildcard2(){
        PathInits inits = new PathInits(".*").get("");
        assertTrue(inits.isInitialized("1"));
        assertTrue(inits.isInitialized("2"));
    }
    
    @Test
    public void Deep_Wildcard() {
        PathInits inits = new PathInits("*.*").get("");
        assertTrue(inits.isInitialized("1"));
        assertTrue(inits.isInitialized("2"));
    }
    
}
