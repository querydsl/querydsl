package com.mysema.query.types;

import static org.junit.Assert.*;

import org.junit.Test;


public class NullExpressionTest {

    @Test
    public void test(){
        assertNotNull(new NullExpression<Object>(Object.class));
    }
    
}
