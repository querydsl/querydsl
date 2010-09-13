/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.mysema.query.types.Expression;

public final class QueryMutability {

    private final Projectable query;

    private final QueryMetadata metadata;

    public QueryMutability(Projectable query) throws SecurityException,
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
        query.countDistinct();
        assertProjectionEmpty();

        System.err.println("iterate");
        query.iterate(p1);
        assertProjectionEmpty();

        query.iterate(p1, p2);
        assertProjectionEmpty();

        System.err.println("iterateDistinct");
        query.iterateDistinct(p1);
        assertProjectionEmpty();

        query.iterateDistinct(p1, p2);
        assertProjectionEmpty();

        System.err.println("list");
        query.list(p1);
        assertProjectionEmpty();

        query.list(p1, p2);
        assertProjectionEmpty();

        System.err.println("listDistinct");
        query.listDistinct(p1);
        assertProjectionEmpty();

        query.listDistinct(p1, p2);
        assertProjectionEmpty();

        System.err.println("listResults");
        query.listResults(p1);
        assertProjectionEmpty();

        System.err.println("listDistinctResults");
        query.listDistinctResults(p1);
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
        if (query instanceof Closeable){
            ((Closeable)query).close();
        }
        System.err.println();
    }

}
