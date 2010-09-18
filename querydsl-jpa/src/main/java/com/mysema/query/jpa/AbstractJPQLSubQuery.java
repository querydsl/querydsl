/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.MapPath;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q>
 */
public class AbstractJPQLSubQuery<Q extends AbstractJPQLSubQuery<Q>> extends DetachableQuery<Q>{

    private final JPQLQueryMixin<Q> queryMixin;

    public AbstractJPQLSubQuery() {
        this(new DefaultQueryMetadata());
    }

    @SuppressWarnings("unchecked")
    public AbstractJPQLSubQuery(QueryMetadata metadata) {
        super(new JPQLQueryMixin<Q>(metadata));
        super.queryMixin.setSelf((Q)this);
        this.queryMixin = (JPQLQueryMixin<Q>) super.queryMixin;
    }

    public Q from(EntityPath<?>... o) {
        return queryMixin.from(o);
    }

    public <P> Q fullJoin(Path<? extends Collection<P>> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q fullJoin(EntityPath<P> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q fullJoin(MapPath<?,P,?> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q innerJoin(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(MapPath<?,P,?> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(Path<? extends Collection<P>> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q join(EntityPath<P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q join(MapPath<?,P,?> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q leftJoin(Path<? extends Collection<P>> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(EntityPath<P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(MapPath<?,P,?> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(MapPath<?,P,?> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public Q with(BooleanExpression... conditions){
        return queryMixin.with(conditions);
    }
    
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
            serializer.serialize(queryMixin.getMetadata(), false, null);
            return serializer.toString().trim();
        }else{
            return super.toString();
        }
    }

}
