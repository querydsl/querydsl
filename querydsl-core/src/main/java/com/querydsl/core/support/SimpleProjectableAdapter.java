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
package com.querydsl.core.support;

import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Projectable;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.SearchResults;
import com.querydsl.core.SimpleProjectable;
import com.querydsl.core.SimpleQuery;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

/**
 * SimpleQueryAdapter is an adapter implementation for the {@link SimpleQuery} and 
 * {@link SimpleProjectable} interfaces
 *
 * @author tiwe
 *
 * @param <T> type of entity
 */
public class SimpleProjectableAdapter<T> implements SimpleQuery<SimpleProjectableAdapter<T>>, SimpleProjectable<T> {

    private final Projectable projectable;

    private final Expression<T> projection;

    private final SimpleQuery<?> query;

    @SuppressWarnings("BC_UNCONFIRMED_CAST")
    public <Q extends SimpleQuery<?> & Projectable> SimpleProjectableAdapter(Q query, Expression<T> projection) {
        // NOTE : this is a correct cast which is not handled properly by FindBugs
        this(query, query, projection);
    }

    public SimpleProjectableAdapter(SimpleQuery<?> query, Projectable projectable, Expression<T> projection) {
        this.query = query;
        this.projectable = projectable;
        this.projection = projection;
    }

    @Override
    public boolean exists() {
        return projectable.exists();
    }

    @Override
    public boolean notExists() {
        return projectable.notExists();
    }

    @Override
    public long count() {
        return projectable.count();
    }

    @Override
    public SimpleProjectableAdapter<T> distinct() {
        query.distinct();
        return this;
    }

    @Override
    public SimpleProjectableAdapter<T> limit(long limit) {
        query.limit(limit);
        return this;
    }

    @Override
    public CloseableIterator<T> iterate() {
        return projectable.iterate(projection);
    }

    @Override
    public List<T> list() {
        return projectable.list(projection);
    }

    @Override
    public SearchResults<T> listResults() {
        return projectable.listResults(projection);
    }

    @Override
    public SimpleProjectableAdapter<T> offset(long offset) {
        query.offset(offset);
        return this;
    }

    @Override
    public SimpleProjectableAdapter<T> orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return this;
    }

    @Override
    public SimpleProjectableAdapter<T> restrict(QueryModifiers modifiers) {
        query.restrict(modifiers);
        return this;
    }

    @Override
    public <P> SimpleProjectableAdapter<T> set(ParamExpression<P> param, P value) {
        query.set(param, value);
        return this;
    }

    @Override
    public String toString() {
        return query.toString();
    }

    @Override
    public T singleResult() {
        return projectable.singleResult(projection);
    }

    @Override
    public T uniqueResult() {
        return projectable.uniqueResult(projection);
    }

    @Override
    public SimpleProjectableAdapter<T> where(Predicate... e) {
        query.where(e);
        return this;
    }

}
