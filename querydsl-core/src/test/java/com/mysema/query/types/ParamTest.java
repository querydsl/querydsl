/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.mysema.query.types.expr.Param;

public class ParamTest {

    Param<String> param11 = new Param<String>(String.class, "param1");
    Param<String> param12 = new Param<String>(String.class, "param1");
    Param<String> param2 = new Param<String>(String.class, "param2");
    Param<Object> param3 = new Param<Object>(Object.class, "param1");
    Param<String> param4 = new Param<String>(String.class);

    @Test
    public void Identity(){
        assertEquals(param11, param12);
        assertFalse(param11.equals(param2));
        assertFalse(param11.equals(param3));
        assertFalse(param11.equals(param4));
    }

    @Test
    public void GetNotSetMessage(){
        assertEquals("The parameter param1 needs to be set", param11.getNotSetMessage());
        assertEquals("A parameter of type java.lang.String was not set", param4.getNotSetMessage());
    }
}
