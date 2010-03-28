/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleEvaluatorTest {

    @Test
    public void testToId() {
        assertEquals("Q_119160_1063877011_1195259493", SimpleEvaluator.toId("xxx", Object.class, String.class));
    }

}
