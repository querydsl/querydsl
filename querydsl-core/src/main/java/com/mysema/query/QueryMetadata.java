/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.List;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * QueryMetadata provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface QueryMetadata<JoinMeta> {

    List<? extends Expr<?>> getGroupBy();

    EBoolean getHaving();

    List<JoinExpression<JoinMeta>> getJoins();

    List<OrderSpecifier<?>> getOrderBy();

    List<? extends Expr<?>> getSelect();

    EBoolean getWhere();

}