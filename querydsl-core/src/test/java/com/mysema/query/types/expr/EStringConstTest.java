/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;


public class EStringConstTest {

    @Test
    public void test(){
        assertEquals("abc", expr("ab").append("c").toString());
        assertEquals("abc", expr("bc").prepend("a").toString());
        assertEquals("abc", expr("ABC").lower().toString());
        assertEquals("ABC", expr("abc").upper().toString());
        assertEquals("ab",  expr("abc").substring(0,2).toString());
    }
    
    @Test
    public void test2(){
        assertEquals("abc", expr("ab").append(expr("c")).toString());
        assertEquals("abc", expr("bc").prepend(expr("a")).toString());
        assertEquals("abc", expr("ABC").lower().toString());
        assertEquals("ABC", expr("abc").upper().toString());
        assertEquals("ab",  expr("abc").substring(0,2).toString());
    }
    
    private EString expr(String str){
        return EString.create(str);
    }
}
