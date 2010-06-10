/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.path.PString;

public class EArrayConstructorTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNewInstanceObjectArray() {
	EArrayConstructor<String> constructor = new EArrayConstructor<String>(String[].class,  new PString("test"), new PString("test2"));
	
	String[] strings = constructor.newInstance((Object[])new String[]{"1", "2"});
	assertEquals("1", strings[0]);
	assertEquals("2", strings[1]);
	
	strings = constructor.newInstance(new Object[]{"1", "2"});
	assertEquals("1", strings[0]);
	assertEquals("2", strings[1]);
	
    }

}
