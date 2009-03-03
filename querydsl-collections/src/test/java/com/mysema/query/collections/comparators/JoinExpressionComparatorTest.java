/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.collections.AbstractQueryTest;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;


/**
 * JoinExpressionComparatorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JoinExpressionComparatorTest extends AbstractQueryTest{
    
    private JoinExpression<?> catJoin, otherCatJoin, mateJoin;
    
    private Map<Expr<?>,Iterable<?>> exprToIt;
    
    @Before
    public void setUp(){
        exprToIt = new HashMap<Expr<?>,Iterable<?>>();
        exprToIt.put(cat, Collections.emptyList());
        exprToIt.put(otherCat, Collections.emptyList());
        exprToIt.put(mate, Collections.emptyList());
        catJoin = new JoinExpression<Object>(JoinType.DEFAULT, cat);
        otherCatJoin = new JoinExpression<Object>(JoinType.DEFAULT, otherCat);
        mateJoin = new JoinExpression<Object>(JoinType.DEFAULT, mate);
    }
    
    @Test
    public void test1(){        
        EBoolean where = cat.name.eq(otherCat.name).and(otherCat.name.eq("Bob"));
        
        JoinExpressionComparator comp = new JoinExpressionComparator(where, exprToIt.keySet());        
        assertTrue( comp.compare(otherCatJoin, catJoin) < 0);
        assertTrue( comp.compare(catJoin, otherCatJoin) > 0);
        assertEquals(0, comp.compare(catJoin, catJoin));
        assertEquals(0, comp.compare(otherCatJoin, otherCatJoin));
           
    }
    
    @Test
    public void test2(){
        EBoolean where = cat.name.eq(otherCat.name)
            .and(cat.eq(mate.kittens(0)))
            .and(otherCat.name.eq("Bob"));
        
        JoinExpressionComparator comp = new JoinExpressionComparator(where, exprToIt.keySet());        
        assertTrue( comp.compare(otherCatJoin, catJoin) < 0);
        assertTrue( comp.compare(catJoin, mateJoin) < 0);
    }
    
    
       
}
