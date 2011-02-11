/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import javax.annotation.Nonnegative;

import com.mysema.query.types.Expression;

/**
 * ArrayExpression defines an interface for array typed expression
 *
 * @author tiwe
 *
 * @param <T> array element type
 */
public interface ArrayExpression<T> extends Expression<T[]>{

    /**
     * Get the size of the array
     *
     * @return
     */
    NumberExpression<Integer> size();

    /**
     * Get the element at the given index
     *
     * @param index
     * @return
     */
    SimpleExpression<T> get(Expression<Integer> index);

    /**
     * Get the element at the given index
     *
     * @param index
     * @return
     */
    SimpleExpression<T> get(@Nonnegative int index);

}
