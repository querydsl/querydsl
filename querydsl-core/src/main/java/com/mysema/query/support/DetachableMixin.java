/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.ONumber;
import com.mysema.query.types.query.BooleanSubQuery;
import com.mysema.query.types.query.ComparableSubQuery;
import com.mysema.query.types.query.DateSubQuery;
import com.mysema.query.types.query.DateTimeSubQuery;
import com.mysema.query.types.query.Detachable;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;
import com.mysema.query.types.query.StringSubQuery;
import com.mysema.query.types.query.TimeSubQuery;

/**
 * Mixin style implementation of the Detachable interface
 *
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable{

    private static final ENumber<Long> COUNT_ALL_AGG_EXPR = ONumber.create(Long.class, Ops.AggOps.COUNT_ALL_AGG);

    private final QueryMixin<?> queryMixin;

    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = Assert.notNull(queryMixin,"queryMixin");
    }

    @Override
    public ObjectSubQuery<Long> count() {
        queryMixin.addToProjection(COUNT_ALL_AGG_EXPR);
        return new ObjectSubQuery<Long>(Long.class, queryMixin.getMetadata());
    }

    @Override
    public EBoolean exists(){
        if (queryMixin.getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No sources given");
        }
        return unique(queryMixin.getMetadata().getJoins().get(0).getTarget()).exists();
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.addToProjection(first, second);
        queryMixin.addToProjection(rest);
        return new ListSubQuery<Object[]>(Object[].class, queryMixin.getMetadata());
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        return new ListSubQuery<Object[]>(Object[].class, queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        queryMixin.addToProjection(projection);
        return new ListSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @Override
    public EBoolean notExists(){
        return exists().not();
    }

    private void setUniqueProjection(Expr<?> projection){
        queryMixin.addToProjection(projection);
        queryMixin.setUnique(true);
    }

    @Override
    public BooleanSubQuery unique(EBoolean projection) {
        setUniqueProjection(projection);
        return new BooleanSubQuery(queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(EComparable<RT> projection) {
        setUniqueProjection(projection);
        return new ComparableSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateSubQuery<RT> unique(EDate<RT> projection) {
        setUniqueProjection(projection);
        return new DateSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(EDateTime<RT> projection) {
        setUniqueProjection(projection);
        return new DateTimeSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(ENumber<RT> projection) {
        setUniqueProjection(projection);
        return new NumberSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @Override
    public StringSubQuery unique(EString projection) {
        setUniqueProjection(projection);
        return new StringSubQuery(queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(ETime<RT> projection) {
        setUniqueProjection(projection);
        return new TimeSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.addToProjection(first, second);
        queryMixin.addToProjection(rest);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<Object[]>(Object[].class, queryMixin.getMetadata());
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<Object[]>(Object[].class, queryMixin.getMetadata());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        setUniqueProjection(projection);
        return new ObjectSubQuery<RT>((Class)projection.getType(), queryMixin.getMetadata());
    }
}
