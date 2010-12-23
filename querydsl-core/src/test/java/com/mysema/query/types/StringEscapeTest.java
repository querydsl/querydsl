/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringEscapeTest {

    @Test
    public void EscapeForLike() {
        Converters converters = new Converters('\\');
        assertEquals("a\\%b\\_c", converters.escapeForLike(new ConstantImpl<String>("a%b_c")).toString());
    }

}
