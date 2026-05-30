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
package com.querydsl.core.group;

import static com.querydsl.core.group.GroupBy.groupBy;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.Tuple;
import com.querydsl.core.support.DummyFetchableQuery;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

/**
 * Verifies that the {@link CloseableIterator} obtained from the query is always closed by the
 * {@code GroupBy} transformers, even when iteration fails midway. Failing to close the iterator
 * leaks the underlying resource (e.g. a JDBC cursor).
 *
 * @see <a href="https://github.com/querydsl/querydsl/issues/3957">#3957</a>
 */
public class GroupByCloseOnExceptionTest {

    private static final NumberPath<Integer> key = Expressions.numberPath(Integer.class, "key");

    private static final StringPath value = Expressions.stringPath("value");

    /**
     * A {@link CloseableIterator} that yields a single row and then throws on the next access,
     * recording whether it was closed.
     */
    private static class FailingCloseableIterator implements CloseableIterator<Tuple> {

        private boolean closed = false;

        private boolean firstReturned = false;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Tuple next() {
            if (!firstReturned) {
                firstReturned = true;
                return new MockTuple(new Object[]{1, "first"});
            }
            throw new RuntimeException("iteration failed");
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    private FailingCloseableIterator iterator;

    private DummyFetchableQuery<Tuple> failingQuery() {
        iterator = new FailingCloseableIterator();
        return new DummyFetchableQuery<Tuple>(new ArrayList<Tuple>()) {
            @Override
            public CloseableIterator<Tuple> iterate() {
                return iterator;
            }
        };
    }

    private void assertClosedAfterFailure(ResultTransformer<?> transformer) {
        try {
            failingQuery().transform(transformer);
            fail("expected the failing iterator to propagate an exception");
        } catch (RuntimeException expected) {
            // expected
        }
        assertTrue("iterator should be closed even when iteration fails", iterator.closed);
    }

    @Test
    public void list_closes_iterator_on_exception() {
        assertClosedAfterFailure(groupBy(key).list(value));
    }

    @Test
    public void collection_closes_iterator_on_exception() {
        assertClosedAfterFailure(groupBy(key).collection(ArrayList::new, value));
    }

    @Test
    public void map_closes_iterator_on_exception() {
        assertClosedAfterFailure(groupBy(key).as(value));
    }
}
