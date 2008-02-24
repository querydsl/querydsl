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
 * Query provides a fluent query interface, operations can be constructed via the 
 * static methods of Grammar
 *
 * @author tiwe
 * @version $Id$
 */
public interface Query<A extends Query<A>>{
    A select(Expr<?>... objects);
    A from(ExprEntity<?>... objects);    
    A where(ExprBoolean... objects);
    A groupBy(Expr<?>... objects);
    A having(ExprBoolean... objects);
    A orderBy(OrderSpecifier<?>... objects);
}