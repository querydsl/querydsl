/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.query.Detachable;

/**
 *
 * SubQueryExpression represents a sub query. The actual construction of a subquery
 * is done via an {@link Detachable} instance.
 *
 * @author tiwe
 * @version $Id$
 *
 * @param <T>
 */
public interface SubQueryExpression<T> extends Expression<T>{

    /**
     * Get an exists(this) expression for the subquery
     *
     * @return
     */
    BooleanExpression exists();

    /**
     * Get the query metadata for the subquery
     *
     * @return
     */
    QueryMetadata getMetadata();

    /**
     * Get a not exists(this) expression for the subquery
     *
     * @return
     */
    BooleanExpression notExists();

}
