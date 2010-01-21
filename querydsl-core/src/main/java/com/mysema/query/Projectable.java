/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.mysema.query.types.expr.Expr;

/**
 * Projectable defines default projection methods for Query implementations.
 * All Querydsl query implementations should implement this interface.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Projectable {
    /**
     * return the amount of matched rows
     */
    @Nonnegative
    long count();

    /**
     * return the amount of distinct matched rows
     */
    @Nonnegative
    long countDistinct();

    /**
     * iterate over the results for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * iterate over the results for the given projection
     * 
     * @param args
     * @return
     */
    Iterator<Object[]> iterate(Expr<?>[] args);

    /**
     * iterate over the results for the given projection
     * 
     * @param <RT>
     *            generic type of the Iteratpr
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> Iterator<RT> iterate(Expr<RT> projection);

    /**
     * iterate over the distinct results for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    Iterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * iterate over the distinct results for the given projection
     * 
     * @param args
     * @return
     */
    Iterator<Object[]> iterateDistinct(Expr<?>[] args);

    /**
     * iterate over the distinct results for the given projection
     * 
     * @param <RT>
     *            generic type of the Iteratpr
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> Iterator<RT> iterateDistinct(Expr<RT> projection);

    /**
     * list the results for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * list the results for the given projection
     * 
     * @param args
     * @return
     */
    List<Object[]> list(Expr<?>[] args);

    /**
     * list the results for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> list(Expr<RT> projection);

    /**
     * list the distinct results for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * list the distinct results for the given projection
     * 
     * @param args
     * @return
     */
    List<Object[]> listDistinct(Expr<?>[] args);

    /**
     * list the distinct results for the given projection
     * 
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> listDistinct(Expr<RT> projection);

    /**
     * list the results for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT> SearchResults<RT> listResults(Expr<RT> projection);
    
    /**
     * list the distinct results for the given projection
     * 
     * @param <RT>
     * @param projection
     * @return
     */
    <RT> SearchResults<RT> listDistinctResults(Expr<RT> projection);
    
    /**
     * return the given projection as a Map instance using key and value for Map population
     * 
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return
     */
    <K,V> Map<K,V> map(Expr<K> key, Expr<V> value);

    /**
     * return a unique result for the given projection
     * 
     * @param first
     * @param second
     * @param rest
     * @return
     */
    @Nullable
    Object[] uniqueResult(Expr<?> first, Expr<?> second, Expr<?>... rest);
    
    /**
     * return a unique result for the given projection
     * 
     * @param args
     * @return
     */
    @Nullable
    Object[] uniqueResult(Expr<?>[] args);

    /**
     * return a unique result for the given projection
     * 
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    @Nullable
    <RT> RT uniqueResult(Expr<RT> projection);

}