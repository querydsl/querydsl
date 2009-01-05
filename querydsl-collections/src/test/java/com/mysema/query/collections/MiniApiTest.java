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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.grammar.types.Path;

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
        for(String[] strs : from($("a"), "aa","bb","cc").from($("b"), "a","b")
                .where($("a").startsWith($("b")))
                .iterate($("a"),$("b"))){
            System.out.println(Arrays.asList(strs));
        }
    }

    @Test
    public void testVarious1(){
        for(String s : from($("str"), "a","ab","cd","de")
                .where($("str").startsWith("a"))
                .iterate($("str"))){
            assertTrue(s.equals("a") || s.equals("ab"));
            System.out.println(s);
        }
    }
    
    @Test
    public void testVarious2(){
        for (Object o : from($(),1,2,"abc",5,3).where($().ne("abc")).iterate($())){
            int i = (Integer)o;
            assertTrue(i > 0 && i < 6);
            System.out.println(o);
        }                
    }
    
    @Test
    public void testVarious3(){
        for (Integer i : from($(0),1,2,3,4).where($(0).lt(4)).iterate($(0))){
            System.out.println(i);
        }
    }
    
    @Test
    public void testMapUsage(){        
        Map<String,String> map = new HashMap<String,String>();      
        map.put("1","one");
        map.put("2","two");
        map.put("3","three");
        map.put("4","four");
        
        // 1st 
        Path.PSimple<Map.Entry<String,String>> e = $(map.entrySet().iterator().next());
        for (Map.Entry<String,String> entry : from(e, map.entrySet()).iterate(e)){
            System.out.println(entry.getKey() + " > " + entry.getValue());
        }
        
        // 2nd
//        for (String[] kv : from($("k"), $("v"), map).iterate($("k"),$("v"))){
//            System.out.println(kv[0] + " > " + kv[1]);
//        }
    }
    
    @Test public void testSimpleSelect() {
    //  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = select(myInts, $(0).gt(2));  
        
        for (Integer i : threeAndFour) ints.add(i);
        assertEquals(Arrays.asList(3,4), ints);
    }

    @Test public void testSimpleReject() {
    //  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = reject(myInts, $(0).gt(2));
        
        for (Integer i : oneAndTwo) ints.add(i);
        assertEquals(Arrays.asList(1,2), ints);
    }
    
}
