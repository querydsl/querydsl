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
package com.querydsl.collections;

import com.querydsl.core.util.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.JoinType;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryResults;
import com.querydsl.core.support.FetchableQueryBase;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * {@code AbstractCollQuery} provides a base class for {@code Collection} query implementations.
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @see CollQuery
 * @author tiwe
 */
public abstract class AbstractCollQuery<T, Q extends AbstractCollQuery<T, Q>> extends FetchableQueryBase<T, Q>
        implements FetchableQuery<T, Q> {

    private final Map<Expression<?>, Iterable<?>> iterables = new HashMap<Expression<?>, Iterable<?>>();

    private final QueryEngine queryEngine;

    public AbstractCollQuery(QueryMetadata metadata, QueryEngine queryEngine) {
        super(new CollQueryMixin<Q>(metadata));
        @SuppressWarnings("unchecked") // Q is this + subtype
        Q self = (Q) this;
        this.queryMixin.setSelf(self);
        this.queryEngine = queryEngine;
    }

    @Override
    public long fetchCount() {
        return queryEngine.count(queryMixin.getMetadata(), iterables);
    }

    protected QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    private <D> Expression<D> createAlias(Path<? extends Collection<D>> target, Path<D> alias) {
        return ExpressionUtils.operation(alias.getType(), Ops.ALIAS, target, alias);
    }

    private <D> Expression<D> createAlias(MapExpression<?,D> target, Path<D> alias) {
        return ExpressionUtils.operation(alias.getType(), Ops.ALIAS, target, alias);
    }

    /**
     * Add a query source
     *
     * @param <A> type of expression
     * @param entity Path for the source
     * @param col content of the source
     * @return current object
     */
    public <A> Q from(Path<A> entity, Iterable<? extends A> col) {
        iterables.put(entity, col);
        getMetadata().addJoin(JoinType.DEFAULT, entity);
        return queryMixin.getSelf();
    }

    /**
     * Bind the given collection to an already existing query source
     *
     * @param <A> type of expression
     * @param entity Path for the source
     * @param col content of the source
     * @return current object
     */
    public <A> Q bind(Path<A> entity, Iterable<? extends A> col) {
        iterables.put(entity, col);
        return queryMixin.getSelf();
    }

    @Override
    public Q groupBy(Expression<?> e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Q groupBy(Expression<?>... o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Q having(Predicate e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Q having(Predicate... e) {
        throw new UnsupportedOperationException();
    }

    protected QueryEngine getQueryEngine() {
        return queryEngine;
    }

    /**
     * Define an inner join from the Collection typed path to the alias
     *
     * @param <P> type of expression
     * @param target target of the join
     * @param alias alias for the join target
     * @return current object
     */
    public <P> Q innerJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return queryMixin.getSelf();
    }

    /**
     * Define an inner join from the Map typed path to the alias
     *
     * @param <P> type of expression
     * @param target target of the join
     * @param alias alias for the join target
     * @return current object
     */
    public <P> Q innerJoin(MapExpression<?,P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return queryMixin.getSelf();
    }

    /**
     * Define a left join from the Collection typed path to the alias
     *
     * @param <P> type of expression
     * @param target target of the join
     * @param alias alias for the join target
     * @return current object
     */
    public <P> Q leftJoin(Path<? extends Collection<P>> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return queryMixin.getSelf();
    }

    /**
     * Define a left join from the Map typed path to the alias
     *
     * @param <P> type of expression
     * @param target target of the join
     * @param alias alias for the joint target
     * @return current object
     */
    public <P> Q leftJoin(MapExpression<?,P> target, Path<P> alias) {
        getMetadata().addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return queryMixin.getSelf();
    }

    @Override
    public CloseableIterator<T> iterate() {
        @SuppressWarnings("unchecked") // This is the built type
        Expression<T> projection = (Expression<T>) queryMixin.getMetadata().getProjection();
        return CloseableIterator.fromIterator(fetch().iterator());
    }

    @Override
    public Stream<T> stream() {
        return fetch().stream();
    }

    @Override
    public List<T> fetch() {
        @SuppressWarnings("unchecked") // This is the built type
        Expression<T> projection = (Expression<T>) queryMixin.getMetadata().getProjection();
        return queryEngine.list(getMetadata(), iterables, projection);
    }

    @Override
    public QueryResults<T> fetchResults() {
        @SuppressWarnings("unchecked") // This is the built type
        Expression<T> projection = (Expression<T>) queryMixin.getMetadata().getProjection();
        long count = queryEngine.count(getMetadata(), iterables);
        if (count > 0L) {
            List<T> list = queryEngine.list(getMetadata(), iterables, projection);
            return new QueryResults<T>(list, getMetadata().getModifiers(), count);
        } else {
            return QueryResults.<T>emptyResults();
        }
    }

    @Override
    public T fetchOne() throws NonUniqueResultException {
        queryMixin.setUnique(true);
        if (queryMixin.getMetadata().getModifiers().getLimit() == null) {
            limit(2L);
        }
        return uniqueResult(iterate());
    }


}
