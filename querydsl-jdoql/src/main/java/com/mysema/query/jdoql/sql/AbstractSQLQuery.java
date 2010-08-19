/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdoql.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.ONumber;

/**
 * Base class for JDO based SQLQuery implementations
 * 
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

    public T from(Expr<?>... args) {
        return queryMixin.from(args);
    }

    public <E> T fullJoin(ForeignKey<E> key, EntityPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T fullJoin(EntityPath<?> o) {
        return queryMixin.fullJoin(o);
    }

    public T fullJoin(SubQuery<?> o, Path<?> alias) {
        return queryMixin.fullJoin(o, alias);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public <E> T innerJoin(ForeignKey<E> key, EntityPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T innerJoin(EntityPath<?> o) {
        return queryMixin.innerJoin(o);
    }

    public T innerJoin(SubQuery<?> o, Path<?> alias) {
        return queryMixin.innerJoin(o, alias);
    }

    public <E> T join(ForeignKey<E> key, EntityPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T join(EntityPath<?> o) {
        return queryMixin.join(o);
    }

    public T join(SubQuery<?> o, Path<?> alias) {
        return queryMixin.join(o, alias);
    }

    public <E> T leftJoin(ForeignKey<E> key, EntityPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T leftJoin(EntityPath<?> o) {
        return queryMixin.leftJoin(o);
    }

    public T leftJoin(SubQuery<?> o, Path<?> alias) {
        return queryMixin.leftJoin(o, alias);
    }

    public T on(EBoolean... conditions) {
        return queryMixin.on(conditions);
    }

    public <E> T rightJoin(ForeignKey<E> key, EntityPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T rightJoin(EntityPath<?> o) {
        return queryMixin.rightJoin(o);
    }

    public T rightJoin(SubQuery<?> o, Path<?> alias) {
        return queryMixin.rightJoin(o, alias);
    }

}
