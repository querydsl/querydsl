/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.query.Detachable;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Grammar;
import com.mysema.query.types.ListSubQuery;
import com.mysema.query.types.ObjectSubQuery;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <JoinMeta>
 * @param <SubType>
 */
public abstract class QueryBaseWithProjectionAndDetach <JoinMeta, 
    SubType extends QueryBaseWithProjectionAndDetach<JoinMeta, SubType>>
    extends QueryBaseWithProjection<JoinMeta, SubType> implements Detachable<JoinMeta> {
    
    public QueryBaseWithProjectionAndDetach() {
    }

    public QueryBaseWithProjectionAndDetach(QueryMetadata<JoinMeta> metadata) {
        super(metadata);
    }
    
    @Override
    public ObjectSubQuery<JoinMeta,Long> countExpr(){
        addToProjection(Grammar.count());
        return new ObjectSubQuery<JoinMeta,Long>(getMetadata(), Long.class);
    }

    @Override
    public ListSubQuery<JoinMeta, Object[]> listExpr(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        return new ListSubQuery<JoinMeta,Object[]>(getMetadata(), Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<JoinMeta, RT> listExpr(Expr<RT> projection) {
        addToProjection(projection);
        return new ListSubQuery<JoinMeta,RT>(getMetadata(), (Class)projection.getType());
    }

    @Override
    public ObjectSubQuery<JoinMeta, Object[]> uniqueExpr(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<JoinMeta,Object[]>(getMetadata(),Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<JoinMeta, RT> uniqueExpr(Expr<RT> projection) {
        addToProjection(projection);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<JoinMeta,RT>(getMetadata(), (Class)projection.getType());
    }

}
