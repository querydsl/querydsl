/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EStringEscapeTest {

    @Test
    public void testEscapeForLike() {
        assertEquals("a\\%b\\_c", StringEscape.escapeForLike(StringConstant.create("a%b_c")).toString());
    }

}
