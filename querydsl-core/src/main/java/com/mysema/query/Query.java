/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.OrderSpecifier;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EEntity;

/**
 * Query provides a the query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A from(EEntity<?>... o);  
    A innerJoin(EEntity<?> o);
    A join(EEntity<?> o);
    A fullJoin(EEntity<?> o);
    A leftJoin(EEntity<?> o);
    A with(Expr.EBoolean o);
    A where(Expr.EBoolean o);
    A groupBy(Expr<?>... o);
    A having(Expr.EBoolean o);
    A orderBy(OrderSpecifier<?>... o);
}