/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.*;

/**
 * ExtQuery provides
 *
 * @author tiwe
 * @version $Id$
 */
public interface ExtQuery<A extends ExtQuery<?>> extends Query<A> {
    A innerJoin(EntityExpr<?>... objects);
    A leftJoin(EntityExpr<?>... objects); 
    A with(BooleanExpr... objects);
}
