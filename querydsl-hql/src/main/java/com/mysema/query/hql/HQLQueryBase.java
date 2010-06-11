/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.Map;

import com.mysema.query.QueryMetadata;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PMap;

/**
 * HQLQueryBase is a base Query class for HQL
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class HQLQueryBase<Q extends HQLQueryBase<Q>> extends ProjectableQuery<Q> {

    private Map<Object,String> constants;

    private final HQLQueryMixin<Q> queryMixin;
    
    private final JPQLTemplates templates;
    
    protected JPQLTemplates getTemplates(){
        return templates;
    }
    
    protected HQLQueryMixin<Q> getQueryMixin(){
        return queryMixin;
    }
    
    @SuppressWarnings("unchecked")
    public HQLQueryBase(QueryMetadata md, JPQLTemplates templates) {
        super(new HQLQueryMixin<Q>(md));
        super.queryMixin.setSelf((Q) this);
        this.queryMixin = (HQLQueryMixin) super.queryMixin;
        this.templates = templates;
    }

    protected String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }
    
    protected void reset() {
        queryMixin.getMetadata().reset();
    }
    
    public Q fetch(){
        return queryMixin.fetch();
    }

    public Q fetchAll(){
        return queryMixin.fetchAll();
    }

    public Q from(PEntity<?>... args) {        
        return queryMixin.from(args);
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
    
    protected Map<Object,String> getConstants() {
        return constants;
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
        return queryMixin.innerJoin(target);
    }
    
    public <P> Q join(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }
    
    public <P> Q join(PEntity<P> target) {
        return queryMixin.innerJoin(target);
    }
    
    public <P> Q join(PEntity<P> target, PEntity<P> alias) {
        return queryMixin.innerJoin(target, alias);
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
    
    protected void setConstants(Map<Object, String> constants) {
        this.constants = constants;
    }
    
    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString(){
        return buildQueryString(false);
    }
    
    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

}
