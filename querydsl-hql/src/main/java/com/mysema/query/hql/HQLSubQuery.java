/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.support.QueryBaseWithDetach;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.Path;

/**
 * HQLSubQuery is a subquery builder class for HQL/JPAQL
 * 
 * @author tiwe
 *
 */
public class HQLSubQuery extends QueryBaseWithDetach<HQLSubQuery>{

    private final HQLQueryMixin<HQLSubQuery> queryMixin;
    
    public HQLSubQuery() {
        super(new HQLQueryMixin<HQLSubQuery>(new DefaultQueryMetadata()));
        super.queryMixin.setSelf(this);
        this.queryMixin = (HQLQueryMixin<HQLSubQuery>) super.queryMixin;
    }

    public HQLSubQuery from(PEntity<?>... o) {
        return queryMixin.from(o);
    }

    public <P> HQLSubQuery fullJoin(Path<? extends Collection<P>> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> HQLSubQuery fullJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> HQLSubQuery fullJoin(PEntity<P> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> HQLSubQuery fullJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> HQLSubQuery fullJoin(PMap<?,P,?> target) {
        return queryMixin.fullJoin(target);
    }
    
    public <P> HQLSubQuery fullJoin(PMap<?,P,?> target, PEntity<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }
    
    public <P> HQLSubQuery innerJoin(Path<? extends Collection<P>> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> HQLSubQuery innerJoin(Path<? extends Collection<P>>target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> HQLSubQuery innerJoin(PEntity<P> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> HQLSubQuery innerJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> HQLSubQuery innerJoin(PMap<?,P,?> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> HQLSubQuery innerJoin(PMap<?,P,?> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> HQLSubQuery join(Path<? extends Collection<P>> target) {
        return queryMixin.join(target);
    }
    
    public <P> HQLSubQuery join(Path<? extends Collection<P>> target, PEntity<P> alias) {
        return queryMixin.join(target, alias);
    }
    
    public <P> HQLSubQuery join(PEntity<P> target) {
        return queryMixin.join(target);
    }
    
    public <P> HQLSubQuery join(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.join(target, alias);
    }
    
    public <P> HQLSubQuery join(PMap<?,P,?> target) {
        return queryMixin.join(target);
    }
    
    public <P> HQLSubQuery join(PMap<?,P,?> target, PEntity<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> HQLSubQuery leftJoin(Path<? extends Collection<P>> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> HQLSubQuery leftJoin(Path<? extends Collection<P>> target, PEntity<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> HQLSubQuery leftJoin(PEntity<P> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> HQLSubQuery leftJoin(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }
    
    public <P> HQLSubQuery leftJoin(PMap<?,P,?> target) {
        return queryMixin.leftJoin(target);
    }
    
    public <P> HQLSubQuery leftJoin(PMap<?,P,?> target, PEntity<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public HQLSubQuery with(EBoolean... conditions){
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
