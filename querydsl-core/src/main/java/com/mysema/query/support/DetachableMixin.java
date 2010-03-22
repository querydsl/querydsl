/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.EComparable;
import com.mysema.query.types.EDate;
import com.mysema.query.types.EDateTime;
import com.mysema.query.types.ENumber;
import com.mysema.query.types.EString;
import com.mysema.query.types.ETime;
import com.mysema.query.types.Expr;
import com.mysema.query.types.operation.Ops;
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

    private final QueryMixin<?> queryMixin;
    
    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = Assert.notNull(queryMixin,"queryMixin");
    }
    
    @Override
    public ObjectSubQuery<Long> count() {
        queryMixin.addToProjection(Ops.AggOps.COUNT_ALL_AGG_EXPR);
        return new ObjectSubQuery<Long>(queryMixin.getMetadata(), Long.class);
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
        return new ListSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        return new ListSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }
    

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        queryMixin.addToProjection(projection);
        return new ListSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
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
        return new ComparableSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateSubQuery<RT> unique(EDate<RT> projection) {
        setUniqueProjection(projection);
        return new DateSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(EDateTime<RT> projection) {
        setUniqueProjection(projection);
        return new DateTimeSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(ENumber<RT> projection) {
        setUniqueProjection(projection);
        return new NumberSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
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
        return new TimeSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        queryMixin.addToProjection(first, second);
        queryMixin.addToProjection(rest);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?>[] args) {
        queryMixin.addToProjection(args);
        queryMixin.setUnique(true);
        return new ObjectSubQuery<Object[]>(queryMixin.getMetadata(), Object[].class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        setUniqueProjection(projection);
        return new ObjectSubQuery<RT>(queryMixin.getMetadata(), (Class)projection.getType());
    }
}
