/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.serialization;

import static org.junit.Assert.*;

import org.junit.Test;


public class JavaPatternsTest {
    
    @Test
    public void test(){
        JavaPatterns patterns = new JavaPatterns();
        for (String pattern : patterns.patterns.values()){
            assertNotNull(String.format(pattern, "","","","",""));
        }
    }

}
