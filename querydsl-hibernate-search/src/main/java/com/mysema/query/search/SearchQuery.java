/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.lucene.LuceneSerializer;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.Param;

/**
 * SearchQuery is a Query implementation for Hibernate Search
 *
 * @author tiwe
 *
 * @param <T>
 */
public class SearchQuery<T> implements SimpleQuery<SearchQuery<T>>, SimpleProjectable<T>{

    private final EntityPath<T> path;

    private final QueryMixin<SearchQuery<T>> queryMixin;

    private final LuceneSerializer serializer;

    private final FullTextSession session;

    public SearchQuery(FullTextSession session, EntityPath<T> path) {
        this.queryMixin = new QueryMixin<SearchQuery<T>>(this);
        this.session = Assert.notNull(session,"session");
        this.path = Assert.notNull(path,"path");
        this.serializer = SearchSerializer.DEFAULT;
    }

    public SearchQuery(Session session, EntityPath<T> path) {
        this(Search.getFullTextSession(session), path);
    }

    @Override
    public long count() {
        return createQuery(true).getResultSize();
    }

    @Override
    public long countDistinct() {
        return count();
    }

    private FullTextQuery createQuery(boolean forCount){
        QueryMetadata metadata = queryMixin.getMetadata();
        Assert.notNull(metadata.getWhere(), "where needs to be set");
        org.apache.lucene.search.Query query = serializer.toQuery(metadata.getWhere(), metadata);

        FullTextQuery fullTextQuery = session.createFullTextQuery(query, path.getType());

        // order
        if (!metadata.getOrderBy().isEmpty() && !forCount){
            fullTextQuery.setSort(serializer.toSort(metadata.getOrderBy()));
        }

        // paging
        QueryModifiers modifiers = metadata.getModifiers();
        if (modifiers != null && modifiers.isRestricting() && !forCount){
            if (modifiers.getLimit() != null){
                fullTextQuery.setMaxResults(modifiers.getLimit().intValue());
            }
            if (modifiers.getOffset() != null){
                fullTextQuery.setFirstResult(modifiers.getOffset().intValue());
            }
        }
        return fullTextQuery;
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

    @Override
    public List<T> listDistinct() {
        return list();
    }

    @Override
    public SearchResults<T> listDistinctResults() {
        return listResults();
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
    public <P> SearchQuery<T> set(Param<P> param, P value) {
        return queryMixin.set(param, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T uniqueResult() {
        return (T) createQuery(false).uniqueResult();
    }

    @Override
    public SearchQuery<T> where(Predicate... e) {
        return queryMixin.where(e);
    }

}
