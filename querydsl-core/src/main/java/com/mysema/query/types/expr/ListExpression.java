/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.List;

import javax.annotation.Nonnegative;

import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.Expression;

/**
 * ListExpression represents java.util.List typed expressions
 *
 * @author tiwe
 *
 * @param <E> component type
 * @see java.util.List
 */
public interface ListExpression<E, Q extends SimpleExpression<? super E>> extends CollectionExpression<List<E>, E> {

    /**
     * Indexed access
     *
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Q get(Expression<Integer> index);

    /**
     * Indexed access
     *
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Q get(@Nonnegative int index);
}
