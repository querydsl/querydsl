/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.SubQueryImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.NumberExpression;

/**
 * Single result subquery
 *
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
public final class NumberSubQuery<A extends Number & Comparable<?>> extends NumberExpression<A> implements SubQueryExpression<A>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryImpl<A> subQueryMixin;

    public NumberSubQuery(Class<A> type, QueryMetadata md) {
        super(type);
        subQueryMixin = new SubQueryImpl<A>(type, md);
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
    public Predicate exists() {
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
    public Predicate notExists() {
        return subQueryMixin.notExists();
    }

}
