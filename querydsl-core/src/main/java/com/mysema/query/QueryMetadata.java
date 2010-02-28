/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * QueryMetadata defines query metadata such as query sources, filtering
 * conditions and the projection
 * 
 * @author tiwe
 * @version $Id$
 */
public interface QueryMetadata extends Serializable {

    /**
     * @param o
     */
    void addFrom(Expr<?>... o);
    
    /**
     * @param o
     */
    void addGroupBy(Expr<?>... o);

    /**
     * @param o
     */
    void addHaving(EBoolean... o);

    /**
     * @param joinType
     * @param expr
     */
    void addJoin(JoinType joinType, Expr<?> expr);

    /**
     * @param o
     */
    void addJoinCondition(EBoolean o);

    /**
     * @param o
     */
    void addOrderBy(OrderSpecifier<?>... o);

    /**
     * @param o
     */
    void addProjection(Expr<?>... o);

    /**
     * @param o
     */
    void addWhere(EBoolean... o);

    /**
     * @return
     */
    List<? extends Expr<?>> getGroupBy();

    /**
     * @return
     */
    @Nullable
    EBoolean getHaving();

    /**
     * @return
     */
    List<JoinExpression> getJoins();

    /**
     * @return
     */
    @Nullable
    QueryModifiers getModifiers();

    /**
     * @return
     */
    List<OrderSpecifier<?>> getOrderBy();

    /**
     * @return
     */
    List<? extends Expr<?>> getProjection();

    /**
     * @return
     */
    @Nullable
    EBoolean getWhere();
    
    /**
     * @return
     */
    boolean isDistinct();
    
    /**
     * @return
     */
    boolean isUnique();
    
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
     * 
     */
    void reset();

    /**
     * @return
     */
    QueryMetadata clone();
}