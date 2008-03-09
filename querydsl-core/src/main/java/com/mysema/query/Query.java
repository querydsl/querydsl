/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.OrderSpecifier;

/**
 * Query provides a the query interface of the fluent query DSL
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... o);
    A from(ExprEntity<?>... o);  
    A innerJoin(ExprEntity<?> o);
    A join(ExprEntity<?> o);
    A leftJoin(ExprEntity<?> o);
    A with(ExprBoolean... o);
    A where(ExprBoolean... o);
    A groupBy(Expr<?>... o);
    A having(ExprBoolean... o);
    A orderBy(OrderSpecifier<?>... o);
}