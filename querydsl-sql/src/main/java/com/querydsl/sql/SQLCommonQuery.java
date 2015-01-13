/*
 * Copyright 2011, Mysema Ltd
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
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;

/**
 * SQLCommonQuery is a common interface for SQLQuery and SQLSubQuery
 *
 * @author tiwe
 *
 * @param <Q> concrete type
 */
public interface SQLCommonQuery<Q extends SQLCommonQuery<Q>> extends Query<Q> {

    /**
     * Add the given Expression as a querydsl flag
     *
     * @param position
     * @param flag
     * @return
     */
    Q addFlag(Position position, Expression<?> flag);

    /**
     * Add the given String literal as querydsl flag
     *
     * @param position
     * @param flag
     * @return
     */
    Q addFlag(Position position, String flag);

    /**
     * Add the given prefix and expression as a general querydsl flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return
     */
    Q addFlag(Position position, String prefix, Expression<?> expr);

    /**
     * Add the given String literal as a join flag to the last added join with the
     * position BEFORE_TARGET
     *
     * @param flag
     * @return
     */
    Q addJoinFlag(String flag);

    /**
     * Add the given String literal as a join flag to the last added join
     *
     * @param flag
     * @param position
     * @return
     */
    Q addJoinFlag(String flag, JoinFlag.Position position);

    /**
     * Defines the sources of the querydsl
     *
     * @param o
     * @return
     */
    Q from(Expression<?>... o);

    /**
     * Adds a sub querydsl source
     *
     * @param subQuery
     * @param alias
     * @return
     */
    Q from(SubQueryExpression<?> subQuery, Path<?> alias);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @return
     */
    Q fullJoin(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o
     * @param alias
     * @return
     */
    <E> Q fullJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param key
     * @param entity
     * @return
     */
    <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity);

    /**
     * Adds a full join to the given target
     *
     * @param o
     * @param alias
     * @return
     */
    Q fullJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @return
     */
    Q innerJoin(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o
     * @param alias
     * @return
     */
    <E> Q innerJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds an inner join to the given target
     *
     * @param <E>
     * @param foreign
     * @param entity
     * @return
     */
    <E> Q innerJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds an inner join to the given target
     *
     * @param o
     * @param alias
     * @return
     */
    Q innerJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @return
     */
    Q join(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o
     * @param alias
     * @return
     */
    <E> Q join(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a join to the given target
     *
     * @param <E>
     * @param foreign
     * @param entity
     * @return
     */
    <E> Q join(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a join to the given target
     *
     * @param o
     * @param alias
     * @return
     */
    Q join(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @return
     */
    Q leftJoin(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o
     * @param alias
     * @return
     */
    <E> Q leftJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a left join to the given target
     *
     * @param <E>
     * @param foreign
     * @param entity
     * @return
     */
    <E> Q leftJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a left join to the given target
     *
     * @param o
     * @param alias
     * @return
     */
    Q leftJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Defines a filter to the last added join
     *
     * @param conditions
     * @return
     */
    Q on(Predicate... conditions);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @return
     */
    Q rightJoin(EntityPath<?> o);

    /**
     * Adds a full join to the given target
     *
     * @param <E>
     * @param o
     * @param alias
     * @return
     */
    <E> Q rightJoin(RelationalFunctionCall<E> o, Path<E> alias);

    /**
     * Adds a right join to the given target
     *
     * @param <E>
     * @param foreign
     * @param entity
     * @return
     */
    <E> Q rightJoin(ForeignKey<E> foreign, RelationalPath<E> entity);

    /**
     * Adds a right join to the given target
     *
     * @param o
     * @param alias
     * @return
     */
    Q rightJoin(SubQueryExpression<?> o, Path<?> alias);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.with(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param o
     * @return
     */
    Q with(Path<?> alias, SubQueryExpression<?> o);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.with(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param query
     * @return
     */
    Q with(Path<?> alias, Expression<?> query);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.with(alias, columns...).as(subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param columns
     * @return
     */
    WithBuilder<Q> with(Path<?> alias, Path<?>... columns);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.withRecursive(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param o
     * @return
     */
    Q withRecursive(Path<?> alias, SubQueryExpression<?> o);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.withRecursive(alias, subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param query
     * @return
     */
    Q withRecursive(Path<?> alias, Expression<?> query);

    /**
     * Adds a common table expression
     *
     * <p>Usage</p>
     * <pre>
     * querydsl.withRecursive(alias, columns...).as(subQuery)
     *      .from(...)
     * </pre>
     *
     * @param alias
     * @param columns
     * @return
     */
    WithBuilder<Q> withRecursive(Path<?> alias, Path<?>... columns);
}
