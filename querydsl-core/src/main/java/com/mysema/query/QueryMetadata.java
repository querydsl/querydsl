/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

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
public interface QueryMetadata {

    void addFrom(Expr<?>... o);
    
    void addGroupBy(Expr<?>... o);

    void addHaving(EBoolean... o);

//    void addJoin(JoinExpression joinExpression);

    void addJoin(JoinType joinType, Expr<?> expr);

    void addJoinCondition(EBoolean o);

    void addOrderBy(OrderSpecifier<?>... o);

    void addProjection(Expr<?>... o);

    void addWhere(EBoolean... o);

    List<? extends Expr<?>> getGroupBy();

    @Nullable
    EBoolean getHaving();

    List<JoinExpression> getJoins();

    @Nullable
    QueryModifiers getModifiers();

    List<OrderSpecifier<?>> getOrderBy();

    List<? extends Expr<?>> getProjection();

    @Nullable
    EBoolean getWhere();
    
    boolean isDistinct();
    
    boolean isUnique();
    
    void setDistinct(boolean distinct);
    
    void setLimit(@Nullable Long limit);

    void setModifiers(QueryModifiers restriction);
    
    void setOffset(@Nullable Long offset);
    
    void setUnique(boolean unique);
}