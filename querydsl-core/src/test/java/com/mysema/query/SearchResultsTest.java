package com.mysema.query;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;


public class SearchResultsTest {
    
    private List<Integer> list = Arrays.<Integer>asList(0,1,2,3,4,5,6,7,8,9);
    
    private SearchResults<Integer> results = new SearchResults<Integer>(list,10l,0l,20);
    
    @Test
    public void getResults(){
	assertEquals(list, results.getResults());
    }
    
    @Test
    public void getTotal(){
	assertEquals(20l , results.getTotal());
    }

    @Test
    public void isEmpty(){
	assertFalse(results.isEmpty());
    }
    
    @Test
    public void getLimit(){
	assertEquals(10l, results.getLimit());
    }
    
    @Test
    public void getOffset(){
	assertEquals(0l, results.getOffset());
    }
}
