/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.*;
import junit.framework.Assert;

public abstract class AbstractTest {

    public Class<?> cl;

    protected void match(Class<?> expectedType, String name) throws SecurityException, NoSuchFieldException{
        assertTrue(cl.getSimpleName()+"."+name + " failed", expectedType.isAssignableFrom(cl.getField(name).getType()));
    }

    protected void assertMissing(String name){
        try {
            cl.getField(name);
            Assert.fail("Expected missing field : " + cl.getSimpleName() + "." + name);
        } catch (NoSuchFieldException e) {
            // expected
        }
    }

}
