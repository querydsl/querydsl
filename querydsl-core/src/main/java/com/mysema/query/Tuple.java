/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;

/**
 * Tuple defines an interface for generic query result projection
 *
 * @author tiwe
 *
 */
public interface Tuple {

    /**
     * Get a Tuple element by index
     *
     * @param <T>
     * @param index
     * @param type
     * @return
     */
    @Nullable
    <T> T get(int index, Class<T> type);

    /**
     * Get a tuple element by expression
     *
     * @param <T>
     * @param expr
     * @return
     */
    @Nullable
    <T> T get(Expression<T> expr);

    /**
     * Get the content as an Object array
     *
     * @return
     */
    Object[] toArray();

}
