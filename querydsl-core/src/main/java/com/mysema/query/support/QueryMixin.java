/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;

/**
 * Mixin style Query implementation
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class QueryMixin<T>{
    
    private T self;
    
    private final QueryMetadata metadata;
    
    public QueryMixin(){
        this.metadata = new DefaultQueryMetadata();
    }
    
    public QueryMixin(QueryMetadata metadata){
        this.metadata = Assert.notNull(metadata,"metadata");
    }
        
    public QueryMixin(T self){
        this(self, new DefaultQueryMetadata());
    }
    
    public QueryMixin(T self, QueryMetadata metadata){
        this.self = Assert.notNull(self,"self");
        this.metadata = Assert.notNull(metadata,"metadata");
    }
    
    public T addToProjection(Expr<?>... o) {
        metadata.addProjection(o);
        return self;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }
    
    public T from(Expr<?>... args) {        
        metadata.addFrom(args);
        return self;
    }

    public T groupBy(Expr<?>... o) {
        metadata.addGroupBy(o);
        return self;
    }

    public T having(EBoolean... o) {
        metadata.addHaving(o);
        return self;
    }

    public T orderBy(OrderSpecifier<?>... o) {
        metadata.addOrderBy(o);
        return self;
    }

    public T where(EBoolean... o) {
        metadata.addWhere(o);
        return self;
    }

    public String toString() {
        return metadata.toString();
    }
    
    public T limit(long limit) {
        metadata.setLimit(limit);
        return self;
    }

    public T offset(long offset) {
        metadata.setOffset(offset);
        return self;
    }
    
    public <P> T fullJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }    
    
    public <P> T innerJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }
    
    public <P> T join(PEntity<P> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return self;
    }    

    public <P> T leftJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return self;
    }

    public T restrict(QueryModifiers modifiers) {
        metadata.setModifiers(modifiers);
        return self;
    }
    
    public T on(EBoolean... conditions){
        for (EBoolean condition : conditions){
            metadata.addJoinCondition(condition);    
        }        
        return self;
    }

    public void setUnique(boolean unique) {
        metadata.setUnique(unique);        
    }

    public void setDistinct(boolean distinct) {
        metadata.setDistinct(distinct);        
    }
    
    public boolean isUnique() {
        return metadata.isUnique();
    }

    public boolean isDistinct() {
        return metadata.isDistinct();
    }
    
    public T getSelf(){
        return self;
    }    
    
    public void setSelf(T self){
        this.self = self;
    }
    
}
