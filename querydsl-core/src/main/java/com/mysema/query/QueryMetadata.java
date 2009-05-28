/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.List;

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
public interface QueryMetadata<JoinMeta> {

    void addJoin(JoinExpression<JoinMeta> joinExpression);

    void addJoin(JoinType joinType, Expr<?> expr);

    void addJoinCondition(EBoolean o);

    void addFrom(Expr<?>... o);

    void addGroupBy(Expr<?>... o);

    void addHaving(EBoolean... o);

    void addOrderBy(OrderSpecifier<?>... o);

    void addProjection(Expr<?>... o);

    void addWhere(EBoolean... o);

    List<? extends Expr<?>> getGroupBy();

    EBoolean getHaving();

    List<JoinExpression<JoinMeta>> getJoins();

    List<OrderSpecifier<?>> getOrderBy();

    List<? extends Expr<?>> getProjection();

    EBoolean getWhere();

    boolean isDistinct();

    void setDistinct(boolean distinct);

    QueryModifiers getModifiers();

    void setModifiers(QueryModifiers restriction);
}