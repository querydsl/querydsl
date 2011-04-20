/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.NumberPath;

public class CastTest extends AbstractQueryTest {

    private static NumberExpression<Integer> expr = new NumberPath<Integer>(Integer.class,"int");

    @Test
    public void Byte() {
        assertEquals(Byte.class, expr.byteValue().getType());
    }
    
    @Test
    public void Double() {
        assertEquals(Double.class, expr.doubleValue().getType());
    }
    
    @Test
    public void Float() {
        assertEquals(Float.class, expr.floatValue().getType());
    }
    
    @Test
    public void Integer() {
        assertEquals(Integer.class, expr.intValue().getType());
    }
    
    @Test
    public void Long() {
        assertEquals(Long.class, expr.longValue().getType());
    }
    
    @Test
    public void Short() {
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void StringCast() {
        assertEquals(String.class, expr.stringValue().getType());
    }
}
