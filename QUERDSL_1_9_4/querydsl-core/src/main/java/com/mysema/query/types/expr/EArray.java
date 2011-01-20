/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import javax.annotation.Nonnegative;

import com.mysema.query.types.Expr;

/**
 * EArray defines an interface for array typed expression
 *
 * @author tiwe
 *
 * @param <EA>
 */
public interface EArray<E> {

    /**
     * Get the size of the array
     *
     * @return
     */
    ENumber<Integer> size();

    /**
     * Get the element at the given index
     *
     * @param index
     * @return
     */
    Expr<E> get(Expr<Integer> index);

    /**
     * Get the element at the given index
     *
     * @param index
     * @return
     */
    Expr<E> get(@Nonnegative int index);

}
