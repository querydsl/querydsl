/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.functions;

import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * Grammar provides the factory methods for the fluent grammar.
 * 
 * @author tiwe
 * @version $Id$
 */
@Deprecated
public final class AggregationFunctions {
    
    private AggregationFunctions(){}

    @Deprecated
    public static ENumber<Long> count() {
        return Expr.countAll();
    }
    
}
