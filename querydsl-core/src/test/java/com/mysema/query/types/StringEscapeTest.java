/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ConstantImpl;
import com.mysema.util.StringEscape;

public class StringEscapeTest {

    @Test
    public void EscapeForLike() {
        assertEquals("a\\%b\\_c", StringEscape.escapeForLike(new ConstantImpl<String>("a%b_c")).toString());
    }

}
