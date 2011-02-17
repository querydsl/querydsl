/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.types.Expression;

/**
 * Projectable defines default projection methods for {@link Query} implementations.
 * All Querydsl query implementations should implement either this interface or
 * {@link SimpleProjectable}.
 *
 * @author tiwe
 * @see SimpleProjectable
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
     * @return true, if rows matching the given criteria exist, otherwise false
     */
    boolean exists();

    /**
     * @return true, if no rows matching the given criteria exist, otherwise false
     */
    boolean notExists();

    /**
     * iterate over the results for the given projection
     *
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    CloseableIterator<Object[]> iterate(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * iterate over the results for the given projection
     *
     * @param args
     * @return
     */
    CloseableIterator<Object[]> iterate(Expression<?>[] args);

    /**
     * iterate over the results for the given projection
     *
     * @param <RT>
     *            generic type of the Iterator
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> CloseableIterator<RT> iterate(Expression<RT> projection);

    /**
     * iterate over the distinct results for the given projection
     *
     * @param first
     * @param second
     * @param rest
     * @return an Iterator over the projection
     */
    CloseableIterator<Object[]> iterateDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * iterate over the distinct results for the given projection
     *
     * @param args
     * @return
     */
    CloseableIterator<Object[]> iterateDistinct(Expression<?>[] args);

    /**
     * iterate over the distinct results for the given projection
     *
     * @param <RT>
     *            generic type of the Iteratpr
     * @param projection
     * @return an Iterator over the projection
     */
    <RT> CloseableIterator<RT> iterateDistinct(Expression<RT> projection);

    /**
     * list the results for the given projection
     *
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    List<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * list the results for the given projection
     *
     * @param args
     * @return
     */
    List<Object[]> list(Expression<?>[] args);

    /**
     * list the results for the given projection
     *
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> list(Expression<RT> projection);

    /**
     * list the distinct results for the given projection
     *
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    List<Object[]> listDistinct(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * list the distinct results for the given projection
     *
     * @param args
     * @return
     */
    List<Object[]> listDistinct(Expression<?>[] args);

    /**
     * list the distinct results for the given projection
     *
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> List<RT> listDistinct(Expression<RT> projection);

    /**
     * list the results for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT> SearchResults<RT> listResults(Expression<RT> projection);

    /**
     * list the distinct results for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT> SearchResults<RT> listDistinctResults(Expression<RT> projection);

    /**
     * return the given projection as a Map instance using key and value for Map population
     *
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return
     */
    <K,V> Map<K,V> map(Expression<K> key, Expression<V> value);

    /**
     * return a unique result for the given projection or null if no result is found
     * 
     * <p>for multiple results only the first one is returned</p>
     *
     * @param first
     * @param second
     * @param rest
     * @throws NonUniqueResultException if there is more than one matching result
     * @return
     */
    @Nullable
    Object[] uniqueResult(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * return a unique result for the given projection or null if no result is found
     *
     * @param args
     * @throws NonUniqueResultException if there is more than one matching result
     * @return
     */
    @Nullable
    Object[] uniqueResult(Expression<?>[] args);

    /**
     * return a unique result for the given projection or null if no result is found
     *
     * @param <RT>
     *            return type
     * @param projection
     * @throws NonUniqueResultException if there is more than one matching result
     * @return the result or null for an empty result
     */
    @Nullable
    <RT> RT uniqueResult(Expression<RT> projection);

}
