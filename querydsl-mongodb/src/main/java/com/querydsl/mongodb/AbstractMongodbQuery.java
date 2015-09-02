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
package com.querydsl.mongodb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.*;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.CollectionPathBase;

/**
 * {@code AbstractMongodbQuery} provides a base class for general Querydsl query implementation with a
 * pluggable DBObject to Bean transformation
 *
 * @author laimw
 *
 * @param <K> result type
 * @param <Q> concrete subtype
 */
public abstract class AbstractMongodbQuery<K, Q extends AbstractMongodbQuery<K, Q>> implements SimpleQuery<Q>, Fetchable<K> {

    @SuppressWarnings("serial")
    private static class NoResults extends RuntimeException { }

    private final MongodbSerializer serializer;

    private final QueryMixin<Q> queryMixin;

    private final DBCollection collection;

    private final Function<DBObject, K> transformer;

    private ReadPreference readPreference;

    /**
     * Create a new MongodbQuery instance
     *
     * @param collection collection
     * @param transformer result transformer
     * @param serializer serializer
     */
    public AbstractMongodbQuery(DBCollection collection, Function<DBObject, K> transformer, MongodbSerializer serializer) {
        this.queryMixin = new QueryMixin<Q>((Q) this, new DefaultQueryMetadata(), false);
        this.transformer = transformer;
        this.collection = collection;
        this.serializer = serializer;
    }

    /**
     * Define a join
     *
     * @param ref reference
     * @param target join target
     * @return join builder
     */
    public <T> JoinBuilder<Q, K,T> join(Path<T> ref, Path<T> target) {
        return new JoinBuilder<Q, K,T>(queryMixin, ref, target);
    }

    /**
     * Define a join
     *
     * @param ref reference
     * @param target join target
     * @return join builder
     */
    public <T> JoinBuilder<Q, K,T> join(CollectionPathBase<?,T,?> ref, Path<T> target) {
        return new JoinBuilder<Q, K,T>(queryMixin, ref, target);
    }

    /**
     * Define a constraint for an embedded object
     *
     * @param collection collection
     * @param target target
     * @return builder
     */
    public <T> AnyEmbeddedBuilder<Q, K> anyEmbedded(Path<? extends Collection<T>> collection, Path<T> target) {
        return new AnyEmbeddedBuilder<Q, K>(queryMixin, collection);
    }

    protected abstract DBCollection getCollection(Class<?> type);

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

    @Nullable
    protected Predicate createJoinFilter(QueryMetadata metadata) {
        Multimap<Expression<?>, Predicate> predicates = HashMultimap.create();
        List<JoinExpression> joins = metadata.getJoins();
        for (int i = joins.size() - 1; i >= 0; i--) {
            JoinExpression join = joins.get(i);
            Path source = (Path) ((Operation<?>) join.getTarget()).getArg(0);
            Path target = (Path) ((Operation<?>) join.getTarget()).getArg(1);
            Collection<Predicate> extraFilters = predicates.get(target.getRoot());
            Predicate filter = ExpressionUtils.allOf(join.getCondition(), allOf(extraFilters));
            List<Object> ids = getIds(target.getType(), filter);
            if (ids.isEmpty()) {
                throw new NoResults();
            }
            Path path = ExpressionUtils.path(String.class, source, "$id");
            predicates.put(source.getRoot(), ExpressionUtils.in(path, ids));
        }
        Path source = (Path) ((Operation) joins.get(0).getTarget()).getArg(0);
        return allOf(predicates.get(source.getRoot()));
    }

    private Predicate allOf(Collection<Predicate> predicates) {
        return predicates != null ? ExpressionUtils.allOf(predicates) : null;
    }

    protected List<Object> getIds(Class<?> targetType, Predicate condition) {
        DBCollection collection = getCollection(targetType);
        // TODO : fetch only ids
        DBCursor cursor = createCursor(collection, condition, null,
                QueryModifiers.EMPTY, Collections.<OrderSpecifier<?>>emptyList());
        if (cursor.hasNext()) {
            List<Object> ids = new ArrayList<Object>(cursor.count());
            for (DBObject obj : cursor) {
                ids.add(obj.get("_id"));
            }
            return ids;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Q distinct() {
        return queryMixin.distinct();
    }

    public Q where(Predicate e) {
        return queryMixin.where(e);
    }

    @Override
    public Q where(Predicate... e) {
        return queryMixin.where(e);
    }

    @Override
    public Q limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public Q offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public Q restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

    public Q orderBy(OrderSpecifier<?> o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public Q orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public <T> Q set(ParamExpression<T> param, T value) {
        return queryMixin.set(param, value);
    }

    /**
     * Iterate with the specific fields
     *
     * @param paths fields to return
     * @return iterator
     */
    public CloseableIterator<K> iterate(Path<?>... paths) {
        queryMixin.setProjection(paths);
        return iterate();
    }

    @Override
    public CloseableIterator<K> iterate() {
        final DBCursor cursor = createCursor();
        return new CloseableIterator<K>() {
            @Override
            public boolean hasNext() {
                return cursor.hasNext();
            }

            @Override
            public K next() {
                return transformer.apply(cursor.next());
            }

            @Override
            public void remove() {
            }

            @Override
            public void close() {
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
        queryMixin.setProjection(paths);
        return fetch();
    }

    @Override
    public List<K> fetch() {
        try {
            DBCursor cursor = createCursor();
            List<K> results = new ArrayList<K>();
            for (DBObject dbObject : cursor) {
                results.add(transformer.apply(dbObject));
            }
            return results;
        } catch (NoResults ex) {
            return Collections.emptyList();
        }
    }

    protected DBCursor createCursor() {
        QueryMetadata metadata = queryMixin.getMetadata();
        Predicate filter = createFilter(metadata);
        return createCursor(collection, filter, metadata.getProjection(), metadata.getModifiers(), metadata.getOrderBy());
    }

    protected DBCursor createCursor(DBCollection collection, @Nullable Predicate where, Expression<?> projection,
            QueryModifiers modifiers, List<OrderSpecifier<?>> orderBy) {
        DBCursor cursor = collection.find(createQuery(where), createProjection(projection));
        Integer limit = modifiers.getLimitAsInteger();
        Integer offset = modifiers.getOffsetAsInteger();
        if (limit != null) {
            cursor.limit(limit);
        }
        if (offset != null) {
            cursor.skip(offset);
        }
        if (orderBy.size() > 0) {
            cursor.sort(serializer.toSort(orderBy));
        }
        if (readPreference != null) {
            cursor.setReadPreference(readPreference);
        }
        return cursor;
    }

    private DBObject createProjection(Expression<?> projection) {
        if (projection instanceof FactoryExpression) {
            DBObject obj = new BasicDBObject();
            for (Object expr : ((FactoryExpression) projection).getArgs()) {
                if (expr instanceof Expression) {
                    obj.put((String) serializer.handle((Expression) expr), 1);
                }
            }
            return obj;
        }
        return null;
    }

    /**
     * Fetch first with the specific fields
     *
     * @param paths fields to return
     * @return first result
     */
    public K fetchFirst(Path<?>...paths) {
        queryMixin.setProjection(paths);
        return fetchFirst();
    }

    @Override
    public K fetchFirst() {
        try {
            DBCursor c = createCursor().limit(1);
            if (c.hasNext()) {
                return transformer.apply(c.next());
            } else {
                return null;
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
        queryMixin.setProjection(paths);
        return fetchOne();
    }

    @Override
    public K fetchOne() {
        try {
            Long limit = queryMixin.getMetadata().getModifiers().getLimit();
            if (limit == null) {
                limit = 2L;
            }
            DBCursor c = createCursor().limit(limit.intValue());
            if (c.hasNext()) {
                K rv = transformer.apply(c.next());
                if (c.hasNext()) {
                    throw new NonUniqueResultException();
                }
                return rv;
            } else {
                return null;
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
        queryMixin.setProjection(paths);
        return fetchResults();
    }

    @Override
    public QueryResults<K> fetchResults() {
        try {
            long total = fetchCount();
            if (total > 0L) {
                return new QueryResults<K>(fetch(), queryMixin.getMetadata().getModifiers(), total);
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
            Predicate filter = createFilter(queryMixin.getMetadata());
            return collection.count(createQuery(filter));
        } catch (NoResults ex) {
            return 0L;
        }
    }

    private DBObject createQuery(@Nullable Predicate predicate) {
        if (predicate != null) {
            return (DBObject) serializer.handle(predicate);
        } else {
            return new BasicDBObject();
        }
    }

    /**
     * Sets the read preference for this query
     *
     * @param readPreference read preference
     */
    public void setReadPreference(ReadPreference readPreference) {
        this.readPreference = readPreference;
    }

    @Override
    public String toString() {
        return createQuery(queryMixin.getMetadata().getWhere()).toString();
    }

}