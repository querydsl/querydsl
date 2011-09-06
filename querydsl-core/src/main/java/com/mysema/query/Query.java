/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * Query defines the main query interface of the fluent query language.
 *
 * <p>Note that the from method has been left out, since there are implementation
 * specific variants of it.</p>
 *
 * @author tiwe
 * @see SimpleQuery
 */
public interface Query<Q extends Query<Q>> extends SimpleQuery<Q>{

    /**
     * Defines the grouping/aggregation expressions
     *
     * @param o
     * @return
     */
    Q groupBy(Expression<?>... o);

    /**
     * Defines the filters for aggregation
     *
     * @param o
     * @return
     */
    Q having(Predicate... o);
   
}
