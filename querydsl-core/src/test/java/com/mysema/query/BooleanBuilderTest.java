/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanConstant;
import com.mysema.query.types.path.BooleanPath;

/**
 * CascadingBooleanTest provides.
 *
 * @author tiwe
 * @version $Id$
 */
public class BooleanBuilderTest {

    private BooleanExpression first = BooleanConstant.TRUE;

    private BooleanExpression second = BooleanConstant.FALSE;

    @Test
    public void test(){
//        new BooleanBuilder().and(first).or(second);
        assertEquals(first.or(second).toString(),
            new BooleanBuilder().and(first).or(second).toString());
    }

    @Test
    public void advanced(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        System.out.println(builder);
    }

    @Test
    public void ifThenElse(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(null);
        builder.or(null);
        builder.and(second);
        assertEquals(second, builder.getValue());
    }

    @Test
    public void nullSupport(){
        assertEquals(first, first.and(null));
        assertEquals(first, first.or(null));
    }

    @Test
    public void andNot(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).andNot(second);
        assertEquals(first.and(second.not()), builder.getValue());
    }

    @Test
    public void orNot(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).orNot(second);
        assertEquals(first.or(second.not()), builder.getValue());
    }

    @Test
    public void not(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).not();
        assertEquals(first.not(), builder.getValue());
    }

    @Test
    public void equals(){
        assertEquals(new BooleanBuilder(first), new BooleanBuilder(first));
        assertFalse(first.equals(new BooleanBuilder(first)));
        assertFalse(new BooleanBuilder(first).equals(first));
    }

    @Test
    public void testHashCode(){
        assertEquals(new BooleanBuilder(first).hashCode(), new BooleanBuilder(first).hashCode());
    }

    @Test
    @Ignore // FIXME
    public void testToString(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        assertEquals("true", builder.toString());
        builder.or(new BooleanPath("condition"));
        assertEquals("true || condition", builder.toString());
    }
}
