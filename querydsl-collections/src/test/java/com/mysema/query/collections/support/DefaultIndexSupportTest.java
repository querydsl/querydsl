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
//        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops);
    }
    
    @Test
    public void testPathToPath1(){
        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops, Arrays.asList(cat, otherCat));
        indexSupport.getChildFor(cat.name.eq(otherCat.name));        
        Map<?,? extends Iterable<?>> map = indexSupport.getPathToKeyToValues().get(otherCat.name);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.getPathToKeyToValues().get(cat) == null);
        
    }
    
    @Test
    public void testPathToPath2(){
        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops, Arrays.asList(otherCat,cat));
        indexSupport.getChildFor(cat.name.eq(otherCat.name));
        Map<?,? extends Iterable<?>> map = indexSupport.getPathToKeyToValues().get(cat.name);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.getPathToKeyToValues().get(otherCat) == null);      
        System.out.println(indexSupport.getPathToKeyToValues());        
    }
    
    @Test
    public void testToPath3(){
        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops, Arrays.asList(otherCat,cat));
        indexSupport.getChildFor(cat.eq(otherCat));
        Map<?,? extends Iterable<?>> map = indexSupport.getPathToKeyToValues().get(cat);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());        
        assertTrue(indexSupport.getPathToKeyToValues().get(otherCat) == null);     
        assertEquals(Arrays.asList(c4), map.get(c4));
    }
    
    @Test
    public void testPathToConstant(){
        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops, Arrays.asList(cat,otherCat));
        indexSupport.getChildFor(cat.bodyWeight.eq(0).and(otherCat.name.eq("Kitty")));
        Iterable<?> iterable = indexSupport.getPathToKeyToValues().get(otherCat.name).get("Kitty");
        assertTrue(iterable != null && iterable.iterator().hasNext());
        assertTrue(indexSupport.getPathToKeyToValues().get(cat.name) == null);
    }
    
    @Test
    public void testNullArgument(){
        indexSupport = new DefaultIndexSupport(new SimpleIteratorSource(exprToIt),ops, Arrays.asList(cat,otherCat));
        assertEquals(indexSupport, indexSupport.getChildFor(null));
    }
}
