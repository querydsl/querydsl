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
public interface Query<SubType extends Query<SubType>>{
    SubType from(Expr<?>... o);  
    SubType innerJoin(Expr<?> o);
    SubType join(Expr<?> o);
    SubType fullJoin(Expr<?> o);
    SubType leftJoin(Expr<?> o);
    SubType on(Expr.EBoolean o);
    SubType groupBy(Expr<?>... o);
    SubType having(Expr.EBoolean... o);
    SubType orderBy(OrderSpecifier<?>... o);
}