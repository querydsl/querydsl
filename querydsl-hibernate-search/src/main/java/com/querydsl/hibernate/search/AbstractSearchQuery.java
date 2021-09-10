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
package com.querydsl.hibernate.search;

import java.util.List;

import com.querydsl.lucene5.LuceneSerializer;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.querydsl.core.*;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

/**
 * Abstract base class for Hibernate Search query classes
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 */
public abstract class AbstractSearchQuery<T, Q extends AbstractSearchQuery<T,Q>> implements SimpleQuery<Q>, Fetchable<T> {

    private final EntityPath<T> path;

    private final QueryMixin<Q> queryMixin;

    private final LuceneSerializer serializer;

    private final FullTextSession session;

    @SuppressWarnings("unchecked")
    public AbstractSearchQuery(FullTextSession session, EntityPath<T> path) {
        this.queryMixin = new QueryMixin<Q>((Q) this);
        this.session = session;
        this.path = path;
        this.serializer = SearchSerializer.DEFAULT;
        queryMixin.from(path);
    }

    public AbstractSearchQuery(Session session, EntityPath<T> path) {
        this(Search.getFullTextSession(session), path);
    }

    @Override
    public long fetchCount() {
        return createQuery(true).getResultSize();
    }

    private FullTextQuery createQuery(boolean forCount) {
        QueryMetadata metadata = queryMixin.getMetadata();
        org.apache.lucene.search.Query query;
        if (metadata.getWhere() != null) {
            query = serializer.toQuery(metadata.getWhere(), metadata);
        } else {
            query = new MatchAllDocsQuery();
        }
        FullTextQuery fullTextQuery = session.createFullTextQuery(query, path.getType());

        // order
        if (!metadata.getOrderBy().isEmpty() && !forCount) {
            fullTextQuery.setSort(serializer.toSort(metadata.getOrderBy()));
        }

        // paging
        QueryModifiers modifiers = metadata.getModifiers();
        if (modifiers.isRestricting() && !forCount) {
            Integer limit = modifiers.getLimitAsInteger();
            Integer offset = modifiers.getOffsetAsInteger();
            if (limit != null) {
                fullTextQuery.setMaxResults(limit);
            }
            if (offset != null) {
                fullTextQuery.setFirstResult(offset);
            }
        }
        return fullTextQuery;
    }


    @Override
    public Q distinct() {
        return queryMixin.distinct();
    }

    @Override
    @SuppressWarnings("unchecked")
    public CloseableIterator<T> iterate() {
        return CloseableIterator.fromIterator(createQuery(false).iterate());
    }

    @Override
    public Q limit(long limit) {
        return queryMixin.limit(limit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> fetch() {
        return createQuery(false).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResults<T> fetchResults() {
        FullTextQuery query = createQuery(false);
        return new QueryResults<T>(query.list(), queryMixin.getMetadata().getModifiers(), query.getResultSize());
    }

    @Override
    public Q offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public Q restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public <P> Q set(ParamExpression<P> param, P value) {
        return queryMixin.set(param, value);
    }

    @Override
    public T fetchFirst() {
        return limit(1).fetchOne();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() throws NonUniqueResultException {
        try {
            return (T) createQuery(false).uniqueResult();
        } catch (org.hibernate.NonUniqueResultException e) {
            throw new NonUniqueResultException(e);
        }
    }

    @Override
    public Q where(Predicate... e) {
        return queryMixin.where(e);
    }

}
