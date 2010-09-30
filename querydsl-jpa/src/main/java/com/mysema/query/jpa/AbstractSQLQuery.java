/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;

/**
 * Abstract super class for SQLQuery implementation for JPA and Hibernate
 * 
 * @author tiwe
 *
 * @param <T>
 */
public abstract class AbstractSQLQuery<T extends AbstractSQLQuery<T>> extends ProjectableQuery<T>{

    private static final Expression<Integer> COUNT_ALL_AGG_EXPR = OperationImpl.create(Integer.class, Ops.AggOps.COUNT_ALL_AGG);

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata) {
        super(new QueryMixin<T>(metadata));
        this.queryMixin.setSelf((T)this);
    }

    @Override
    public long count() {
        return uniqueResult(COUNT_ALL_AGG_EXPR);
    }

    public T from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    public <E> T fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T fullJoin(RelationalPath<?> o) {
        return queryMixin.fullJoin(o);
    }

    public T fullJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.fullJoin(o, alias);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public <E> T innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T innerJoin(RelationalPath<?> o) {
        return queryMixin.innerJoin(o);
    }

    public T innerJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.innerJoin(o, alias);
    }

    public <E> T join(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T join(RelationalPath<?> o) {
        return queryMixin.join(o);
    }

    public T join(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.join(o, alias);
    }

    public <E> T leftJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T leftJoin(RelationalPath<?> o) {
        return queryMixin.leftJoin(o);
    }

    public T leftJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.leftJoin(o, alias);
    }

    public T on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    public <E> T rightJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    public T rightJoin(RelationalPath<?> o) {
        return queryMixin.rightJoin(o);
    }

    public T rightJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.rightJoin(o, alias);
    }

}
