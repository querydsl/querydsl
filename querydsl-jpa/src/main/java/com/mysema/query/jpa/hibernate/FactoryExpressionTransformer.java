/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate;

import java.util.List;

import org.hibernate.transform.ResultTransformer;

import com.mysema.query.types.FactoryExpression;

/**
 * FactoryExpressionTransformer is a ResultTransformer implementation using the EConstructor for transformation
 * 
 * @author tiwe
 *
 */
public final class FactoryExpressionTransformer implements ResultTransformer{

    private static final long serialVersionUID = -3625957233853100239L;

    private final transient FactoryExpression<?> constructor;

    public FactoryExpressionTransformer(FactoryExpression<?> constructor){
        this.constructor = constructor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List transformList(List collection) {
        return collection;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        return constructor.newInstance(tuple);
    }

}
