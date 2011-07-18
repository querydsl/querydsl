/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.TemplateExpressionImpl;


/**
 * @author tiwe
 *
 */
public final class Wildcard {
    
    public static final Expression<Object[]> all = TemplateExpressionImpl.create(Object[].class, "*");

    public static final NumberExpression<Long> count = NumberOperation.create(Long.class, Ops.AggOps.COUNT_ALL_AGG);

    public static final NumberExpression<Integer> countAsInt = NumberOperation.create(Integer.class, Ops.AggOps.COUNT_ALL_AGG);

    private Wildcard(){}
    
}
