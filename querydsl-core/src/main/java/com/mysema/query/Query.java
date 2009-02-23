/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;

/**
 * Query provides a the query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A from(Expr<?>... o);  
    A innerJoin(Expr<?> o);
    A join(Expr<?> o);
    A fullJoin(Expr<?> o);
    A leftJoin(Expr<?> o);
    A on(Expr.EBoolean o);
    A groupBy(Expr<?>... o);
    A having(Expr.EBoolean... o);
    A orderBy(OrderSpecifier<?>... o);
}