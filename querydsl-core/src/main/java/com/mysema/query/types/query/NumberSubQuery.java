/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQueryExpressionImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.NumberExpression;

/**
 * Number typed single result subquery
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public final class NumberSubQuery<T extends Number & Comparable<?>> extends NumberExpression<T> implements ExtendedSubQueryExpression<T>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryExpressionImpl<T> subQueryMixin;

    @Nullable
    private volatile BooleanExpression exists;

    public NumberSubQuery(Class<T> type, QueryMetadata md) {
        super(type);
        subQueryMixin = new SubQueryExpressionImpl<T>(type, md);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
       return subQueryMixin.equals(o);
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null){
            exists = BooleanOperation.create(Ops.EXISTS, this);
        }
        return exists;
    }
    
    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public int hashCode(){
        return subQueryMixin.hashCode();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

}
