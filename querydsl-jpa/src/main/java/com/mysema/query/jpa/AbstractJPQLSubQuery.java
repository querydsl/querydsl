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

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.types.CollectionExpression;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.MapExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.template.NumberTemplate;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 * @param <Q> concrete subtype
 */
public class AbstractJPQLSubQuery<Q extends AbstractJPQLSubQuery<Q>> extends DetachableQuery<Q>{

    private final JPQLQueryMixin<Q> queryMixin;

    public AbstractJPQLSubQuery() {
        this(new DefaultQueryMetadata(false));
    }

    @SuppressWarnings("unchecked")
    public AbstractJPQLSubQuery(QueryMetadata metadata) {
        super(new JPQLQueryMixin<Q>(metadata));
        super.queryMixin.setSelf((Q)this);
        this.queryMixin = (JPQLQueryMixin<Q>) super.queryMixin;
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

    public Q from(EntityPath<?>... o) {
        return queryMixin.from(o);
    }

    public <P> Q fullJoin(CollectionExpression<?,P> target) {
        return queryMixin.fullJoin(target);
    }

    public <P> Q fullJoin(CollectionExpression<?,P> target, Path<P> alias) {
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

    public <P> Q innerJoin(CollectionExpression<?,P> target) {
        return queryMixin.innerJoin(target);
    }

    public <P> Q innerJoin(CollectionExpression<?,P> target, Path<P> alias) {
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

    public <P> Q join(CollectionExpression<?,P> target) {
        return queryMixin.join(target);
    }

    public <P> Q join(CollectionExpression<?,P> target, Path<P> alias) {
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

    public <P> Q leftJoin(CollectionExpression<?,P> target) {
        return queryMixin.leftJoin(target);
    }

    public <P> Q leftJoin(CollectionExpression<?,P> target, Path<P> alias) {
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

    public <P> Q rightJoin(CollectionExpression<?,P> target) {
        return queryMixin.rightJoin(target);
    }

    public <P> Q rightJoin(CollectionExpression<?,P> target, Path<P> alias) {
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
    
    public String toString() {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            JPQLSerializer serializer = new JPQLSerializer(JPQLTemplates.DEFAULT);
            serializer.serialize(queryMixin.getMetadata(), false, null);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

}
