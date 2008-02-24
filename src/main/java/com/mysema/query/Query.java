/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprForBoolean;
import com.mysema.query.grammar.Types.ExprForEntity;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.OrderSpecifier;

/**
 * Query provides a the query interface of the fluent query DSL
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... objects);
    A from(ExprForEntity<?>... objects);    
    A where(ExprForBoolean... objects);
    A groupBy(Expr<?>... objects);
    A having(ExprForBoolean... objects);
    A orderBy(OrderSpecifier<?>... objects);
}