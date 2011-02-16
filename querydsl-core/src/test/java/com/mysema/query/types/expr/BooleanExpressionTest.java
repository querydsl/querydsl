package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.BooleanPath;

public class BooleanExpressionTest {

    private final BooleanExpression a = new BooleanPath("a");
    private final BooleanExpression b = new BooleanPath("b");
    private final BooleanExpression c = new BooleanPath("c");

    @Test
    public void AnyOf(){
        assertEquals(a.or(b).or(c), BooleanExpression.anyOf(a, b, c));
    }

    @Test
    public void AllOf(){
        assertEquals(a.and(b).and(c), BooleanExpression.allOf(a, b, c));
    }

    @Test
    public void AndAnyOf(){
        assertEquals(a.and(b.or(c)), a.andAnyOf(b, c));
    }

    @Test
    public void OrAllOf(){
        assertEquals(a.or(b.and(c)), a.orAllOf(b, c));
    }

    @Test
    public void Not(){
        assertEquals(a, a.not().not());
    }

    @Test
    public void IsTrue(){
        assertEquals(a.eq(true), a.isTrue());
    }

    @Test
    public void IsFalse(){
        assertEquals(a.eq(false), a.isFalse());
    }
}
