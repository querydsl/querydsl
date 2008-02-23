/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.BooleanExpr;
import com.mysema.query.grammar.Types.EntityExpr;

/**
 * ExtQuery externds the Query interface to provide innerJoin, leftJoin and with methods
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExtQuery<A extends ExtQuery<A>> extends Query<A> {
    A innerJoin(EntityExpr<?> object);
    A join(EntityExpr<?> object);
    A leftJoin(EntityExpr<?> object); 
    A with(BooleanExpr... objects);
}
