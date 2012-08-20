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
package com.mysema.query.jpa;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;

import com.mysema.query.QueryMetadata;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.template.NumberTemplate;

/**
 * JPQLQueryBase is a base Query class for JPQL queries
 *
 * @author tiwe
 */
public abstract class JPQLQueryBase<Q extends JPQLQueryBase<Q>> extends ProjectableQuery<Q> {

    private Map<Object,String> constants;

    private final JPQLQueryMixin<Q> queryMixin;

    private final JPQLTemplates templates;
    
    @Nullable
    protected final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public JPQLQueryBase(QueryMetadata md, JPQLTemplates templates, @Nullable EntityManager entityManager) {
        super(new JPQLQueryMixin<Q>(md));
        super.queryMixin.setSelf((Q) this);
        this.queryMixin = (JPQLQueryMixin) super.queryMixin;
        this.templates = templates;
        this.entityManager = entityManager;
    }

    protected JPQLTemplates getTemplates() {
        return templates;
    }

    protected JPQLQueryMixin<Q> getQueryMixin() {
        return queryMixin;
    }
    
    protected String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serialize(queryMixin.getMetadata(), forCountRow, null);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
    }

    @Override
    public boolean exists() {
        if (templates.isSelect1Supported()) {
            return limit(1).singleResult(NumberTemplate.ONE) != null;
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

    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
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

    public <P> Q fullJoin(MapExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(MapExpression<?,P> target, Path<P> alias) {
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

    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    public <P> Q innerJoin(MapExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(MapExpression<?,P> target, Path<P> alias) {
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

    public <P> Q join(MapExpression<?,P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(MapExpression<?,P> target, Path<P> alias) {
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

    public <P> Q leftJoin(MapExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public <P> Q rightJoin(Path<? extends Collection<P>> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <P> Q rightJoin(EntityPath<P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(EntityPath<P> target, EntityPath<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public <P> Q rightJoin(MapExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }
    
    public Q with(Predicate... conditions) {
        return queryMixin.with(conditions);
    }

    protected void setConstants(Map<Object, String> constants) {
        this.constants = constants;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString() {
        return buildQueryString(false);
    }

    @Override
    public String toString() {
        return buildQueryString(false).trim();
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

}
