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
package com.querydsl.sql;

import com.querydsl.core.JoinFlag;
import com.querydsl.core.Query;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.*;

/**
 * {@code SQLCommonQuery} is a common interface for SQLQuery and SQLSubQuery
 *
 * @author tiwe
 *
 * @param <Q> concrete type
 */
public interface SQLCommonQuery<Q extends SQLCommonQuery<Q>> extends Query<Q> {

    /**
     * Add the given Expression as a query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    Q addFlag(Position position, Expression<?> flag);

    /**
     * Add the given String literal as query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    Q addFlag(Position position, String flag);

    /**
     * Add the given prefix and expression as a general query flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return the current object
     */
    Q addFlag(Position position, String prefix, Expression<?> expr);

    /**
     * Add the given String literal as a join flag to the last added join with the
     * position BEFORE_TARGET
     *
     * @param flag join flag
     * @return the current object
     */
    Q addJoinFlag(String flag);

    /**
     * Add the given String literal as a join flag to the last added join
     *
     * @param flag join flag
     * @param position position
     * @return the current object
     */
    Q addJoinFlag(String flag, JoinFlag.Position position);

    /**
     * Defines the sources of the query
     *
     * @param o from
     * @return the current object
     */
    Q from(Expression<?>... o);

    /**
     * Adds a sub query source
     *
     * @param subQuery sub query
     * @param alias alias
     * @return the current object
     */
    Q from(SubQueryExpression<?> subQuery, Path<?> alias);

    /**
     * Adds a full join to the given target
     *
     * @param o full join target
     * @return the current object
     */
    Q fullJoin(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o full join target
     * @param alias alias
     * @return the current object
     */
    <E> Q fullJoin(EntityPath<E> o, Path<E> alias);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o full join target
     * @param alias alias
     * @return the current object
     */
    <E> Q fullJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param key foreign key for join
     * @param entity join target
     * @return the current object
     */
    <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity);

    /**
     * Adds a full join to the given target
     *
     * @param o subquery
     * @param alias alias
     * @return the current object
     */
    Q fullJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return the current object
     */
    Q innerJoin(EntityPath<?> o);

    /**
     * Adds an inner join to the given target
     *
     * @param <E>
     * @param o inner join target
     * @param alias alias
     * @return the current object
     */
    <E> Q innerJoin(EntityPath<E> o, Path<E> alias);

    /**
     * Adds a inner join to the given target
     *
     * @param <E>
     * @param o relational function call
     * @param alias alias
     * @return the current object
     */
    <E> Q innerJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param <E>
     * @param foreign foreign key to use for join
     * @param entity join target
     * @return the current object
     */
    <E> Q innerJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds an inner join to the given target
     *
     * @param o subquery
     * @param alias alias
     * @return the current object
     */
    Q innerJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a join to the given target
     *
     * @param o join target
     * @return the current object
     */
    Q join(EntityPath<?> o);

    /**
     * Adds a join to the given target
     *
     * @param <E>
     * @param o join target
     * @param alias alias
     * @return the current object
     */
    <E> Q join(EntityPath<E> o, Path<E> alias);

    /**
     * Adds a join to the given target
     *
     * @param <E>
     * @param o join target
     * @param alias alias
     * @return the current object
     */
    <E> Q join(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a join to the given target
     *
     * @param <E>
     * @param foreign foreign key to use for join
     * @param entity join target
     * @return the current object
     */
    <E> Q join(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a join to the given target
     *
     * @param o subquery
     * @param alias alias
     * @return the current object
     */
    Q join(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a left join to the given target
     *
     * @param o join target
     * @return the current object
     */
    Q leftJoin(EntityPath<?> o);

    /**
     * Adds a left join to the given target
     *
     * @param <E>
     * @param o left join target
     * @param alias alias
     * @return the current object
     */
    <E> Q leftJoin(EntityPath<E> o, Path<E> alias);

    /**
     * Adds a left join to the given target
     *
     * @param <E>
     * @param o relational function call
     * @param alias alias
     * @return the current object
     */
    <E> Q leftJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a left join to the given target
     *
     * @param <E>
     * @param foreign foreign key to use for join
     * @param entity join target
     * @return the current object
     */
    <E> Q leftJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a left join to the given target
     *
     * @param o subquery
     * @param alias alias
     * @return the current object
     */
    Q leftJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Defines a filter to the last added join
     *
     * @param conditions join conditions
     * @return the current object
     */
    Q on(Predicate... conditions);

    /**
     * Adds a right join to the given target
     *
     * @param o join target
     * @return the current object
     */
    Q rightJoin(EntityPath<?> o);

    /**
     * Adds a right join to the given target
     *
     * @param <E>
     * @param o right join target
     * @param alias alias
     * @return the current object
     */
    <E> Q rightJoin(EntityPath<E> o, Path<E> alias);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o relational function call
     * @param alias alias
     * @return the current object
     */
    <E> Q rightJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a right join to the given target
     *
     * @param <E>
     * @param foreign foreign key to use for join
     * @param entity join target
     * @return the current object
     */
    <E> Q rightJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a right join to the given target
     *
     * @param o subquery
     * @param alias alias
     * @return the current object
     */
    Q rightJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.with(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param o subquery
     * @return the current object
     */
    Q with(Path<?> alias, SubQueryExpression<?> o);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.with(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param query subquery
     * @return the current object
     */
    Q with(Path<?> alias, Expression<?> query);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.with(alias, columns...).as(subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param columns columns to use
     * @return the current object
     */
    WithBuilder<Q> with(Path<?> alias, Path<?>... columns);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.withRecursive(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param o subquery
     * @return the current object
     */
    Q withRecursive(Path<?> alias, SubQueryExpression<?> o);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.withRecursive(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param query subquery
     * @return the current object
     */
    Q withRecursive(Path<?> alias, Expression<?> query);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * query.withRecursive(alias, columns...).as(subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias alias for query
     * @param columns columns to use
     * @return builder for with part
     */
    WithBuilder<Q> withRecursive(Path<?> alias, Path<?>... columns);
}
