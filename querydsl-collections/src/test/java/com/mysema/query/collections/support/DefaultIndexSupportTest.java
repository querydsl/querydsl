/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.collections.AbstractQueryTest;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;


/**
 * DefaultIndexSupportTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexSupportTest extends AbstractQueryTest{

    private JavaOps ops = JavaOps.DEFAULT;
    
    private Map<Expr<?>,Iterable<?>> exprToIt = new HashMap<Expr<?>,Iterable<?>>();
    
    private DefaultIndexSupport indexSupport;
    
    @Before
    public void before(){
        exprToIt.put(cat, cats);
        exprToIt.put(otherCat, cats);
        indexSupport = new DefaultIndexSupport();
    }
    
    @Test
    public void testPathToPath1(){
        indexSupport.init(exprToIt, ops, Arrays.asList(cat,otherCat), cat.name.eq(otherCat.name));        
        Map<?,? extends Iterable<?>> map = indexSupport.pathEqExprIndex.get(otherCat);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.pathEqExprIndex.get(cat) == null);
        
    }
    
    @Test
    public void testPathToPath2(){
        indexSupport.init(exprToIt, ops, Arrays.asList(otherCat,cat), cat.name.eq(otherCat.name));
        Map<?,? extends Iterable<?>> map = indexSupport.pathEqExprIndex.get(cat);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.pathEqExprIndex.get(otherCat) == null);      
        System.out.println(indexSupport.pathEqExprIndex);        
    }
    
    @Test
    public void testToPath3(){
        indexSupport.init(exprToIt, ops, Arrays.asList(otherCat,cat), cat.eq(otherCat));
        Map<?,? extends Iterable<?>> map = indexSupport.pathEqExprIndex.get(cat);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());        
        assertTrue(indexSupport.pathEqExprIndex.get(otherCat) == null);     
        assertEquals(Arrays.asList(c4), map.get(c4));
    }
    
    @Test
    public void testPathToConstant(){
        indexSupport.init(exprToIt, ops, Arrays.asList(otherCat,cat),
                cat.bodyWeight.eq(0).and(otherCat.name.eq("Kitty")));
        Iterable<?> iterable = indexSupport.pathEqConstantIndex.get(cat);
        assertTrue(iterable != null && iterable.iterator().hasNext());
        iterable = indexSupport.pathEqConstantIndex.get(otherCat);
        assertTrue(iterable != null && iterable.iterator().hasNext());
    }
    
}
