/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;


/**
 * @author tiwe
 *
 */
public final class Wildcard {

    public static final Expression<Object[]> all = TemplateExpressionImpl.create(Object[].class, "*");
    
    public static final Expression<Long> count = OperationImpl.create(Long.class, Ops.AggOps.COUNT_ALL_AGG);

}
