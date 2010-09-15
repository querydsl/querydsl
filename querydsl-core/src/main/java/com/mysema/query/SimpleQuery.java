/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import javax.annotation.Nonnegative;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.Param;

/**
 * SimpleQuery defines a simple querying interface than {@link Query}
 *
 * @author tiwe
 *
 * @param <Q>
 * @see Query
 */
public interface SimpleQuery<Q extends SimpleQuery<Q>> {

    /**
     * Defines the filter constraints
     *
     * @param e
     * @return
     */
    Q where(Predicate... e);

    /**
     * Defines the limit / max results for the query results
     *
     * @param limit
     * @return
     */
    Q limit(@Nonnegative long limit);

    /**
     * Defines the offset for the query results
     *
     * @param offset
     * @return
     */
    Q offset(@Nonnegative long offset);

    /**
     * Defines both limit and offset of the query results
     *
     * @param modifiers
     * @return
     */
    Q restrict(QueryModifiers modifiers);

    /**
     * Defines the order expressions
     *
     * @param o
     * @return
     */
    Q orderBy(OrderSpecifier<?>... o);

    /**
     * Set the given parameter to the given value
     *
     * @param <T>
     * @param param
     * @param value
     * @return
     */
    <T> Q set(Param<T> param, T value);

}
