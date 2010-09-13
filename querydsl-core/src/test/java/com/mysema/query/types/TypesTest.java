/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class TypesTest {

    @Test
    public void testExpr() {
        for (Class<?> cl : Expression.class.getClasses()) {
            assertTrue(cl.getName(), Expression.class.isAssignableFrom(cl));
        }
    }

    @Test
    public void testPath() {
        for (Class<?> cl : Path.class.getClasses()) {
            assertTrue(cl.getName(), Path.class.isAssignableFrom(cl));
            if (!cl.isInterface()) {
                assertTrue(cl.getName(), Expression.class.isAssignableFrom(cl));
            }
        }
    }

}
