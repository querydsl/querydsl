/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SearchResultsTest {

    private List<Integer> list = Arrays.<Integer>asList(0,1,2,3,4,5,6,7,8,9);

    private SearchResults<Integer> results = new SearchResults<Integer>(list,10l,0l,20);

    @Test
    public void GetResults(){
        assertEquals(list, results.getResults());
    }

    @Test
    public void GetTotal(){
        assertEquals(20l , results.getTotal());
    }

    @Test
    public void IsEmpty(){
        assertFalse(results.isEmpty());
    }

    @Test
    public void GetLimit(){
        assertEquals(10l, results.getLimit());
    }

    @Test
    public void GetOffset(){
        assertEquals(0l, results.getOffset());
    }

    @Test
    public void EmptyResults(){
        SearchResults<Object> empty = SearchResults.emptyResults();
        assertTrue(empty.isEmpty());
        assertEquals(Long.MAX_VALUE, empty.getLimit());
        assertEquals(0l, empty.getOffset());
        assertEquals(0l, empty.getTotal());
        assertEquals(Collections.emptyList(), empty.getResults());
    }

}
