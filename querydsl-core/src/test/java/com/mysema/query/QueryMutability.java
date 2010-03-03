package com.mysema.query;

import static org.junit.Assert.assertTrue;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mysema.query.types.expr.Expr;

public final class QueryMutability {

    private QueryMutability() {}

    public static void test(Projectable query, Expr<?> p1, Expr<?> p2)
            throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, IOException {
        Method m = query.getClass().getMethod("getMetadata");
        
        System.err.println("count");
        query.count();
        assertProjectionEmpty(query, m);
        
        System.err.println("countDistinct");
        query.countDistinct();
        assertProjectionEmpty(query, m);

        System.err.println("iterate");
        query.iterate(p1);
        assertProjectionEmpty(query, m);
        
        query.iterate(p1, p2);
        assertProjectionEmpty(query, m);
        
        System.err.println("iterateDistinct");
        query.iterateDistinct(p1);
        assertProjectionEmpty(query, m);
        
        query.iterateDistinct(p1, p2);
        assertProjectionEmpty(query, m);

        System.err.println("list");
        query.list(p1);
        assertProjectionEmpty(query, m);
        
        query.list(p1, p2);
        assertProjectionEmpty(query, m);
        
        System.err.println("listDistinct");
        query.listDistinct(p1);
        assertProjectionEmpty(query, m);
        
        query.listDistinct(p1, p2);
        assertProjectionEmpty(query, m);

        System.err.println("listResults");
        query.listResults(p1);
        assertProjectionEmpty(query, m);
        
        System.err.println("listDistinctResults");
        query.listDistinctResults(p1);
        assertProjectionEmpty(query, m);

        System.err.println("map");
        query.map(p1, p2);
        assertProjectionEmpty(query, m);

        System.err.println("uniqueResult");
        query.uniqueResult(p1);
        assertProjectionEmpty(query, m);
        
        query.uniqueResult(p1, p2);
        assertProjectionEmpty(query, m);
    }

    private static void assertProjectionEmpty(Projectable query, Method m)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, IOException {
        QueryMetadata metadata = (QueryMetadata) m.invoke(query);
        assertTrue(metadata.getProjection().isEmpty());
        if (query instanceof Closeable){
            ((Closeable)query).close();
        }
        System.err.println();
    }

}
