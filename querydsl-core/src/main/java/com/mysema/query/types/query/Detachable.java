/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;

/**
 * Detachable defines methods for the construction of SubQuery instances
 *
 * @author tiwe
 *
 */
public interface Detachable {

    /**
     * Return the count of matched rows as a sub query
     *
     * @return
     */
    ObjectSubQuery<Long> count();

    /**
     * Create an exists(this) expression
     *
     * @return
     */
    BooleanExpression exists();

    /**
     * Create a projection expression for the given projection
     *
     * @param first
     * @param second
     * @param rest
     *            rest
     * @return a List over the projection
     */
    ListSubQuery<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * Create a projection expression for the given projection
     *
     * @param args
     * @return
     */
    ListSubQuery<Object[]> list(Expression<?>[] args);

    /**
     * Create a projection expression for the given projection
     *
     * @param <RT>
     *            generic type of the List
     * @param projection
     * @return a List over the projection
     */
    <RT> ListSubQuery<RT> list(Expression<RT> projection);

    /**
     * Create an not exists(this) expression
     *
     * @return
     */
    BooleanExpression notExists();

    /**
     * Create a projection expression for the given projection
     *
     * @param first
     * @param second
     * @param rest
     * @return
     */
    ObjectSubQuery<Object[]> unique(Expression<?> first, Expression<?> second, Expression<?>... rest);

    /**
     * Create a projection expression for the given projection
     *
     * @param args
     * @return
     */
    ObjectSubQuery<Object[]> unique(Expression<?>[] args);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     *            return type
     * @param projection
     * @return the result or null for an empty result
     */
    <RT> ObjectSubQuery<RT> unique(Expression<RT> projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param projection
     * @return
     */
    BooleanSubQuery unique(BooleanExpression projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param projection
     * @return
     */
    StringSubQuery unique(StringExpression projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> ComparableSubQuery<RT> unique(ComparableExpression<RT> projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> DateSubQuery<RT> unique(DateExpression<RT> projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(DateTimeExpression<RT> projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Comparable<?>> TimeSubQuery<RT> unique(TimeExpression<RT> projection);

    /**
     * Create a subquery expression for the given projection
     *
     * @param <RT>
     * @param projection
     * @return
     */
    <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection);

}
