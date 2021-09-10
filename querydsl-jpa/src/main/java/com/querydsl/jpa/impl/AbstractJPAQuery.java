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
package com.querydsl.jpa.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.QueryHandler;

/**
 * Abstract base class for JPA API based implementations of the JPQLQuery interface
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @author tiwe
 */
public abstract class AbstractJPAQuery<T, Q extends AbstractJPAQuery<T, Q>> extends JPAQueryBase<T, Q> {

    private static final Logger logger = Logger.getLogger(JPAQuery.class.getName());

    protected final Map<String, Object> hints = new LinkedHashMap<>();

    protected final EntityManager entityManager;

    protected final QueryHandler queryHandler;

    @Nullable
    protected LockModeType lockMode;

    @Nullable
    protected FlushModeType flushMode;

    @Nullable
    protected FactoryExpression<?> projection;

    public AbstractJPAQuery(EntityManager em) {
        this(em, JPAProvider.getTemplates(em), new DefaultQueryMetadata());
    }

    public AbstractJPAQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
        super(metadata, templates);
        this.queryHandler = templates.getQueryHandler();
        this.entityManager = em;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated {@code fetchCount} requires a count query to be computed. In {@code querydsl-sql}, this is done
     * by wrapping the query in a subquery, like so: {@code SELECT COUNT(*) FROM (&lt;original query&gt;)}. Unfortunately,
     * JPQL - the query language of JPA - does not allow queries to project from subqueries. As a result there isn't a
     * universal way to express count queries in JPQL. Historically QueryDSL attempts at producing a modified query
     * to compute the number of results instead.
     *
     * However, this approach only works for simple queries. Specifically
     * queries with multiple group by clauses and queries with a having clause turn out to be problematic. This is because
     * {@code COUNT(DISTINCT a, b, c)}, while valid SQL in most dialects, is not valid JPQL. Furthermore, a having
     * clause may refer select elements or aggregate functions and therefore cannot be emulated by moving the predicate
     * to the where clause instead.
     *
     * In order to support {@code fetchCount} for queries with multiple group by elements or a having clause, we
     * generate the count in memory instead. This means that the method simply falls back to returning the size of
     * {@link #fetch()}. For large result sets this may come at a severe performance penalty.
     *
     * For very specific domain models where {@link #fetchCount()} has to be used in conjunction with complex queries
     * containing multiple group by elements and/or a having clause, we recommend using the
     * <a href="https://persistence.blazebit.com/documentation/1.5/core/manual/en_US/index.html#querydsl-integration">Blaze-Persistence</a>
     * integration for QueryDSL. Among other advanced query features, Blaze-Persistence makes it possible to select
     * from subqueries in JPQL. As a result the {@code BlazeJPAQuery} provided with the integration, implements
     * {@code fetchCount} properly and always executes a proper count query.
     */
    @Override
    @Deprecated
    public long fetchCount() {
        try {
            if (getMetadata().getGroupBy().size() > 1 || getMetadata().getHaving() != null) {
                logger.warning("Fetchable#fetchCount() was computed in memory! See the Javadoc for AbstractJPAQuery#fetchCount for more details.");
                Query query = createQuery(null, false);
                return query.getResultList().size();
            }

            Query query = createQuery(null, true);
            return (Long) query.getSingleResult();
        } finally {
            reset();
        }
    }

    /**
     * Expose the original JPA query for the given projection
     *
     * @return query
     */
    public Query createQuery() {
        return createQuery(getMetadata().getModifiers(), false);
    }

    protected Query createQuery(@Nullable QueryModifiers modifiers, boolean forCount) {
        JPQLSerializer serializer = serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString);
        Query query = entityManager.createQuery(queryString);
        JPAUtil.setConstants(query, serializer.getConstants(), getMetadata().getParams());
        if (modifiers != null && modifiers.isRestricting()) {
            Integer limit = modifiers.getLimitAsInteger();
            Integer offset = modifiers.getOffsetAsInteger();
            if (limit != null) {
                query.setMaxResults(limit);
            }
            if (offset != null) {
                query.setFirstResult(offset);
            }
        }
        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        if (flushMode != null) {
            query.setFlushMode(flushMode);
        }

        for (Map.Entry<String, Object> entry : hints.entrySet()) {
            query.setHint(entry.getKey(), entry.getValue());
        }

        // set transformer, if necessary and possible
        Expression<?> projection = getMetadata().getProjection();
        this.projection = null; // necessary when query is reused

        if (!forCount && projection instanceof FactoryExpression) {
            if (!queryHandler.transform(query, (FactoryExpression<?>) projection)) {
                this.projection = (FactoryExpression) projection;
            }
        }

        return query;
    }

    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     *
     * @param query query
     * @return results
     */
    private List<?> getResultList(Query query) {
        // TODO : use lazy fetch here?
        if (projection != null) {
            List<?> results = query.getResultList();
            List<Object> rv = new ArrayList<Object>(results.size());
            for (Object o : results) {
                if (o != null) {
                    if (!o.getClass().isArray()) {
                        o = new Object[]{o};
                    }
                    rv.add(projection.newInstance((Object[]) o));
                } else {
                    rv.add(projection.newInstance(new Object[] {null}));
                }
            }
            return rv;
        } else {
            return query.getResultList();
        }
    }

    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     *
     * @param query query
     * @return single result
     */
    @Nullable
    private Object getSingleResult(Query query) {
        if (projection != null) {
            Object result = query.getSingleResult();
            if (result != null) {
                if (!result.getClass().isArray()) {
                    result = new Object[]{result};
                }
                return projection.newInstance((Object[]) result);
            } else {
                return null;
            }
        } else {
            return query.getSingleResult();
        }
    }

    @Override
    public CloseableIterator<T> iterate() {
        try {
            Query query = createQuery();
            return queryHandler.iterate(query, projection);
        } finally {
            reset();
        }
    }

    @Override
    public Stream<T> stream() {
        try {
            Query query = createQuery();
            return queryHandler.stream(query, projection);
        } finally {
            reset();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
        try {
            Query query = createQuery();
            return (List<T>) getResultList(query);
        } finally {
            reset();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated {@code fetchResults} requires a count query to be computed. In {@code querydsl-sql}, this is done
     * by wrapping the query in a subquery, like so: {@code SELECT COUNT(*) FROM (&lt;original query&gt;)}. Unfortunately,
     * JPQL - the query language of JPA - does not allow queries to project from subqueries. As a result there isn't a
     * universal way to express count queries in JPQL. Historically QueryDSL attempts at producing a modified query
     * to compute the number of results instead.
     *
     * However, this approach only works for simple queries. Specifically
     * queries with multiple group by clauses and queries with a having clause turn out to be problematic. This is because
     * {@code COUNT(DISTINCT a, b, c)}, while valid SQL in most dialects, is not valid JPQL. Furthermore, a having
     * clause may refer select elements or aggregate functions and therefore cannot be emulated by moving the predicate
     * to the where clause instead.
     *
     * In order to support {@code fetchResults} for queries with multiple group by elements or a having clause, we
     * generate the count in memory instead. This means that the method simply falls back to returning the size of
     * {@link #fetch()}. For large result sets this may come at a severe performance penalty.
     *
     * For very specific domain models where {@link #fetchResults()} has to be used in conjunction with complex queries
     * containing multiple group by elements and/or a having clause, we recommend using the
     * <a href="https://persistence.blazebit.com/documentation/1.5/core/manual/en_US/index.html#querydsl-integration">Blaze-Persistence</a>
     * integration for QueryDSL. Among other advanced query features, Blaze-Persistence makes it possible to select
     * from subqueries in JPQL. As a result the {@code BlazeJPAQuery} provided with the integration, implements
     * {@code fetchResults} properly and always executes a proper count query.
     *
     * Mind that for any scenario where the count is not strictly needed separately, we recommend to use {@link #fetch()}
     * instead.
     */
    @Override
    @Deprecated
    public QueryResults<T> fetchResults() {
        try {
            QueryModifiers modifiers = getMetadata().getModifiers();
            if (getMetadata().getGroupBy().size() > 1 || getMetadata().getHaving() != null) {
                logger.warning("Fetchable#fetchResults() was computed in memory! See the Javadoc for AbstractJPAQuery#fetchResults for more details.");
                Query query = createQuery(null, false);
                @SuppressWarnings("unchecked")
                List<T> resultList = query.getResultList();
                int offset = modifiers.getOffsetAsInteger() == null ? 0 : modifiers.getOffsetAsInteger();
                int limit = modifiers.getLimitAsInteger() == null ? resultList.size() : modifiers.getLimitAsInteger();
                return new QueryResults<T>(resultList.subList(offset, Math.min(resultList.size(), offset + limit)), modifiers, resultList.size());
            }

            Query countQuery = createQuery(null, true);
            long total = (Long) countQuery.getSingleResult();
            if (total > 0) {
                Query query = createQuery(modifiers, false);
                @SuppressWarnings("unchecked")
                List<T> list = (List<T>) getResultList(query);
                return new QueryResults<T>(list, modifiers, total);
            } else {
                return QueryResults.emptyResults();
            }
        } finally {
            reset();
        }

    }

    protected void logQuery(String queryString) {
        if (logger.isLoggable(Level.FINEST)) {
            String normalizedQuery = queryString.replace('\n', ' ');
            logger.finest(normalizedQuery);
        }
    }

    @Override
    protected void reset() {
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() throws NonUniqueResultException {
        try {
            Query query = createQuery(getMetadata().getModifiers(), false);
            return (T) getSingleResult(query);
        } catch (javax.persistence.NoResultException e) {
            logger.log(Level.FINEST, e.getMessage(), e);
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException(e);
        } finally {
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    public Q setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return (Q) this;
    }

    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
        return (Q) this;
    }

    @SuppressWarnings("unchecked")
    public Q setHint(String name, Object value) {
        hints.put(name, value);
        return (Q) this;
    }

    @Override
    protected JPQLSerializer createSerializer() {
        return new JPQLSerializer(getTemplates(), entityManager);
    }

    protected void clone(Q query) {
        projection = query.projection;
        flushMode = query.flushMode;
        hints.putAll(query.hints);
        lockMode = query.lockMode;
    }

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     *
     * @param entityManager entity manager
     * @return cloned query
     */
    public abstract Q clone(EntityManager entityManager);

    /**
     * Clone the state of this query to a new instance with the given EntityManager
     * and the specified templates
     *
     * @param entityManager entity manager
     * @param templates templates
     * @return cloned query
     */
    public abstract Q clone(EntityManager entityManager, JPQLTemplates templates);

    /**
     * Clone the state of this query to a new instance
     *
     * @return cloned query
     */
    @Override
    public Q clone() {
        return clone(entityManager, getTemplates());
    }

}
