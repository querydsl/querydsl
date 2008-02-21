/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.BooleanExpr;
import com.mysema.query.grammar.Types.EntityExpr;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.OrderSpecifier;

/**
 * Query provides a fluent query interface, operations can be constructed via the 
 * static methods of Grammar
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<?>>{
    A select(Expr<?>... objects);
    A from(EntityExpr<?>... objects);    
    A where(BooleanExpr... objects);
    A groupBy(Expr<?>... objects);
    A orderBy(OrderSpecifier<?>... objects);
}