/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathBuilderFactoryTest {

    @Test
    public void testCreate() {
        PathBuilderFactory factory = new PathBuilderFactory();
        PathBuilder<Object> pathBuilder = factory.create(Object.class);
        assertEquals("object", pathBuilder.toString());
        assertEquals(Object.class, pathBuilder.getType());
        
        pathBuilder.get("prop", Object.class);
        pathBuilder.get("prop", String.class);
        pathBuilder.get("prop", Object.class);
    }

}
