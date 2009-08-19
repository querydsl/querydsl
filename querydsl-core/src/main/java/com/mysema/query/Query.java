/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Query provides a query interface of the fluent query DSL.
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<SubType extends Query<SubType>> {

    SubType from(PEntity<?>... sources);

    SubType where(EBoolean... o);

    SubType groupBy(Expr<?>... o);

    SubType having(EBoolean... o);

    SubType orderBy(OrderSpecifier<?>... o);
    
    SubType limit(long limit);
    
    SubType offset(long offset);
        
    SubType restrict(QueryModifiers mod);
       
}