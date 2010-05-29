/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.assertTrue;

import java.util.regex.Pattern;

import org.junit.Test;


public class PatternsTest {
    
    @Test
    public void matches(){
        assertTrue(Pattern.matches("Bob","Bob"));
        assertTrue(Pattern.matches("^Bob$","Bob"));
        assertTrue(Pattern.matches("^Bo.*","Bob"));
        assertTrue(Pattern.matches(".*ob$","Bob"));
        assertTrue(Pattern.matches(".*o.*","Bob"));
    }
}
