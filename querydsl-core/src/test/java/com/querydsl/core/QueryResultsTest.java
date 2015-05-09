/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class QueryResultsTest {

    private List<Integer> list = Arrays.asList(0,1,2,3,4,5,6,7,8,9);

    private QueryResults<Integer> results = new QueryResults<Integer>(list,10l,0l,20);

    @Test
    public void GetResults() {
        assertEquals(list, results.getResults());
    }

    @Test
    public void GetTotal() {
        assertEquals(20l , results.getTotal());
    }

    @Test
    public void IsEmpty() {
        assertFalse(results.isEmpty());
    }

    @Test
    public void GetLimit() {
        assertEquals(10l, results.getLimit());
    }

    @Test
    public void GetOffset() {
        assertEquals(0l, results.getOffset());
    }

    @Test
    public void EmptyResults() {
        QueryResults<Object> empty = QueryResults.emptyResults();
        assertTrue(empty.isEmpty());
        assertEquals(Long.MAX_VALUE, empty.getLimit());
        assertEquals(0l, empty.getOffset());
        assertEquals(0l, empty.getTotal());
        assertEquals(Collections.emptyList(), empty.getResults());
    }

}
