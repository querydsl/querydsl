package com.querydsl.core.support;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;

/**
 * {@code ExtendedSubQuery} extends the {@link SubQueryExpression} interface to provide fluent
 * expression creation functionality
 *
 * @param <T>
 */
public interface ExtendedSubQuery<T> extends SubQueryExpression<T> {

    /**
     * Create a {@code this == right} expression
     *
     * @param expr rhs of the comparison
     * @return this == right
     */
    BooleanExpression eq(Expression<? extends T> expr);

    /**
     * Create a {@code this == right} expression
     *
     * @param constant rhs of the comparison
     * @return this == right
     */
    BooleanExpression eq(T constant);

    /**
     * Create a {@code this != right} expression
     *
     * @param expr rhs of the comparison
     * @return this != right
     */
    BooleanExpression ne(Expression<? extends T> expr);

    /**
     * Create a {@code this != right} expression
     *
     * @param constant rhs of the comparison
     * @return this != right
     */
    BooleanExpression ne(T constant);

    /**
     * Create a {@code right in this} expression
     *
     * @param right rhs of the comparison
     * @return right in this
     */
    BooleanExpression contains(Expression<? extends T> right);

    /**
     * Create a {@code right in this} expression
     *
     * @param constant rhs of the comparison
     * @return right in this
     */
    BooleanExpression contains(T constant);

    /**
     * Create a {@code exists(this)} expression
     *
     * @return exists(this)
     */
    BooleanExpression exists();

    /**
     * Create a {@code not exists(this)} expression
     *
     * @return not exists(this)
     */
    BooleanExpression notExists();

    /**
     * Create a {@code this < right} expression
     *
     * @param expr rhs of the comparison
     * @return this &lt; right
     */
    BooleanExpression lt(Expression<? extends T> expr);

    /**
     * Create a {@code this < right} expression
     *
     * @param constant rhs of the comparison
     * @return this &lt; right
     */
    BooleanExpression lt(T constant);

    /**
     * Create a {@code this > right} expression
     *
     * @param expr rhs of the comparison
     * @return this &gt; right
     */
    BooleanExpression gt(Expression<? extends T> expr);

    /**
     * Create a {@code this > right} expression
     *
     * @param constant rhs of the comparison
     * @return this &gt; right
     */
    BooleanExpression gt(T constant);

    /**
     * Create a {@code this <= right} expression
     *
     * @param expr rhs of the comparison
     * @return this &lt;= right
     */
    BooleanExpression loe(Expression<? extends T> expr);

    /**
     * Create a {@code this <= right} expression
     *
     * @param constant rhs of the comparison
     * @return this &lt;= right
     */
    BooleanExpression loe(T constant);

    /**
     * Create a {@code this >= right} expression
     *
     * @param expr rhs of the comparison
     * @return this &gt;= right
     */
    BooleanExpression goe(Expression<? extends T> expr);

    /**
     * Create a {@code this >= right} expression
     *
     * @param constant rhs of the comparison
     * @return this &gt;= right
     */
    BooleanExpression goe(T constant);

}
