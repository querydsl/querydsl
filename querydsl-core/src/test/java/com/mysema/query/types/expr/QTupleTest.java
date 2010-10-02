/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.Tuple;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class QTupleTest {

    private StringPath first = new StringPath("x");

    private NumberPath<Integer> second = new NumberPath<Integer>(Integer.class,"y");

    private BooleanPath third = new BooleanPath("z");

    private QTuple tupleExpression = new QTuple(first, second, third);

    @Test
    public void NewInstanceObjectArray() {
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
