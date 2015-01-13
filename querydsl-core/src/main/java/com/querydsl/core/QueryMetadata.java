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
package com.querydsl.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

/**
 * QueryMetadata defines querydsl metadata such as querydsl sources, filtering
 * conditions and the projection
 *
 * @author tiwe
 */
public interface QueryMetadata extends Serializable {

    /**
     * Add the given group by expressions
     *
     * @param o
     */
    void addGroupBy(Expression<?> o);

    /**
     * Add the given having expressions
     *
     * @param o
     */
    void addHaving(Predicate o);

    /**
     * Add the given querydsl join
     *
     * @param joinType
     * @param expr
     */
    void addJoin(JoinType joinType, Expression<?> expr);

    /**
     * Add the given join flag to the last given join
     *
     * @param flag
     */
    void addJoinFlag(JoinFlag flag);

    /**
     * Add the given join condition to the last given join
     *
     * @param o
     */
    void addJoinCondition(Predicate o);

    /**
     * Add the given order specifiers
     *
     * @param o
     */
    void addOrderBy(OrderSpecifier<?> o);

    /**
     * Add the given projections
     *
     * @param o
     */
    void addProjection(Expression<?> o);

    /**
     * Add the given where expressions
     *
     * @param o
     */
    void addWhere(Predicate o);

    /**
     * Clear the order expressions
     */
    void clearOrderBy();

    /**
     * Clear the projection
     */
    void clearProjection();

    /**
     * Clear the where expressions
     */
    void clearWhere();

    /**
     * Clone this QueryMetadata instance
     *
     * @return
     */
    QueryMetadata clone();

    /**
     * Get the group by expressions
     *
     * @return
     */
    List<Expression<?>> getGroupBy();

    /**
     * Get the having expressions
     *
     * @return
     */
    Predicate getHaving();

    /**
     * Get the querydsl joins
     *
     * @return
     */
    List<JoinExpression> getJoins();

    /**
     * Get the QueryModifiers
     *
     * @return
     */
    QueryModifiers getModifiers();

    /**
     * Get the OrderSpecifiers
     *
     * @return
     */
    List<OrderSpecifier<?>> getOrderBy();

    /**
     * Get the projection
     *
     * @return
     */
    List<Expression<?>> getProjection();

    /**
     * Get the parameters
     *
     * @return
     */
    Map<ParamExpression<?>,Object> getParams();

    /**
     * Get the expressions aggregated into a single boolean expression or null,
     * if none where defined
     *
     * @return
     */
    @Nullable
    Predicate getWhere();

    /**
     * Get whether the projection is distinct
     *
     * @return
     */
    boolean isDistinct();

    /**
     * Get whether the projection is unique
     *
     * @return
     */
    boolean isUnique();

    /**
     * Reset the projection
     */
    void reset();

    /**
     * @param distinct
     */
    void setDistinct(boolean distinct);

    /**
     * @param limit
     */
    void setLimit(@Nullable Long limit);

    /**
     * @param restriction
     */
    void setModifiers(QueryModifiers restriction);

    /**
     * @param offset
     */
    void setOffset(@Nullable Long offset);

    /**
     * @param unique
     */
    void setUnique(boolean unique);

    /**
     * @param <T>
     * @param param
     * @param value
     */
    <T> void setParam(ParamExpression<T> param, T value);

    /**
     * @param flag
     */
    void addFlag(QueryFlag flag);

    /**
     * @param flag
     * @return
     */
    boolean hasFlag(QueryFlag flag);

    /**
     * @param flag
     */
    void removeFlag(QueryFlag flag);

    /**
     * @return
     */
    Set<QueryFlag> getFlags();

    /**
     * @param v
     */
    void setValidate(boolean v);
}
