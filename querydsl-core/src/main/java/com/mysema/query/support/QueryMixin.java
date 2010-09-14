/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.Collection;

import com.mysema.commons.lang.Assert;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Ops;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Param;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.SimpleOperation;
import com.mysema.query.types.path.MapPath;

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

    public T addFlag(QueryFlag queryFlag){
        metadata.addFlag(queryFlag);
        return self;
    }
    
    public T addToProjection(Expression<?>... o) {
        metadata.addProjection(o);
        return self;
    }

    private <P extends Path<?>> P assertRoot(P p){
        if (!p.getRoot().equals(p)){
            throw new IllegalArgumentException(p + " is not a root path");
        }
        return p;
    }

    protected <D> Expression<D> createAlias(Expression<D> path, Path<D> alias){
        assertRoot(alias);
        return ExpressionUtils.as(path, alias);
    }

    @SuppressWarnings("unchecked")
    protected <D> Expression<D> createAlias(Path<? extends Collection<D>> target, Path<D> alias){
        assertRoot(alias);
        return SimpleOperation.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }

    @SuppressWarnings("unchecked")
    protected <D> Expression<D> createAlias(MapPath<?,D,?> target, Path<D> alias){
        assertRoot(alias);
        return SimpleOperation.create((Class<D>)alias.getType(), Ops.ALIAS, target, alias);
    }

    protected <D> Expression<D> createAlias(SubQueryExpression<D> path, Path<D> alias){
        assertRoot(alias);
        return ExpressionUtils.as(path, alias);
    }

    public T from(Expression<?>... args) {
        for (Expression<?> arg : args){
            metadata.addJoin(JoinType.DEFAULT, arg);
        }
        return self;
    }
    
    public T from(EntityPath<?>... args) {
        for (EntityPath<?> arg : args){
            metadata.addJoin(JoinType.DEFAULT, arg);
        }
        return self;
    }

    public <P> T fullJoin(EntityPath<P> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }

    public <P> T fullJoin(EntityPath<P> target, EntityPath<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T fullJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }

    public <P> T fullJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }

    public <P> T fullJoin(MapPath<?,P,?> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }

    public <P> T fullJoin(MapPath<?,P,?> target, Path<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    
    @SuppressWarnings("unchecked")
    public <P> T fullJoin(SubQueryExpression<P> target, Path alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }

    public T getSelf(){
        return self;
    }

    public T groupBy(Expression<?>... o) {
        metadata.addGroupBy(o);
        return self;
    }

    public T having(Predicate... o) {
        metadata.addHaving(o);
        return self;
    }
    
    public <P> T innerJoin(EntityPath<P> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }

    public <P> T innerJoin(EntityPath<P> target, EntityPath<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public <P> T innerJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }

    public <P> T innerJoin(Path<? extends Collection<P>>target, Path<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public <P> T innerJoin(MapPath<?,P,?> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }

    public <P> T innerJoin(MapPath<?,P,?> target, Path<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    @SuppressWarnings("unchecked")
    public <P> T innerJoin(SubQueryExpression<P> target, Path alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public boolean isDistinct() {
        return metadata.isDistinct();
    }

    public boolean isUnique() {
        return metadata.isUnique();
    }
    
    public <P> T join(EntityPath<P> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return self;
    }

    public <P> T join(EntityPath<P> target, EntityPath<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T join(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return getSelf();
    }

    public <P> T join(Path<? extends Collection<P>> target, Path<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }
    
    public <P> T join(MapPath<?,P,?> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return getSelf();
    }

    public <P> T join(MapPath<?,P,?> target, Path<P> alias) {       
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }

    @SuppressWarnings("unchecked")
    public <P> T join(SubQueryExpression<P> target, Path alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return self;
    }
    
    public <P> T leftJoin(EntityPath<P> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return self;
    }

    public <P> T leftJoin(EntityPath<P> target, EntityPath<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return getSelf();
    }

    public <P> T leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T leftJoin(MapPath<?,P,?> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return getSelf();
    }

    public <P> T leftJoin(MapPath<?,P,?> target, Path<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    @SuppressWarnings("unchecked")
    public <P> T leftJoin(SubQueryExpression<P> target, Path alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
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

    public T on(Predicate... conditions){
        for (Predicate condition : conditions){
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

    public <P> T rightJoin(EntityPath<P> target) {
        metadata.addJoin(JoinType.RIGHTJOIN, target);
        return self;
    }

    public <P> T rightJoin(EntityPath<P> target, EntityPath<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T rightJoin(Path<? extends Collection<P>> target) {
        metadata.addJoin(JoinType.RIGHTJOIN, target);
        return getSelf();
    }

    public <P> T rightJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public <P> T rightJoin(MapPath<?,P,?> target) {
        metadata.addJoin(JoinType.RIGHTJOIN, target);
        return getSelf();
    }

    public <P> T rightJoin(MapPath<?,P,?> target, Path<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    @SuppressWarnings("unchecked")
    public <P> T rightJoin(SubQueryExpression<P> target, Path alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
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
    
    public T where(Predicate... o) {
        metadata.addWhere(o);
        return self;
    }

}
