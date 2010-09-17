/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;

public class JoinExpressionTest {

    private JoinExpression je = new JoinExpression(JoinType.DEFAULT, new StringPath("str"));

    @Test
    public void testToString() {
        assertEquals("DEFAULT str", je.toString());
    }

    @Test
    public void testAddCondition() {
        je.addCondition(BooleanConstant.TRUE);
        assertEquals(BooleanConstant.TRUE, je.getCondition());

        je.addCondition(BooleanConstant.FALSE);
        assertEquals(BooleanConstant.TRUE.and(BooleanConstant.FALSE), je.getCondition());
    }

    @Test
    public void testSetFlag() {
        JoinFlag x = new JoinFlag("x");
        JoinFlag y = new JoinFlag("y");
        assertFalse(je.hasFlag(x));

        je.addFlag(x);
        assertTrue(je.hasFlag(x));
        assertFalse(je.hasFlag(y));
        
        je.addFlag(y);
        assertTrue(je.hasFlag(y));
    }

}
