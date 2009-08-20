/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.List;

import com.mysema.query.Detachable;
import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.Expr;

/**
 * ColQuery is the default implementation for Collection queries
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends Query<ColQuery>, Projectable, Detachable {

    <A> ColQuery from(Expr<A> entity, A... args);

    <A> ColQuery from(Expr<A> entity, Iterable<? extends A> col);

    <RT> List<RT> list(RT alias);

}
