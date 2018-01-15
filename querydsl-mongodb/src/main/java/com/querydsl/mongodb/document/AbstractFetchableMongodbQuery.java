/*
 * Copyright 2018, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.mongodb.document;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.ReadPreference;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link Fetchable} Mongodb query with a pluggable Document to Bean transformation.
 *
 * @param <K> result type
 * @param <Q> concrete subtype
 * @author Mark Paluch
 */
public abstract class AbstractFetchableMongodbQuery<K, Q extends AbstractFetchableMongodbQuery<K, Q>>
        extends AbstractMongodbQuery<Q> implements Fetchable<K> {

    private final Function<Document, K> transformer;

    private final MongoCollection<Document> collection;

    /**
     * Create a new MongodbQuery instance
     * @param collection
     * @param transformer result transformer
     * @param serializer serializer
     */
    public AbstractFetchableMongodbQuery(MongoCollection<Document> collection, Function<Document, K> transformer, MongodbDocumentSerializer serializer) {
        super(serializer);
        this.transformer = transformer;
        this.collection = collection;
    }

    /**
     * Iterate with the specific fields
     *
     * @param paths fields to return
     * @return iterator
     */
    public CloseableIterator<K> iterate(Path<?>... paths) {
        getQueryMixin().setProjection(paths);
        return iterate();
    }

    @Override
    public CloseableIterator<K> iterate() {
        FindIterable<Document> cursor = createCursor();
        final MongoCursor<Document> iterator = cursor.iterator();

        return new CloseableIterator<K>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public K next() {
                return transformer.apply(iterator.next());
            }

            @Override
            public void remove() {

            }

            @Override
            public void close() {
                iterator.close();
            }
        };
    }

    /**
     * Fetch with the specific fields
     *
     * @param paths fields to return
     * @return results
     */
    public List<K> fetch(Path<?>... paths) {
        getQueryMixin().setProjection(paths);
        return fetch();
    }

    @Override
    public List<K> fetch() {
        try {
            FindIterable<Document> cursor = createCursor();
            List<K> results = new ArrayList<K>();
            for (Document document : cursor) {
                results.add(transformer.apply(document));
            }
            return results;
        } catch (NoResults ex) {
            return Collections.emptyList();
        }
    }

    /**
     * Fetch first with the specific fields
     *
     * @param paths fields to return
     * @return first result
     */
    public K fetchFirst(Path<?>... paths) {
        getQueryMixin().setProjection(paths);
        return fetchFirst();
    }

    @Override
    public K fetchFirst() {
        try {
            FindIterable<Document> c = createCursor().limit(1);
            MongoCursor<Document> iterator = c.iterator();
            try {

                if (iterator.hasNext()) {
                    return transformer.apply(iterator.next());
                } else {
                    return null;
                }
            } finally {
                iterator.close();
            }
        } catch (NoResults ex) {
            return null;
        }
    }

    /**
     * Fetch one with the specific fields
     *
     * @param paths fields to return
     * @return first result
     */
    public K fetchOne(Path<?>... paths) {
        getQueryMixin().setProjection(paths);
        return fetchOne();
    }

    @Override
    public K fetchOne() {
        try {
            Long limit = getQueryMixin().getMetadata().getModifiers().getLimit();
            if (limit == null) {
                limit = 2L;
            }

            FindIterable<Document> c = createCursor().limit(limit.intValue());
            MongoCursor<Document> iterator = c.iterator();
            try {

                if (iterator.hasNext()) {
                K rv = transformer.apply(iterator.next());
                if (iterator.hasNext()) {
                    throw new NonUniqueResultException();
                }
                return rv;
            } else {
                return null;
            }
            } finally {
                iterator.close();
            }


        } catch (NoResults ex) {
            return null;
        }
    }

    /**
     * Fetch results with the specific fields
     *
     * @param paths fields to return
     * @return results
     */
    public QueryResults<K> fetchResults(Path<?>... paths) {
        getQueryMixin().setProjection(paths);
        return fetchResults();
    }

    @Override
    public QueryResults<K> fetchResults() {
        try {
            long total = fetchCount();
            if (total > 0L) {
                return new QueryResults<K>(fetch(), getQueryMixin().getMetadata().getModifiers(), total);
            } else {
                return QueryResults.emptyResults();
            }
        } catch (NoResults ex) {
            return QueryResults.emptyResults();
        }
    }

    @Override
    public long fetchCount() {
        try {
            Predicate filter = createFilter(getQueryMixin().getMetadata());
            return collection.count(createQuery(filter));
        } catch (NoResults ex) {
            return 0L;
        }
    }

    protected FindIterable<Document> createCursor() {
        QueryMetadata metadata = getQueryMixin().getMetadata();
        Predicate filter = createFilter(metadata);
        return createCursor(collection, filter, metadata.getProjection(), metadata.getModifiers(), metadata.getOrderBy());
    }

    protected FindIterable<Document> createCursor(MongoCollection<Document> collection, @Nullable Predicate where,
                                                  Expression<?> projection, QueryModifiers modifiers, List<OrderSpecifier<?>> orderBy) {

        ReadPreference readPreference = getReadPreference();
        MongoCollection<Document> collectionToUse = readPreference != null ? collection
                .withReadPreference(readPreference) : collection;
        FindIterable<Document> cursor = collectionToUse.find(createQuery(where))
                .projection(createProjection(projection));
        Integer limit = modifiers.getLimitAsInteger();
        Integer offset = modifiers.getOffsetAsInteger();

        if (limit != null) {
            cursor = cursor.limit(limit);
        }
        if (offset != null) {
            cursor = cursor.skip(offset);
        }
        if (orderBy.size() > 0) {
            cursor = cursor.sort(getSerializer().toSort(orderBy));
        }
        return cursor;
    }

    @Nullable
    protected Predicate createFilter(QueryMetadata metadata) {
        Predicate filter;
        if (!metadata.getJoins().isEmpty()) {
            filter = ExpressionUtils.allOf(metadata.getWhere(), createJoinFilter(metadata));
        } else {
            filter = metadata.getWhere();
        }
        return filter;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected Predicate createJoinFilter(QueryMetadata metadata) {
        Multimap<Expression<?>, Predicate> predicates = HashMultimap.create();
        List<JoinExpression> joins = metadata.getJoins();
        for (int i = joins.size() - 1; i >= 0; i--) {
            JoinExpression join = joins.get(i);
            Path<?> source = (Path) ((Operation<?>) join.getTarget()).getArg(0);
            Path<?> target = (Path) ((Operation<?>) join.getTarget()).getArg(1);
            Collection<Predicate> extraFilters = predicates.get(target.getRoot());
            Predicate filter = ExpressionUtils.allOf(join.getCondition(), allOf(extraFilters));
            List<? extends Object> ids = getIds(target.getType(), filter);
            if (ids.isEmpty()) {
                throw new NoResults();
            }
            Path<?> path = ExpressionUtils.path(String.class, source, "$id");
            predicates.put(source.getRoot(), ExpressionUtils.in((Path<Object>) path, ids));
        }
        Path<?> source = (Path) ((Operation) joins.get(0).getTarget()).getArg(0);
        return allOf(predicates.get(source.getRoot()));
    }

    private Predicate allOf(Collection<Predicate> predicates) {
        return predicates != null ? ExpressionUtils.allOf(predicates) : null;
    }

    protected abstract MongoCollection<Document> getCollection(Class<?> type);

    protected List<Object> getIds(Class<?> targetType, Predicate condition) {
        MongoCollection<Document> collection = getCollection(targetType);
        // TODO : fetch only ids
        FindIterable<Document> cursor = createCursor(collection, condition, null,
                QueryModifiers.EMPTY, Collections.<OrderSpecifier<?>>emptyList());

        MongoCursor<Document> iterator = cursor.iterator();
        try {

            if (iterator.hasNext()) {
                List<Object> ids = new ArrayList<Object>();
                for (Document obj : cursor) {
                    ids.add(obj.get("_id"));
                }
                return ids;
            } else {
                return Collections.emptyList();
            }
        } finally {
           iterator.close();
        }
    }
}
