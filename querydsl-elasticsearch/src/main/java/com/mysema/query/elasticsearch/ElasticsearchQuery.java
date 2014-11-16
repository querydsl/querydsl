/*
 * Copyright 2014, Mysema Ltd
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
package com.mysema.query.elasticsearch;

import com.google.common.base.Function;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.*;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.*;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * ElasticsearchQuery provides a general Querydsl query implementation with a pluggable String to Bean transformation
 *
 * @param <K>
 * @author Kevin Leturc
 */
public abstract class ElasticsearchQuery<K> implements SimpleQuery<ElasticsearchQuery<K>>, SimpleProjectable<K> {

    private final QueryMixin<ElasticsearchQuery<K>> queryMixin;

    private final Client client;

    private final Function<SearchHit, K> transformer;

    private final ElasticsearchSerializer serializer;

    private final EntityPath<K> entityPath;

    public ElasticsearchQuery(Client client, Function<SearchHit, K> transformer, ElasticsearchSerializer serializer,
                              EntityPath<K> entityPath) {
        this.queryMixin = new QueryMixin<ElasticsearchQuery<K>>(this, new DefaultQueryMetadata().noValidate(), false);
        this.client = client;
        this.transformer = transformer;
        this.serializer = serializer;
        this.entityPath = entityPath;
    }

    @Override
    public boolean exists() {
        return count() > 0;
    }

    @Override
    public boolean notExists() {
        return !exists();
    }

    @Override
    public CloseableIterator<K> iterate() {
        return new IteratorAdapter<K>(list().iterator());
    }

    public List<K> list(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return list();
    }

    @Override
    public List<K> list() {
        // Test if there're limit or offset, and if not, set them to retrieve all results
        // because by default elasticsearch returns only 10 results
        QueryMetadata metadata = queryMixin.getMetadata();
        QueryModifiers modifiers = metadata.getModifiers();
        if (modifiers.getLimit() == null && modifiers.getOffset() == null) {
            long count = count();
            if (count > 0l) {
                // Set the limit only if there's result
                metadata.setModifiers(new QueryModifiers(count, 0L));
            }
        }

        // Execute search
        SearchResponse searchResponse = executeSearch();
        List<K> results = new ArrayList<K>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            results.add(transformer.apply(hit));
        }
        return results;
    }

    public K singleResult(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return singleResult();
    }

    @Nullable
    @Override
    public K singleResult() {
        // Set the size of response
        queryMixin.getMetadata().setModifiers(new QueryModifiers(1L, 0L));

        SearchResponse searchResponse = executeSearch();
        SearchHits hits = searchResponse.getHits();
        if (hits.getTotalHits() > 0) {
            return transformer.apply(hits.getAt(0));
        } else {
            return null;
        }
    }

    public K uniqueResult(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return uniqueResult();
    }

    @Nullable
    @Override
    public K uniqueResult() {
        // Set the size of response
        // Set 2 as limit because it has to be ony one result which match the condition
        queryMixin.getMetadata().setModifiers(new QueryModifiers(2L, 0L));

        SearchResponse searchResponse = executeSearch();
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        if (totalHits == 1l) {
            return transformer.apply(hits.getAt(0));
        } else if (totalHits > 1l) {
            throw new NonUniqueResultException();
        } else {
            return null;
        }
    }

    public SearchResults<K> listResults(Path<?>... paths) {
        queryMixin.addProjection(paths);
        return listResults();
    }

    @Override
    public SearchResults<K> listResults() {
        long total = count();
        if (total > 0l) {
            return new SearchResults<K>(list(), queryMixin.getMetadata().getModifiers(), total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    @Override
    public long count() {
        Predicate filter = createFilter(queryMixin.getMetadata());
        return client.prepareCount().setQuery(createQuery(filter)).execute().actionGet().getCount();
    }

    @Override
    public ElasticsearchQuery<K> limit(@Nonnegative long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public ElasticsearchQuery<K> offset(@Nonnegative long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public ElasticsearchQuery<K> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    @Override
    public ElasticsearchQuery<K> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> ElasticsearchQuery<K> set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    @Override
    public ElasticsearchQuery<K> distinct() {
        return queryMixin.distinct();
    }

    @Override
    public ElasticsearchQuery<K> where(Predicate... o) {
        return queryMixin.where(o);
    }

    @Nullable
    protected Predicate createFilter(QueryMetadata metadata) {
        return metadata.getWhere();
    }

    private QueryBuilder createQuery(@Nullable Predicate predicate) {
        if (predicate != null) {
            return (QueryBuilder) serializer.handle(predicate);
        } else {
            return QueryBuilders.matchAllQuery();
        }
    }

    private SearchResponse executeSearch() {
        QueryMetadata metadata = queryMixin.getMetadata();
        Predicate filter = createFilter(metadata);
        return executeSearch(getIndex(), getType(), filter, metadata.getProjection(), metadata.getModifiers(),
                metadata.getOrderBy());
    }

    private SearchResponse executeSearch(String index, String type, Predicate filter,
            List<Expression<?>> projections, QueryModifiers modifiers, List<OrderSpecifier<?>> orderBys) {
        SearchRequestBuilder requestBuilder = client.prepareSearch(index).setTypes(type);

        // Set query
        requestBuilder.setQuery(createQuery(filter));

        // Add order by
        for (OrderSpecifier<?> sort : orderBys) {
            requestBuilder.addSort(serializer.toSort(sort));
        }

        // Add projections
        if (!projections.isEmpty()) {
            List<String> sourceFields = new ArrayList<String>();
            for (Expression<?> projection : projections) {
                sourceFields.add(projection.accept(serializer, null).toString());
            }
            requestBuilder.setFetchSource(sourceFields.toArray(new String[sourceFields.size()]), null);
        }

        // Add limit and offset
        Integer limit = modifiers.getLimitAsInteger();
        Integer offset = modifiers.getOffsetAsInteger();
        if (limit != null) {
            requestBuilder.setSize(limit);
        }
        if (offset != null) {
            requestBuilder.setFrom(offset);
        }

        return requestBuilder.execute().actionGet();
    }

    public String getIndex() {
        return getIndex(entityPath.getType());
    }

    public abstract String getIndex(Class<? extends K> entityType);

    public String getType() {
        return getType(entityPath.getType());
    }

    public abstract String getType(Class<? extends K> entityType);

}
