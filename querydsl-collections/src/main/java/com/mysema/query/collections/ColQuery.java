/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.List;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * ColQuery is the default implementation for Collection queries
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends Projectable {

    <A> ColQuery from(Expr<A> entity, A first, A... rest);

    <A> ColQuery from(Expr<A> entity, Iterable<? extends A> col);

    ColQuery orderBy(OrderSpecifier<?>... o);

    ColQuery where(EBoolean... o);

    <RT> List<RT> list(RT alias);

    ColQuery limit(long limit);

    ColQuery offset(long offset);

    ColQuery restrict(QueryModifiers mod);

}
