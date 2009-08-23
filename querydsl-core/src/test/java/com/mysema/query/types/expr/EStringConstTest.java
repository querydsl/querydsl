package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;


public class EStringConstTest {

    @Test
    public void test(){
        assertEquals("abc", EString.create("ab").append("c").toString());
        assertEquals("abc", EString.create("bc").prepend("a").toString());
        assertEquals("abc", EString.create("ABC").lower().toString());
        assertEquals("ABC", EString.create("abc").upper().toString());
    }
}
