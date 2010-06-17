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
import com.mysema.query.types.Expr;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * Mixin style Query implementation
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class QueryMixin<T>{
    
    private final QueryMetadata metadata;
    
    private T self;
    
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

    public T from(Expr<?>... args) {
        for (Expr<?> arg : args){
            metadata.addJoin(JoinType.DEFAULT, arg);
        }
        return self;
    }
    
    public <P> T fullJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }

    @SuppressWarnings("unchecked")
    public <P> T fullJoin(SubQuery<P> target, Path alias) {
        metadata.addJoin(JoinType.FULLJOIN, target.asExpr().as(alias));
        return self;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }

    public T getSelf(){
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
    
    public <P> T innerJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }

    @SuppressWarnings("unchecked")
    public <P> T innerJoin(SubQuery<P> target, Path alias) {
        metadata.addJoin(JoinType.INNERJOIN, target.asExpr().as(alias));
        return self;
    }
    
    public boolean isDistinct() {
        return metadata.isDistinct();
    }    
    
    public boolean isUnique() {
        return metadata.isUnique();
    }
    
    public <P> T join(PEntity<P> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return self;
    }    

    @SuppressWarnings("unchecked")
    public <P> T join(SubQuery<P> target, Path alias) {
        metadata.addJoin(JoinType.JOIN, target.asExpr().as(alias));
        return self;
    }
    
    public <P> T leftJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return self;
    }    
    
    @SuppressWarnings("unchecked")
    public <P> T leftJoin(SubQuery<P> target, Path alias) {
        metadata.addJoin(JoinType.LEFTJOIN, target.asExpr().as(alias));
        return self;
    }
    
    public T limit(long limit) {
        metadata.setLimit(limit);
        return self;
    }    

    public T offset(long offset) {
        metadata.setOffset(offset);
        return self;
    }
    
    public T on(EBoolean... conditions){
        for (EBoolean condition : conditions){
            metadata.addJoinCondition(condition);    
        }        
        return self;
    }
    
    public T orderBy(OrderSpecifier<?>... o) {
        metadata.addOrderBy(o);
        return self;
    }

    public T restrict(QueryModifiers modifiers) {
        metadata.setModifiers(modifiers);
        return self;
    }
    
    public <P> T rightJoin(PEntity<P> target) {
        metadata.addJoin(JoinType.RIGHTJOIN, target);
        return self;
    }

    @SuppressWarnings("unchecked")
    public <P> T rightJoin(SubQuery<P> target, Path alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, target.asExpr().as(alias));
        return self;
    }
    
    public <P> T set(Param<P> param, P value){
        metadata.setParam(param, value);
        return self;
    }

    public void setDistinct(boolean distinct) {
        metadata.setDistinct(distinct);        
    }
    
    public void setSelf(T self){
        this.self = self;
    }

    public void setUnique(boolean unique) {
        metadata.setUnique(unique);        
    }
    
    public String toString() {
        return metadata.toString();
    }    
    
    public T where(EBoolean... o) {
        metadata.addWhere(o);
        return self;
    }

    
}
