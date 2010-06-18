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

import com.mysema.query.types.expr.EBooleanConst;
import com.mysema.query.types.path.PString;

public class JoinExpressionTest {
    
    private JoinExpression je = new JoinExpression(JoinType.DEFAULT, new PString("str"));
    
    @Test
    public void testToString(){
	assertEquals("DEFAULT str", je.toString());
    }

    @Test
    public void testAddCondition() {
	je.addCondition(EBooleanConst.TRUE);
	assertEquals(EBooleanConst.TRUE, je.getCondition());
	
	je.addCondition(EBooleanConst.FALSE);
	assertEquals(EBooleanConst.TRUE.and(EBooleanConst.FALSE), je.getCondition());
    }

    @Test
    public void testSetFlag() {
	assertFalse(je.hasFlag("X"));
	
	je.setFlag("X");
	assertTrue(je.hasFlag("X"));
	assertFalse(je.hasFlag("Y"));
	
	je.removeFlag("X");
	assertFalse(je.hasFlag("X"));
    }

}
