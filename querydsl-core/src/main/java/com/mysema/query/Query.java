/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.grammar.Types.ExprBoolean;
import com.mysema.query.grammar.Types.ExprEntity;
import com.mysema.query.grammar.Types.Expr;
import com.mysema.query.grammar.Types.OrderSpecifier;

/**
 * Query provides a the query interface of the fluent query DSL
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... o);
    A from(ExprEntity<?>... o);  
    @Deprecated A innerJoin(ExprEntity<?> o);
    @Deprecated A join(ExprEntity<?> o);
    @Deprecated A leftJoin(ExprEntity<?> o);
    @Deprecated A with(ExprBoolean... o);
    A where(ExprBoolean... o);
    A groupBy(Expr<?>... o);
    A having(ExprBoolean... o);
    A orderBy(OrderSpecifier<?>... o);
}