/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.Predicate;

/**
 * QueryMetadata defines query metadata such as query sources, filtering
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
    void addGroupBy(Expression<?>... o);

    /**
     * Add the given having expressions
     *
     * @param o
     */
    void addHaving(Predicate... o);

    /**
     * Add the given query join
     *
     * @param joinType
     * @param expr
     */
    void addJoin(JoinType joinType, Expression<?> expr);
    
    /**
     * Add the given query join
     *
     */
    void addJoin(JoinExpression... join);

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
    void addOrderBy(OrderSpecifier<?>... o);

    /**
     * Add the given projections
     *
     * @param o
     */
    void addProjection(Expression<?>... o);

    /**
     * Add the given where expressions
     *
     * @param o
     */
    void addWhere(Predicate... o);

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
    List<? extends Expression<?>> getGroupBy();

    /**
     * Get the having expressions
     *
     * @return
     */
    @Nullable
    Predicate getHaving();

    /**
     * Get the query joins
     *
     * @return
     */
    List<JoinExpression> getJoins();

    /**
     * Get the QueryModifiers
     *
     * @return
     */
    @Nullable
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
    List<? extends Expression<?>> getProjection();

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
     * @return
     */
    Set<QueryFlag> getFlags();
}
