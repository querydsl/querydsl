/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.path.StringPath;

public class ArrayConstructorExpressionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceObjectArray() {
    ArrayConstructorExpression<String> constructor = new ArrayConstructorExpression<String>(String[].class,  new StringPath("test"), new StringPath("test2"));

    String[] strings = constructor.newInstance((Object[])new String[]{"1", "2"});
    assertEquals("1", strings[0]);
    assertEquals("2", strings[1]);

    strings = constructor.newInstance(new Object[]{"1", "2"});
    assertEquals("1", strings[0]);
    assertEquals("2", strings[1]);

    }

}
