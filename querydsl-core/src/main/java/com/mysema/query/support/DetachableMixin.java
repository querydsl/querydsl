/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.query.Detachable;
import com.mysema.query.QueryMixin;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable{

    private final QueryMixin<?> queryMixin;
    
    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = queryMixin;
    }
    
    @Override
    public ObjectSubQuery<Long> count() {
        queryMixin.addToProjection(Ops.AggOps.COUNT_ALL_AGG_EXPR);
        return new ObjectSubQuery<Long>(queryMixin.getMetadata(), Long.class);
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.addToProjection(first, second);
        queryMixin.addToProjection(rest);
        return new ListSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }

    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        queryMixin.addToProjection(projection);
        return new ListSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.addToProjection(first, second);
        queryMixin.addToProjection(rest);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }

    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        queryMixin.addToProjection(projection);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }
    
    @Override
    public EBoolean exists(){
        if (queryMixin.getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No sources given");
        }
        return unique(queryMixin.getMetadata().getJoins().get(0).getTarget()).exists();
    }
    
    @Override
    public EBoolean notExists(){        
        return exists().not();
    }
}
