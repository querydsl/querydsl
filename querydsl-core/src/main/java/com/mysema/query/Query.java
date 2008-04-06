/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;

import static com.mysema.query.grammar.types.Expr.*;

/**
 * Query provides a the query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... o);
    A from(Entity<?>... o);  
    @Deprecated A innerJoin(Entity<?> o);
    @Deprecated A join(Entity<?> o);
    @Deprecated A fullJoin(Entity<?> o);
    @Deprecated A leftJoin(Entity<?> o);
    @Deprecated A with(Expr.Boolean o);
    A where(Expr.Boolean o);
    A groupBy(Expr<?>... o);
    A having(Expr.Boolean o);
    A orderBy(OrderSpecifier<?>... o);
}