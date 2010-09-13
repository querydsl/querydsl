/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class CoalesceTest {

    private final StringPath firstname = new StringPath("firstname");

    private final StringPath lastname = new StringPath("lastname");

    @Test
    public void withList(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname).add("xxx");
        assertEquals("coalesce(firstname, lastname, xxx)", c.toString());
    }

    @Test
    public void withSingleArg(){
        Coalesce<String> c = new Coalesce<String>().add("xxx");
        assertEquals("coalesce(xxx)", c.toString());
    }

    @Test
    public void asComparable(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asc();
    }

    @Test
    public void asString(){
        Coalesce<String> c = new Coalesce<String>(firstname, lastname);
        c.asString().lower();
    }

    @Test
    public void withoutWarnings() {
        Coalesce<String> c = new Coalesce<String>(String.class).add(firstname).add(lastname);
        assertEquals("coalesce(firstname, lastname)", c.toString());
    }
}
