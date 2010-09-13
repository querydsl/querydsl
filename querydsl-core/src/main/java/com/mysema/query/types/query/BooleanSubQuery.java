/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Single result subquery
 *
 * @author tiwe
 */
public final class BooleanSubQuery extends BooleanExpression implements SubQueryExpression<Boolean>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryMixin<Boolean> subQueryMixin;

    public BooleanSubQuery(QueryMetadata md) {
        subQueryMixin = new SubQueryMixin<Boolean>(this, md);
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
        return subQueryMixin.exists();
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
        return subQueryMixin.notExists();
    }

}
