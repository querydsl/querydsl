package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.BooleanPath;

public class BooleanExpressionTest {

    private BooleanExpression a = new BooleanPath("a");
    private BooleanExpression b = new BooleanPath("b");
    private BooleanExpression c = new BooleanPath("c");
    
    @Test
    public void AnyOf(){
        assertEquals(a.or(b).or(c), BooleanExpression.anyOf(a, b, c));
    }
    
    @Test
    public void AllOf(){
        assertEquals(a.and(b).and(c), BooleanExpression.allOf(a, b, c));
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
