/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.support.ProjectableQuery;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.template.NumberTemplate;

/**
 * JPAQueryBase is a base Query class for JPA queries
 *
 * @author tiwe
 */
public abstract class JPAQueryBase<Q extends JPAQueryBase<Q>> extends ProjectableQuery<Q> implements JPQLQuery {

    protected final JPAQueryMixin<Q> queryMixin;

    private final JPQLTemplates templates;

    @SuppressWarnings("unchecked")
    public JPAQueryBase(QueryMetadata md, JPQLTemplates templates) {
        super(new JPAQueryMixin<Q>(md));
        super.queryMixin.setSelf((Q) this);
        this.queryMixin = (JPAQueryMixin<Q>) super.queryMixin;
        this.templates = templates;
    }

    protected JPQLTemplates getTemplates() {
        return templates;
    }
    
    protected abstract JPQLSerializer createSerializer();
    
    protected JPQLSerializer serialize(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        JPQLSerializer serializer = createSerializer();
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);
        return serializer;
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
    }

    @Override
    public boolean exists() {
        if (templates.isSelect1Supported()) {
            return singleResult(NumberTemplate.ONE) != null;
        } else {            
            EntityPath<?> entityPath = (EntityPath<?>) queryMixin.getMetadata().getJoins().get(0).getTarget();
            return !limit(1).list(entityPath).isEmpty();
        }                
    }

    public Q fetch() {
        return queryMixin.fetch();
    }

    public Q fetchAll() {
        return queryMixin.fetchAll();
    }

    public Q from(EntityPath<?> arg) {
        return queryMixin.from(arg);
    }
    
    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    @Deprecated
    public <P> Q fullJoin(CollectionExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    @Deprecated
    public <P> Q fullJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Deprecated
    public <P> Q fullJoin(EntityPath<P> target) {
        return queryMixin.fullJoin(target);
    }

    @Deprecated
    public <P> Q fullJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Deprecated
    public <P> Q fullJoin(MapExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    @Deprecated
    public <P> Q fullJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    public <P> Q innerJoin(CollectionExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(CollectionExpression<?,P>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(MapExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q join(CollectionExpression<?,P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q join(EntityPath<P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(EntityPath<P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q join(MapExpression<?,P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    public <P> Q leftJoin(CollectionExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(EntityPath<P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q leftJoin(MapExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q rightJoin(CollectionExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <P> Q rightJoin(EntityPath<P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <P> Q rightJoin(MapExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }
    
    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }
    
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(queryMixin.createProjection(args));
    }

    @Override
    public String toString() {
        JPQLSerializer serializer = serialize(false);
        return serializer.toString().trim();
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }
    
    public abstract Q clone();

}
