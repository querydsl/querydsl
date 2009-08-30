/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.annotation.Nonnegative;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * Query provides a query interface of the fluent query DSL.
 * 
 * <p>Note that the from method has been left out, since there are implementation
 * specific variants of it.</p>
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Query<SubType extends Query<SubType>> {
    
    SubType where(EBoolean... o);

    SubType groupBy(Expr<?>... o);

    SubType having(EBoolean... o);

    SubType orderBy(OrderSpecifier<?>... o);
    
    SubType limit(@Nonnegative long limit);
    
    SubType offset(@Nonnegative long offset);
        
    SubType restrict(QueryModifiers mod);
       
}