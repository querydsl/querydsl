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
 * @param <EA>
 */
public interface ArrayExpression<E> {

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
    SimpleExpression<E> get(Expression<Integer> index);

    /**
     * Get the element at the given index
     *
     * @param index
     * @return
     */
    SimpleExpression<E> get(@Nonnegative int index);

}
