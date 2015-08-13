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
package com.querydsl.jpa;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;

/**
 * Common interface for JPA related QueryFactory implementations
 *
 * @author tiwe
 *
 */
public interface JPQLQueryFactory extends QueryFactory<JPQLQuery<?>> {

    /**
     * Create a new DELETE clause
     *
     * @param path entity to delete from
     * @return delete clause
     */
    DeleteClause<?> delete(EntityPath<?> path);

    /**
     * Create a new JPQLQuery instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
     */
    <T> JPQLQuery<T> select(Expression<T> expr);

    /**
     * Create a new JPQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    JPQLQuery<Tuple> select(Expression<?>... exprs);

    /**
     * Create a new JPQLQuery instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(distinct expr)
     */
    <T> JPQLQuery<T> selectDistinct(Expression<T> expr);

    /**
     * Create a new JPQLQuery instance with the given projection
     *
     * @param exprs projection
     * @return select(distinct exprs)
     */
    JPQLQuery<Tuple> selectDistinct(Expression<?>... exprs);

    /**
     * Create a new JPQLQuery instance with the projection one
     *
     * @return select(1)
     */
    JPQLQuery<Integer> selectOne();

    /**
     * Create a new JPQLQuery instance with the projection zero
     *
     * @return select(0)
     */
    JPQLQuery<Integer> selectZero();

    /**
     * Create a new JPQLQuery instance with the given source and projection
     *
     * @param from projection and source
     * @param <T>
     * @return select(from).from(from)
     */
    <T> JPQLQuery<T> selectFrom(EntityPath<T> from);

    /**
     * Create a new JPQLQuery instance with the given source and projection
     *
     * @param from projection and source
     * @param <T>
     * @return select(from).from(from)
     */
    <T> JPQLQuery<Tuple> selectFrom(EntityPath<T>... from);

    /**
     * Create a new Query with the given source
     *
     * @param from from
     * @return from(from)
     */
    JPQLQuery<?> from(EntityPath<?> from);

    /**
     * Create a new Query with the given source
     *
     * @param from from
     * @return from(from)
     */
    JPQLQuery<?> from(EntityPath<?>... from);

    /**
     * Create a new UPDATE clause
     *
     * @param path entity to update
     * @return update clause
     */
    UpdateClause<?> update(EntityPath<?> path);

}