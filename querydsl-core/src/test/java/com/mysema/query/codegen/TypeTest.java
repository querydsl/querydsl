/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeTest {
    
    @Test
    public void arrayType(){
        assertEquals("Object[]",Types.OBJECTS.getLocalRawName(Types.OBJECT));
    }

}
