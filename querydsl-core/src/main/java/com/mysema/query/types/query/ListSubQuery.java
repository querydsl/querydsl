/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import java.util.List;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.SubQueryImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.CollectionExpressionBase;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * List result subquery
 *
 * @author tiwe
 *
 * @param <A>
 */
public final class ListSubQuery<A> extends CollectionExpressionBase<List<A>,A> implements SubQueryExpression<List<A>>{

    private static final long serialVersionUID = 3399354334765602960L;

    private final Class<A> elementType;

    private final SubQueryImpl<List<A>> subQueryMixin;

    @SuppressWarnings("unchecked")
    public ListSubQuery(Class<A> elementType, QueryMetadata md) {
        super((Class)List.class);
        this.elementType = elementType;
        this.subQueryMixin = new SubQueryImpl<List<A>>((Class)List.class, md);
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

    public Class<A> getElementType() {
        return elementType;
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

    @SuppressWarnings("unchecked")
    public Expression<?> as(Expression<?> alias) {
        return SimpleOperation.create(alias.getType(),(Operator)Ops.ALIAS, this, alias);
    }

}
