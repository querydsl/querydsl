package com.mysema.query.collections;

import static com.mysema.query.collections.MiniApi.intVal;
import static com.mysema.query.collections.MiniApi.reject;
import static com.mysema.query.collections.MiniApi.select;
import static org.junit.Assert.assertEquals;

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
    

//  List<Integer> myInts = new ArrayList<Integer>();
//  myInts.add(1);
//  myInts.add(2);
//  myInts.add(3);
//  myInts.add(4);
//
//  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
//  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
  
    
    @Test public void testSelect() {
    //  Iterable<Integer> threeAndFour = select(myInts, greaterThan(2));
        Iterable<Integer> threeAndFour = select(myInts, intVal.gt(2));  
        
        for (Integer i : threeAndFour) ints.add(i);
        assertEquals(Arrays.asList(3,4), ints);
    }

    @Test public void testReject() {
    //  Iterable<Integer> oneAndTwo = reject(myInts, greaterThan(2));
        Iterable<Integer> oneAndTwo = reject(myInts, intVal.gt(2));
        
        for (Integer i : oneAndTwo) ints.add(i);
        assertEquals(Arrays.asList(1,2), ints);
    }

}
