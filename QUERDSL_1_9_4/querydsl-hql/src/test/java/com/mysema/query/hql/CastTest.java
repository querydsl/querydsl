/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.path.PNumber;

public class CastTest extends AbstractQueryTest {

    private static ENumber<Integer> expr = new PNumber<Integer>(Integer.class,"int");

    @Test
    public void testNumericCast() {
        assertEquals(Byte.class, expr.byteValue().getType());
        assertEquals(Double.class, expr.doubleValue().getType());
        assertEquals(Float.class, expr.floatValue().getType());
        assertEquals(Integer.class, expr.intValue().getType());
        assertEquals(Long.class, expr.longValue().getType());
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void testStringCast() {
        assertEquals(String.class, expr.stringValue().getType());
    }
}
