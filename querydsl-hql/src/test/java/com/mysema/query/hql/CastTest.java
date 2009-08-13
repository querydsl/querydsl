/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.expr.ENumber;

public class CastTest extends AbstractQueryTest {

    @Test
    public void testNumericCast() {
        ENumber<Integer> expr = $(0);
        assertEquals(Byte.class, expr.byteValue().getType());
        assertEquals(Double.class, expr.doubleValue().getType());
        assertEquals(Float.class, expr.floatValue().getType());
        assertEquals(Integer.class, expr.intValue().getType());
        assertEquals(Long.class, expr.longValue().getType());
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void testStringCast() {
        ENumber<Integer> expr = $(0);
        assertEquals(String.class, expr.stringValue().getType());
    }
}
