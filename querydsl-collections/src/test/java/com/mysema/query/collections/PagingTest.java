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
package com.mysema.query.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.types.path.NumberPath;

public class PagingTest extends AbstractQueryTest {

    private List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    private NumberPath<Integer> var = new NumberPath<Integer>(Integer.class, "var");

    @Test
    public void test() {
        assertResultSize(9, 9, QueryModifiers.EMPTY);
        assertResultSize(9, 2, new QueryModifiers(2l, null));
        assertResultSize(9, 2, new QueryModifiers(2l, 0l));
        assertResultSize(9, 2, new QueryModifiers(2l, 3l));
        assertResultSize(9, 9, new QueryModifiers(20l, null));
        assertResultSize(9, 9, new QueryModifiers(20l, 0l));
        assertResultSize(9, 5, new QueryModifiers(20l, 4l));
        assertResultSize(9, 0, new QueryModifiers(10l, 9l));
    }

    private void assertResultSize(int total, int size, QueryModifiers modifiers) {
        // via list
        assertEquals(size, createQuery(modifiers).list(var).size());

        // via results
        SearchResults<?> results = createQuery(modifiers).listResults(var);
        assertEquals(total, results.getTotal());
        assertEquals(size, results.getResults().size());

        // via count (ignore limit and offset)
        assertEquals(total, createQuery(modifiers).count());

        // via iterator
        assertEquals(size, IteratorAdapter.asList(createQuery(modifiers).iterate(var)).size());
    }

    private CollQuery createQuery(QueryModifiers modifiers) {
        CollQuery query = new CollQuery().from(var, ints);
        if (modifiers != null) {
            query.restrict(modifiers);
        }
        return query;
    }
}
