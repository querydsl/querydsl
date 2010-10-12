/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.Test;

import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.expr.BooleanExpression;
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
    public void Basic(){
//        new BooleanBuilder().and(first).or(second);
        assertEquals(first.or(second).toString(),
            new BooleanBuilder().and(first).or(second).toString());
    }

    @Test
    public void Advanced(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        System.out.println(builder);
    }

    @Test
    public void If_Then_Else(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(null);
        builder.or(null);
        builder.and(second);
        assertEquals(second, builder.getValue());
    }

    @Test
    public void Null_Support(){
        assertEquals(first, first.and(null));
        assertEquals(first, first.or(null));
    }

    @Test
    public void And_Not(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).andNot(second);
        assertEquals(first.and(second.not()), builder.getValue());
    }

    @Test
    public void Or_Not(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).orNot(second);
        assertEquals(first.or(second.not()), builder.getValue());
    }

    @Test
    public void Not(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).not();
        assertEquals(first.not(), builder.getValue());
    }

    @Test
    public void Equals(){
        assertEquals(new BooleanBuilder(first), new BooleanBuilder(first));
        assertFalse(first.equals(new BooleanBuilder(first)));
        assertFalse(new BooleanBuilder(first).equals(first));
    }

    @Test
    public void HashCode(){
        assertEquals(new BooleanBuilder(first).hashCode(), new BooleanBuilder(first).hashCode());
    }

    @Test
    public void ToString(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        assertEquals("true", builder.toString());
        builder.or(new BooleanPath("condition"));
        assertEquals("true || condition", builder.toString());
    }
    
    @Test
    public void GetArg(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        assertEquals(first, builder.getArg(0));
        assertEquals(Arrays.asList(first), builder.getArgs());
    }
    
    @Test
    public void Accept(){
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        builder.or(new BooleanPath("condition"));
        assertEquals("true || condition", builder.accept(ToStringVisitor.DEFAULT, Templates.DEFAULT));
    }
}
