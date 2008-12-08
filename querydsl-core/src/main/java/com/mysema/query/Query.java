/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.Entity;

/**
 * Query provides a the query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A from(Entity<?>... o);  
    A innerJoin(Entity<?> o);
    A join(Entity<?> o);
    A fullJoin(Entity<?> o);
    A leftJoin(Entity<?> o);
    A with(Expr.Boolean o);
    A where(Expr.Boolean o);
    A groupBy(Expr<?>... o);
    A having(Expr.Boolean o);
    A orderBy(OrderSpecifier<?>... o);
}