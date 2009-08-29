/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.Detachable;
import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.Expr;

/**
 * Query interface for Collection queries
 * 
 * @author tiwe
 * @version $Id$
 */
public interface ColQuery extends Query<ColQuery>, Projectable, Detachable {

    <A> ColQuery from(Expr<A> entity, Iterable<? extends A> col);

}
