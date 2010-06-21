/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.Tuple;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PString;

public class QTupleTest {

    private PString first = new PString("x");
    
    private PNumber<Integer> second = new PNumber<Integer>(Integer.class,"y");
    
    private PBoolean third = new PBoolean("z");
    
    private QTuple tupleExpression = new QTuple(first, second, third);
    
    @Test
    public void testNewInstanceObjectArray() {
    Tuple tuple = tupleExpression.newInstance("1", 42, true);
    assertEquals(3, tuple.toArray().length);
    assertEquals("1", tuple.get(0, String.class));
    assertEquals(Integer.valueOf(42), tuple.get(1, Integer.class));
    assertEquals(Boolean.TRUE, tuple.get(2, Boolean.class));
    assertEquals("1", tuple.get(first));
    assertEquals(Integer.valueOf(42), tuple.get(second));
    assertEquals(Boolean.TRUE, tuple.get(third));
    
    
    }

}
