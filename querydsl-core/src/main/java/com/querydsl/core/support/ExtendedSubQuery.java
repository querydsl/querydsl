/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.support;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanOperation;

import java.util.Collection;

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

    /**
     * Create a {@code this is null} expression
     *
     * @return this is null
     */
     BooleanOperation isNull();

    /**
     * Create a {@code this is not null} expression
     *
     * @return this is not null
     */
     BooleanOperation isNotNull();

    /**
     * Create a {@code this is (a, b, c)} express
     * @param right
     * @return this in (a, b, c)
     */
     BooleanExpression in(Collection<? extends T> right);

     /**
     * Create a {@code this is (a, b, c)} express
     * @param right
     * @return this in (a, b, c)
     */
     BooleanExpression in(T... right);

}
