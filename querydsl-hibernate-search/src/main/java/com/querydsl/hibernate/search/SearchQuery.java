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
package com.querydsl.hibernate.search;

import java.util.List;

import org.apache.lucene.search.MatchAllDocsQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.SearchResults;
import com.querydsl.core.SimpleProjectable;
import com.querydsl.core.SimpleQuery;
import com.querydsl.lucene3.LuceneSerializer;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Predicate;

/**
 * SearchQuery is a Query implementation for Hibernate Search
 *
 * @author tiwe
 *
 * @param <T>
 */
public class SearchQuery<T> implements SimpleQuery<SearchQuery<T>>, SimpleProjectable<T> {

    private final EntityPath<T> path;

    private final QueryMixin<SearchQuery<T>> queryMixin;

    private final LuceneSerializer serializer;

    private final FullTextSession session;

    /**
     * Create a new SearchQuery instance
     * 
     * @param session
     * @param path
     */
    public SearchQuery(FullTextSession session, EntityPath<T> path) {
        this.queryMixin = new QueryMixin<SearchQuery<T>>(this);
        this.session = session;
        this.path = path;
        this.serializer = SearchSerializer.DEFAULT;
        queryMixin.from(path);
    }

    public SearchQuery(Session session, EntityPath<T> path) {
        this(Search.getFullTextSession(session), path);
    }


    @Override
    public boolean exists() {
        return createQuery(true).getResultSize() > 0;
    }

    @Override
    public boolean notExists() {
        return createQuery(true).getResultSize() == 0;
    }

    @Override
    public long count() {
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
        if (modifiers != null && modifiers.isRestricting() && !forCount) {
            Integer limit = modifiers.getLimitAsInteger();
            Integer offset = modifiers.getOffsetAsInteger();
            if (limit != null) {
                fullTextQuery.setMaxResults(limit.intValue());
            }
            if (offset != null) {
                fullTextQuery.setFirstResult(offset.intValue());
            }
        }
        return fullTextQuery;
    }


    @Override
    public SearchQuery<T> distinct() {
        // do nothing
        return this;
    }


    @SuppressWarnings("unchecked")
    public CloseableIterator<T> iterate() {
        return new IteratorAdapter<T>(createQuery(false).iterate());
    }

    public CloseableIterator<T> iterateDistinct() {
        return iterate();
    }

    @Override
    public SearchQuery<T> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> list() {
        return createQuery(false).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public SearchResults<T> listResults() {
        FullTextQuery query = createQuery(false);
        return new SearchResults<T>(query.list(), queryMixin.getMetadata().getModifiers(), query.getResultSize());
    }

    @Override
    public SearchQuery<T> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public SearchQuery<T> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public SearchQuery<T> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public <P> SearchQuery<T> set(ParamExpression<P> param, P value) {
        return queryMixin.set(param, value);
    }

    @Override
    public T singleResult() {
        return limit(1).uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T uniqueResult() {
        try {
            return (T) createQuery(false).uniqueResult();
        } catch (org.hibernate.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        }
    }

    @Override
    public SearchQuery<T> where(Predicate... e) {
        return queryMixin.where(e);
    }


}
