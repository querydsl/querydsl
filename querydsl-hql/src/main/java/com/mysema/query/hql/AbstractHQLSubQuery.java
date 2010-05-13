/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;

/**
 * Abstract superclass for SubQuery implementations
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public class AbstractHQLSubQuery<Q extends AbstractHQLSubQuery<Q>> extends DetachableQuery<Q>{

    private final HQLQueryMixin<Q> queryMixin;
    
    public AbstractHQLSubQuery() {
        this(new DefaultQueryMetadata());
    }
    
    @SuppressWarnings("unchecked")
    public AbstractHQLSubQuery(QueryMetadata metadata) {
        super(new HQLQueryMixin<Q>(metadata));
        super.queryMixin.setSelf((Q)this);
        this.queryMixin = (HQLQueryMixin<Q>) super.queryMixin;
    }

    public Q from(PEntity<?>... o) {
        return queryMixin.from(o);
    }

    public <P> Q fullJoin(Path<? extends Collection<P>> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> Q fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q fullJoin(PEntity<P> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> Q fullJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> Q fullJoin(PMap<?,P,?> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> Q fullJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> Q innerJoin(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> Q innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> Q innerJoin(PEntity<P> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> Q innerJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> Q innerJoin(PMap<?,P,?> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> Q innerJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(Path<? extends Collection<P>> target) {
        return queryMixin.join(target);
    }
    
    public <P> Q join(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }
    
    public <P> Q join(PEntity<P> target) {
        return queryMixin.join(target);
    }
    
    public <P> Q join(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.join(target, alias);
    }
    
    public <P> Q join(PMap<?,P,?> target) {
        return queryMixin.join(target);
    }
    
    public <P> Q join(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q leftJoin(Path<? extends Collection<P>> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> Q leftJoin(PEntity<P> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> Q leftJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> Q leftJoin(PMap<?,P,?> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> Q leftJoin(PMap<?,P,?> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public Q with(EBoolean... conditions){
        return queryMixin.with(conditions); 
    }
    
    public String toString(){
        if (!queryMixin.getMetadata().getJoins().isEmpty()){
            HQLSerializer serializer = new HQLSerializer(HQLTemplates.DEFAULT);
            serializer.serialize(queryMixin.getMetadata(), false, null);
            return serializer.toString().trim();    
        }else{
            return super.toString();
        }        
    }
    
}
