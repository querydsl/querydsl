/*
 * Copyright 2011, Mysema Ltd
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

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Expression;
import static org.junit.Assert.assertTrue;

public final class QueryMutability<T extends QueryBase<T> & Projectable> {

    private final T query;

    private final QueryMetadata metadata;

    public QueryMutability(T query) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
        this.query = query;
        this.metadata = (QueryMetadata) query.getClass().getMethod("getMetadata").invoke(query);
    }

    public void test(Expression<?> p1, Expression<?> p2) throws IOException {
        System.err.println("count");
        query.count();
        assertProjectionEmpty();

        System.err.println("countDistinct");
        query.distinct().count();
        assertProjectionEmpty();

        System.err.println("iterate");
        query.iterate(p1);
        assertProjectionEmpty();

        query.iterate(p1, p2);
        assertProjectionEmpty();

        System.err.println("iterateDistinct");
        query.distinct().iterate(p1);
        assertProjectionEmpty();

        query.distinct().iterate(p1, p2);
        assertProjectionEmpty();

        System.err.println("list");
        query.list(p1);
        assertProjectionEmpty();

        query.list(p1, p2);
        assertProjectionEmpty();

        System.err.println("distinct list");
        query.distinct().list(p1);
        assertProjectionEmpty();

        query.distinct().list(p1, p2);
        assertProjectionEmpty();

        System.err.println("listResults");
        query.listResults(p1);
        assertProjectionEmpty();

        System.err.println("distinct listResults");
        query.distinct().listResults(p1);
        assertProjectionEmpty();

        System.err.println("map");
        query.map(p1, p2);
        assertProjectionEmpty();

        System.err.println("uniqueResult");
        query.uniqueResult(p1);
        assertProjectionEmpty();

        query.uniqueResult(p1, p2);
        assertProjectionEmpty();
    }

    private void assertProjectionEmpty() throws IOException{
        assertTrue(metadata.getProjection().isEmpty());
        if (query instanceof Closeable) {
            ((Closeable)query).close();
        }
    }

}
