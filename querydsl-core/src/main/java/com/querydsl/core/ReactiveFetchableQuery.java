package com.querydsl.core;

import com.querydsl.core.types.Expression;

/**
 * {@code FetchableQuery} extends {@link ReactiveFetchable} and {@link SimpleQuery} with projection changing
 * methods and result aggregation functionality using {@link ResultTransformer} instances.
 *
 * @param <T> element type
 * @param <Q> concrete subtype
 */
public interface ReactiveFetchableQuery<T, Q extends ReactiveFetchableQuery<T, Q>> extends SimpleQuery<Q>, ReactiveFetchable<T> {

    /**
     * Change the projection of this query
     *
     * @param <U>
     * @param expr new projection
     * @return the current object
     */
    <U> ReactiveFetchableQuery select(Expression<U> expr);

    /**
     * Change the projection of this query
     *
     * @param exprs new projection
     * @return the current object
     */
    ReactiveFetchableQuery select(Expression<?>... exprs);

    /**
     * Apply the given transformer to this {@code FetchableQuery} instance and return the results
     *
     * @param <S>
     * @param transformer result transformer
     * @return transformed result
     */
    <S> S transform(ResultTransformer<S> transformer);

}

