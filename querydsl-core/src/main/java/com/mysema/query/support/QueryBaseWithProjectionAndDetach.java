/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.query.Detachable;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 * @param <JoinMeta>
 * @param <SubType>
 */
public abstract class QueryBaseWithProjectionAndDetach 
    <SubType extends QueryBaseWithProjectionAndDetach<SubType>>
    extends QueryBaseWithProjection<SubType> implements Detachable {
    
    public QueryBaseWithProjectionAndDetach() {
    }

    public QueryBaseWithProjectionAndDetach(QueryMetadata metadata) {
        super(metadata);
    }
    
    @Override
    public ObjectSubQuery<Long> countExpr(){
        addToProjection(Grammar.count());
        return new ObjectSubQuery<Long>(getMetadata(), Long.class);
    }

    @Override
    public ListSubQuery<Object[]> listExpr(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        return new ListSubQuery<Object[]>(getMetadata(), Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> listExpr(Expr<RT> projection) {
        addToProjection(projection);
        return new ListSubQuery<RT>(getMetadata(), (Class)projection.getType());
    }

    @Override
    public ObjectSubQuery<Object[]> uniqueExpr(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<Object[]>(getMetadata(),Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> uniqueExpr(Expr<RT> projection) {
        addToProjection(projection);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<RT>(getMetadata(), (Class)projection.getType());
    }

}
