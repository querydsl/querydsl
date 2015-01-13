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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.DetachableQuery;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.*;
import com.querydsl.core.types.query.NumberSubQuery;
import com.querydsl.core.types.template.NumberTemplate;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q> concrete subtype
 */
public class AbstractJPASubQuery<Q extends AbstractJPASubQuery<Q>> extends DetachableQuery<Q> implements JPQLSubQuery {

    private final JPAQueryMixin<Q> queryMixin;

    public AbstractJPASubQuery() {
        this(new DefaultQueryMetadata().noValidate());
    }

    @SuppressWarnings("unchecked")
    public AbstractJPASubQuery(QueryMetadata metadata) {
        super(new JPAQueryMixin<Q>(metadata));
        super.queryMixin.setSelf((Q)this);
        this.queryMixin = (JPAQueryMixin<Q>) super.queryMixin;
    }

    @Override
    public NumberSubQuery<Long> count() {
        StringBuilder count = new StringBuilder();
        for (JoinExpression join : queryMixin.getMetadata().getJoins()) {
            if (join.getType() == JoinType.DEFAULT) {
                count.append(count.length() == 0 ? "count(" : ", ");
                count.append(join.getTarget().toString());
            }
        }
        count.append(")");
        return unique(NumberTemplate.create(Long.class, count.toString()));
    }

    public Q from(EntityPath<?> o) {
        return queryMixin.from(o);
    }

    @Override
    public Q from(EntityPath<?>... o) {
        return queryMixin.from(o);
    }

    @Override
    public <P> Q from(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.fullJoin(Expressions.as((Path)target, alias));
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(CollectionExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(CollectionExpression<?,P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(EntityPath<P> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(EntityPath<P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(MapExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    @Deprecated
    public <P> Q fullJoin(MapExpression<?,P> target, Path<P> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <P> Q innerJoin(CollectionExpression<?,P> target, Path<P> alias) {
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
    public Q limit(long l) {
        throw new UnsupportedOperationException("JPQL doesn't support limit on subqueries");
    }

    @Override
    public Q offset(long o) {
        throw new UnsupportedOperationException("JPQL doesn't support offset on subqueries");
    }

    @Override
    public String toString() {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT, null);
            serializer.setStrict(false);
            serializer.serialize(queryMixin.getMetadata(), false, null);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

}
