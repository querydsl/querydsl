/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.grammar.types.ExtTypes.ExtString;


/**
 * ColTypesTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class ColTypesTest {

    @Test
    @SuppressWarnings("unused")
    public void testExtString(){        
        Expr<Character> echar;
        Expr.EBoolean eboolean;
        Expr.EComparable<Integer> eint;
        Expr.EString estring;
        
        ExtString str = MiniApi.$("it");
        
//      charAt(int)
        assertTrue((echar = str.charAt(0)) != null);        
//      contains(String)
        assertTrue((eboolean = str.contains("")) != null);
//      endsWith
        assertTrue((eboolean = str.endsWith("")) != null);
//      equalsIgnoreCase(String)
        assertTrue((eboolean = str.equalsIgnoreCase("")) != null);
//      indexOf(String)
        assertTrue((eint = str.indexOf("")) != null);
//      indexOf(String,int)
        assertTrue((eint = str.indexOf("",0)) != null);
//      isEmpty
        assertTrue((eboolean = str.isEmpty()) != null);
//      lastIndexOf(String)
        assertTrue((eint = str.lastIndexOf("")) != null);
//      lastIndexOf(String,int)
        assertTrue((eint = str.lastIndex("",0)) != null);
//      length
        assertTrue((eint = str.length()) != null);
//      matches
        assertTrue((eboolean = str.matches("")) != null);
//      startsWith
        assertTrue((eboolean = str.startsWith("")) != null);
//      trim
        assertTrue((estring = str.trim()) != null);
    }
}
