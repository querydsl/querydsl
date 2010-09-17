/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import com.mysema.query.types.Expression;
import com.mysema.query.types.ParametrizedExpression;

/**
 * CollectionExpression represents java.util.Collection typed expressions
 *
 * @author tiwe
 *
 * @param <E>
 * @see java.util.Collection
 */
public interface CollectionExpression<C extends Collection<E>, E> extends ParametrizedExpression<C>{

    /**
     * Get an expression for <code>this.contains(child)</code>
     *
     * @param child
     * @return this.contains(child)
     * @see java.util.Collection#contains(Object)
     */
    BooleanExpression contains(E child);

    /**
     * Get an expression for <code>this.contains(child)</code>
     *
     * @param child
     * @return
     * @see java.util.Collection#contains(Object)
     */
    BooleanExpression contains(Expression<E> child);

    /**
     * Get the element type of this path
     *
     */
    Class<E> getElementType();

    /**
     * Get an expression for <code>this.isEmpty()</code>
     *
     * @return this.isEmpty()
     * @see java.util.Collection#isEmpty()
     */
    BooleanExpression isEmpty();

    /**
     * Get an expression for <code>!this.isEmpty()</code>
     *
     * @return !this.isEmpty()
     * @see java.util.Collection#isEmpty()
     */
    BooleanExpression isNotEmpty();

    /**
     * Get an expression for <code>this.size()</code>
     *
     * @return this.size()
     * @see java.util.Collection#size()
     */
    NumberExpression<Integer> size();
}
