/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.impl.JoinExpressionComparator;
import com.mysema.query.types.expr.EBoolean;

/**
 * JoinExpressionComparatorTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class JoinExpressionComparatorTest extends AbstractQueryTest {

    private JoinExpression catJoin, otherCatJoin, mateJoin;

    @Before
    public void setUp() {
        catJoin = new JoinExpression(JoinType.DEFAULT, cat);
        otherCatJoin = new JoinExpression(JoinType.DEFAULT, otherCat);
        mateJoin = new JoinExpression(JoinType.DEFAULT, mate);
    }

    @Test
    public void test1() {
        EBoolean where = cat.name.eq(otherCat.name)
                .and(otherCat.name.eq("Bob"));

        JoinExpressionComparator comp = new JoinExpressionComparator(where);
        assertTrue(comp.compare(otherCatJoin, catJoin) < 0);
        assertTrue(comp.compare(catJoin, otherCatJoin) > 0);
        assertEquals(0, comp.compare(catJoin, catJoin));
        assertEquals(0, comp.compare(otherCatJoin, otherCatJoin));
    }

    @Test
    public void test2() {
        EBoolean where = cat.name.eq(otherCat.name)
                .and(cat.eq(mate.kittens(0))).and(otherCat.name.eq("Bob"));

        JoinExpressionComparator comp = new JoinExpressionComparator(where);
        assertTrue(comp.compare(otherCatJoin, catJoin) < 0);
        assertTrue(comp.compare(catJoin, mateJoin) < 0);
    }

}
