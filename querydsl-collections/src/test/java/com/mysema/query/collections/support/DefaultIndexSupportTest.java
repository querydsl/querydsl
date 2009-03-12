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

    private JavaOps ops = new JavaOps();
    
    private Map<Expr<?>,Iterable<?>> exprToIt = new HashMap<Expr<?>,Iterable<?>>();
    
    private DefaultIndexSupport indexSupport;
    
    @Before
    public void before(){
        exprToIt.put(cat, cats);
        exprToIt.put(otherCat, cats);
        indexSupport = new DefaultIndexSupport(exprToIt);
    }
    
    @Test
    public void test1(){
        indexSupport.init(ops, Arrays.asList(cat,otherCat), cat.name.eq(otherCat.name));        
        Map<?,? extends Iterable<?>> map = indexSupport.pathEqPathIndex.get(otherCat.name);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.pathEqPathIndex.get(cat.name) == null);
        
    }
    
    @Test
    public void test2(){
        indexSupport.init(ops, Arrays.asList(otherCat,cat), cat.name.eq(otherCat.name));
        Map<?,? extends Iterable<?>> map = indexSupport.pathEqPathIndex.get(cat.name);
        assertTrue("map was null or empty", map != null && !map.isEmpty());
        assertEquals(4, map.size());
        assertTrue(indexSupport.pathEqPathIndex.get(otherCat.name) == null);
        
    }
    
}
