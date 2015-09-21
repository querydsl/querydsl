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
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
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

    private static final Logger logger = LoggerFactory.getLogger(JPAQuery.class);

    protected final Multimap<String,Object> hints = HashMultimap.create();

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

    @Override
    public long fetchCount() {
        try {
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

    private Query createQuery(@Nullable QueryModifiers modifiers, boolean forCount) {
        JPQLSerializer serializer = serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString, serializer.getConstantToLabel());
        Query query = entityManager.createQuery(queryString);
        JPAUtil.setConstants(query, serializer.getConstantToLabel(), getMetadata().getParams());
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

        for (Map.Entry<String, Object> entry : hints.entries()) {
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
                    rv.add(null);
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
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
        try {
            Query query = createQuery();
            return (List<T>) getResultList(query);
        } finally {
            reset();
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        try {
            Query countQuery = createQuery(null, true);
            long total = (Long) countQuery.getSingleResult();
            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
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

    protected void logQuery(String queryString, Map<Object, String> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            MDC.put(MDC_QUERY, normalizedQuery);
            MDC.put(MDC_PARAMETERS, String.valueOf(parameters));
            logger.debug(normalizedQuery);
        }
    }

    protected void cleanupMDC() {
        MDC.remove(MDC_QUERY);
        MDC.remove(MDC_PARAMETERS);
    }

    @Override
    protected void reset() {
        cleanupMDC();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() {
        try {
            Query query = createQuery(getMetadata().getModifiers(), false);
            return (T) getSingleResult(query);
        } catch (javax.persistence.NoResultException e) {
            logger.trace(e.getMessage(),e);
            return null;
        } catch (javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException();
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
