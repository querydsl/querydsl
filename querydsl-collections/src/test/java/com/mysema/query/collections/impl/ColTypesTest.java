/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.alias.Alias;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;

/**
 * ColTypesTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class ColTypesTest {

    @Test
    public void testExtString() {
        Expr<Character> echar;
        EBoolean eboolean;
        EComparable<Integer> eint;
        EComparable<Long> elong;
        EString estring;

        EString str = Alias.$("it");

        // charAt(int)
        assertTrue((echar = str.charAt(0)) != null);
        // contains(String)
        assertTrue((eboolean = str.contains("")) != null);
        // endsWith
        assertTrue((eboolean = str.endsWith("")) != null);
        // equalsIgnoreCase(String)
        assertTrue((eboolean = str.equalsIgnoreCase("")) != null);
        // indexOf(String)
        assertTrue((eint = str.indexOf("")) != null);
        // indexOf(String,int)
        assertTrue((eint = str.indexOf("", 0)) != null);
        // isEmpty
        // assertTrue((eboolean = str.isEmpty()) != null);
        // lastIndexOf(String)
//        assertTrue((eint = str.lastIndexOf("")) != null);
//        // lastIndexOf(String,int)
//        assertTrue((eint = str.lastIndexOf("", 0)) != null);
        // length
        assertTrue((elong = str.length()) != null);
        // matches
        // assertTrue((eboolean = str.matches("")) != null);
        // startsWith
        assertTrue((eboolean = str.startsWith("")) != null);
        // trim
        assertTrue((estring = str.trim()) != null);
        
//        assertTrue(eboolean != null);
//        assertTrue(echar != null);
//        assertTrue(eint != null);
//        assertTrue(elong != null);
//        assertTrue(estring != null);
    }
}
