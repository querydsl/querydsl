/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;

/**
 * Query provides a the query interface of the fluent query DSL
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... o);
    A from(Expr.Entity<?>... o);  
    @Deprecated A innerJoin(Expr.Entity<?> o);
    @Deprecated A join(Expr.Entity<?> o);
    @Deprecated A fullJoin(Expr.Entity<?> o);
    @Deprecated A leftJoin(Expr.Entity<?> o);
    @Deprecated A with(Expr.Boolean... o);
    A where(Expr.Boolean... o);
    A groupBy(Expr<?>... o);
    A having(Expr.Boolean... o);
    A orderBy(OrderSpecifier<?>... o);
}