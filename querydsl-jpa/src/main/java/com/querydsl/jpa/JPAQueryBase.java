/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
import com.querydsl.core.support.FetchableSubQueryBase;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;

/**
 * {@code JPAQueryBase} is a base Query class for JPA queries
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @author tiwe
 */
public abstract class JPAQueryBase<T, Q extends JPAQueryBase<T, Q>> extends FetchableSubQueryBase<T, Q> implements JPQLQuery<T> {

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
    public Q fetchJoin() {
        return queryMixin.fetchJoin();
    }

    @Override
    public Q fetchAll() {
        return queryMixin.fetchAll();
    }

    public Q from(EntityPath<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public Q from(EntityPath<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    public <P> Q from(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.from(Expressions.as((Path) target, alias));
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P>target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q innerJoin(EntityPath<P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q innerJoin(MapExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <P> Q join(CollectionExpression<?,P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q join(EntityPath<P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(EntityPath<P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q join(MapExpression<?,P> target) {
        return queryMixin.join(target);
    }

    @Override
    public <P> Q join(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <P> Q leftJoin(CollectionExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q leftJoin(EntityPath<P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q leftJoin(MapExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <P> Q leftJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(CollectionExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(EntityPath<P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <P> Q rightJoin(MapExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <P> Q rightJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }


    @Override
    public String toString() {
        JPQLSerializer serializer = serialize(false);
        return serializer.toString().trim();
    }

    @Override
    public abstract Q clone();

}
