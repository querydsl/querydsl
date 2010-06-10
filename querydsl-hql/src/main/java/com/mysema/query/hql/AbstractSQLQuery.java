/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ONumber;
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
    public long count() {
        return uniqueResult(COUNT_ALL_AGG_EXPR);
    }
    
    public T from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public T fullJoin(PEntity<?> o) {
        return queryMixin.fullJoin(o);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public T innerJoin(PEntity<?> o) {
        return queryMixin.innerJoin(o);
    }
    
    public T join(PEntity<?> o) {
        return queryMixin.join(o);
    }
    
    public T leftJoin(PEntity<?> o) {
        return queryMixin.leftJoin(o);
    }
    
    public T on(EBoolean... conditions) {
        return queryMixin.on(conditions);
    }
}
