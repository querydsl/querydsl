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
package com.querydsl.sql;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Query;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.*;

/**
 * Default implementation of the Union interface
 * 
 * @author tiwe
 *
 * @param <T>
 */
public class UnionImpl<T, Q extends ProjectableSQLQuery<T, Q> & Query<Q>>  implements Union<T> {
    
    private final Q query;

    public UnionImpl(Q query) {
        this.query = query;
    }
    
    @Override
    public List<T> list() {
        return query.fetch();
    }

    @Override
    public CloseableIterator<T> iterate() {
        return query.iterate();
    }

    @Override
    public Union<T> groupBy(Expression<?>... o) {
        query.groupBy(o);
        return this;
    }

    @Override
    public Union<T> having(Predicate... o) {
        query.having(o);
        return this;
    }

    
    @Override
    public Union<T> orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return this;
    }

    @Override
    public Expression<T> as(String alias) {
        return ExpressionUtils.as(this, alias);
    }

    @Override
    public Expression<T> as(Path<T> alias) {
        return ExpressionUtils.as(this, alias);
    }

    @Override
    public String toString() {
        return query.toString();
    }

    @Nullable
    @Override
    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return query.accept(v, context);
    }

    @Override
    public Class<? extends T> getType() {
        return query.getType();
    }

    @Override
    public QueryMetadata getMetadata() {
        return query.getMetadata();
    }

}
