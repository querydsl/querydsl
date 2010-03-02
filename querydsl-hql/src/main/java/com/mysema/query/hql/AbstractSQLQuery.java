/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Iterator;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractSQLQuery<T extends AbstractSQLQuery<T>> extends ProjectableQuery<T>{
    
    private static final ENumber<Integer> COUNT_ALL_AGG_EXPR = ONumber.create(Integer.class, Ops.AggOps.COUNT_ALL_AGG);
    
    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata) {
        super(new QueryMixin<T>(metadata));
        this.queryMixin.setSelf((T)this);
    }

    @Override
    public final long count() {
        return uniqueResult(COUNT_ALL_AGG_EXPR);
    }
    
    public final T from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public final T fullJoin(PEntity<?> o) {
        return queryMixin.fullJoin(o);
    }

    public final QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public final T innerJoin(PEntity<?> o) {
        return queryMixin.innerJoin(o);
    }
    
    @Override
    public final Iterator<Object[]> iterate(Expr<?>[] args) {
        return list(args).iterator();
    }

    @Override
    public final <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return list(projection).iterator();
    }

    public final T join(PEntity<?> o) {
        return queryMixin.join(o);
    }
    
    public final T leftJoin(PEntity<?> o) {
        return queryMixin.leftJoin(o);
    }
    
    public final T on(EBoolean... conditions) {
        return queryMixin.on(conditions);
    }
}
