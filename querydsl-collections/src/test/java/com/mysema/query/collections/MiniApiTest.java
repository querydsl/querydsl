/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;


import static com.mysema.query.collections.MiniApi.$;
import static com.mysema.query.collections.MiniApi.from;
import static com.mysema.query.collections.MiniApi.reject;
import static com.mysema.query.collections.MiniApi.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * MiniApiTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MiniApiTest {

    List<Integer> myInts = new ArrayList<Integer>();   
    
    List<Integer> ints = new ArrayList<Integer>();
    
    @Before public void setUp(){
        myInts.add(1);
        myInts.add(2);
        myInts.add(3);
        myInts.add(4);        
    }
    
    @Test
    public void testVarious(){
        for(String s : from($("str"), "a","ab","cd","de")
                      .where($("str").startsWith("a"))
                      .iterate($("str"))){
            assertTrue(s.equals("a") || s.equals("ab"));
            System.out.println(s);
        }
        
        for (Object o : from(1,2,"abc",5,3).where($().ne("abc"))
                       .iterate($())){
            int i = (Integer)o;
            assertTrue(i > 0 && i < 6);
            System.out.println(o);
        }
    }
    
    @Test public void testSimpleSelect() {
        int intVal = 0;
    //  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = select(myInts, $(intVal).gt(2));  
        
        for (Integer i : threeAndFour) ints.add(i);
        assertEquals(Arrays.asList(3,4), ints);
    }

    @Test public void testSimpleReject() {
        int intVal = 0;
    //  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = reject(myInts, $(intVal).gt(2));
        
        for (Integer i : oneAndTwo) ints.add(i);
        assertEquals(Arrays.asList(1,2), ints);
    }
    
}
