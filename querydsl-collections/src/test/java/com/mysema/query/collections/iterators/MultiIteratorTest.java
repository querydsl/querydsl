/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.iterators;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.MiniApi;
import com.mysema.query.grammar.types.Expr.ENumber;

/**
 * MultiIteratorTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MultiIteratorTest extends AbstractIteratorTest{

    private MultiIterator it = new MultiIterator();
    
    private ENumber<Integer> int1 = MiniApi.$(1);
    private ENumber<Integer> int2 = MiniApi.$(2); 
    private ENumber<Integer> int3 = MiniApi.$(3);
    private ENumber<Integer> int4 = MiniApi.$(4); 
    
    @Test
    public void testEmptyList(){
        it.add(int1, Arrays.asList(1,2)).add(int2, Collections.emptyList()).init();
        while (it.hasNext()){
            it.next();
            fail("should return false on hasNext()");
        }
    }
    
    
    @Test
    public void testOneLevel(){
        it.add(int1,Arrays.asList(1,2)).init();
        assertIteratorEquals(Arrays.asList(row(1),row(2)).iterator(), it);
    }
    
    @Test
    public void testTwoLevels() {
        it.add(int1,Arrays.asList(1,2))
            .add(int2, Arrays.asList(10, 20, 30)).init();
        Iterator<Object[]> base = Arrays.asList(
                row(1,10), row(1,20), row(1,30),
                row(2,10), row(2,20), row(2,30)).iterator();
        assertIteratorEquals(base, it);
    }
    
    @Test
    public void testThreeLevels() {
        it.add(int1, Arrays.asList(1, 2))
            .add(int2, Arrays.asList(10, 20, 30))
            .add(int3, Arrays.asList(100, 200, 300, 400)).init();
        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1,2)){
            for (Object b : row(10, 20, 30)){
                for (Object c : row(100, 200, 300, 400)){
                    list.add(row(a,b,c));
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }
    
    @Test
    public void testFourLevels() {
        it.add(int1, Arrays.asList(1, 2))
            .add(int2, Arrays.asList(10, 20, 30))
            .add(int3, Arrays.asList(100, 200, 300, 400))
            .add(int4, Arrays.asList(1000, 2000, 3000, 4000, 5000)).init();
        List<Object[]> list = new ArrayList<Object[]>();
        for (Object a : row(1,2)){
            for (Object b : row(10, 20, 30)){
                for (Object c : row(100, 200, 300, 400)){
                    for (Object d : row(1000, 2000, 3000, 4000, 5000)){
                        list.add(row(a, b, c, d));    
                    }                    
                }
            }
        }
        assertIteratorEquals(list.iterator(), it);
    }
    
}
