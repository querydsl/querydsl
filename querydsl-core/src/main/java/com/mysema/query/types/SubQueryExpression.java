/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.query.Detachable;

/**
 *
 * SubQueryExpression represents a sub query. The actual construction of a subquery
 * is done via an {@link Detachable} instance.
 *
 * @author tiwe
 *
 * @param <T> return type of subquery
 */
public interface SubQueryExpression<T> extends Expression<T>{

    /**
     * Get the query metadata for the subquery
     *
     * @return
     */
    QueryMetadata getMetadata();

}
