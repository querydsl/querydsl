/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.query.Detachable;
import com.mysema.query.QueryBase;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class QueryBaseWithDetach <SubType extends QueryBaseWithDetach<SubType>> extends QueryBase<SubType> implements Detachable {

    public QueryBaseWithDetach(QueryMetadata metadata) {
        super(metadata);
    }
    
    @Override
    public ObjectSubQuery<Long> count(){
        addToProjection(Ops.AggOps.COUNT_ALL_AGG_EXPR);
        return new ObjectSubQuery<Long>(getMetadata(), Long.class);
    }
    
    public EBoolean exists(){
        if (getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No sources given");
        }
        return unique(getMetadata().getJoins().get(0).getTarget()).exists();
    }
    
    @Override
    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        return new ListSubQuery<Object[]>(getMetadata(), Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        addToProjection(projection);
        return new ListSubQuery<RT>(getMetadata(), (Class)projection.getType());
    }

    public EBoolean notExists(){        
        return exists().not();
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        addToProjection(first, second);
        addToProjection(rest);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<Object[]>(getMetadata(),Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        addToProjection(projection);
        getMetadata().setUnique(true);
        return new ObjectSubQuery<RT>(getMetadata(), (Class)projection.getType());
    }

}
