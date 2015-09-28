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
package com.querydsl.jpa.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.AbstractSQLQuery;
import com.querydsl.jpa.NativeSQLSerializer;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.jpa.impl.JPAProvider;
import com.querydsl.jpa.impl.JPAUtil;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;

/**
 * {@code AbstractJPASQLQuery} is the base class for JPA Native SQL queries
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @author tiwe*
 */
public abstract class AbstractJPASQLQuery<T, Q extends AbstractJPASQLQuery<T, Q>> extends AbstractSQLQuery<T, Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractJPASQLQuery.class);

    private final EntityManager entityManager;

    protected final Multimap<String,Object> hints = HashMultimap.create();

    protected final QueryHandler queryHandler;

    @Nullable
    protected LockModeType lockMode;

    @Nullable
    protected FlushModeType flushMode;

    @Nullable

    protected FactoryExpression<?> projection;

    public AbstractJPASQLQuery(EntityManager em, Configuration configuration) {
        this(em, configuration, new DefaultQueryMetadata());
    }

    public AbstractJPASQLQuery(EntityManager em, Configuration configuration, QueryHandler queryHandler) {
        this(em, configuration, queryHandler, new DefaultQueryMetadata());
    }

    public AbstractJPASQLQuery(EntityManager em, Configuration configuration, QueryMetadata metadata) {
        this(em, configuration, JPAProvider.getTemplates(em).getQueryHandler(), metadata);
    }

    public AbstractJPASQLQuery(EntityManager em, Configuration configuration, QueryHandler queryHandler, QueryMetadata metadata) {
        super(metadata, configuration);
        this.entityManager = em;
        this.queryHandler = queryHandler;
    }

    public Query createQuery() {
        return createQuery(false);
    }

    private Query createQuery(boolean forCount) {
        NativeSQLSerializer serializer = (NativeSQLSerializer) serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString, serializer.getConstantToLabel());
        Expression<?> projection = queryMixin.getMetadata().getProjection();
        Query query;

        if (!FactoryExpression.class.isAssignableFrom(projection.getClass()) && isEntityExpression(projection)) {
            if (queryHandler.createNativeQueryTyped()) {
                query = entityManager.createNativeQuery(queryString, projection.getType());
            } else {
                query = entityManager.createNativeQuery(queryString);
            }

        } else {
            query = entityManager.createNativeQuery(queryString);
        }
        if (!forCount) {
            ListMultimap<Expression<?>, String> aliases = serializer.getAliases();
            Set<String> used = Sets.newHashSet();
            if (projection instanceof FactoryExpression) {
                for (Expression<?> expr : ((FactoryExpression<?>) projection).getArgs()) {
                    if (isEntityExpression(expr)) {
                        queryHandler.addEntity(query, extractEntityExpression(expr).toString(), expr.getType());
                    } else if (aliases.containsKey(expr)) {
                        for (String scalar : aliases.get(expr)) {
                            if (!used.contains(scalar)) {
                                queryHandler.addScalar(query, scalar, expr.getType());
                                used.add(scalar);
                                break;
                            }

                        }
                    }
                }
            } else if (isEntityExpression(projection)) {
                queryHandler.addEntity(query, extractEntityExpression(projection).toString(), projection.getType());
            } else if (aliases.containsKey(projection)) {
                for (String scalar : aliases.get(projection)) {
                    if (!used.contains(scalar)) {
                        queryHandler.addScalar(query, scalar, projection.getType());
                        used.add(scalar);
                        break;
                    }
                }
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


        // set constants
        JPAUtil.setConstants(query, serializer.getConstantToLabel(), queryMixin.getMetadata().getParams());
        this.projection = null; // necessary when query is reused

        if (projection instanceof FactoryExpression) {
            if (!queryHandler.transform(query, (FactoryExpression<?>) projection)) {
                this.projection = (FactoryExpression<?>) projection;
            }
        }

        return query;
    }

    @Override
    protected SQLSerializer createSerializer() {
        return new NativeSQLSerializer(configuration, queryHandler.wrapEntityProjections());
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
                    Object[] arr;
                    if (!o.getClass().isArray()) {
                        arr = new Object[]{o};
                    } else {
                        arr = (Object[]) o;
                    }
                    if (projection.getArgs().size() < arr.length) {
                        Object[] shortened = new Object[projection.getArgs().size()];
                        System.arraycopy(arr, 0, shortened, 0, shortened.length);
                        arr = shortened;
                    }
                    rv.add(projection.newInstance(arr));
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

    @SuppressWarnings("unchecked")
    @Override
    public List<T> fetch() {
        try {
            Query query = createQuery();
            return (List<T>) getResultList(query);
        } finally {
            reset();
        }
    }

    @Override
    public CloseableIterator<T> iterate() {
        try {
            Query query = createQuery();
            return queryHandler.iterate(query, null);
        } finally {
            reset();
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        // TODO : handle entity projections as well
        try {
            Query query = createQuery(true);
            long total = ((Number) query.getSingleResult()).longValue();
            if (total > 0) {
                QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
                query = createQuery(false);
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
        String normalizedQuery = queryString.replace('\n', ' ');
        MDC.put(MDC_QUERY, normalizedQuery);
        MDC.put(MDC_PARAMETERS, String.valueOf(parameters));
        if (logger.isDebugEnabled()) {
            logger.debug(normalizedQuery);
        }
    }

    protected void cleanupMDC() {
        MDC.remove(MDC_QUERY);
        MDC.remove(MDC_PARAMETERS);
    }

    protected void reset() {
        cleanupMDC();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T fetchOne() {
        Query query = createQuery();
        return (T) uniqueResult(query);
    }

    @Nullable
    private Object uniqueResult(Query query) {
        try {
            return getSingleResult(query);
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
    protected void clone(Q query) {
        super.clone(query);
        flushMode = query.flushMode;
        hints.putAll(query.hints);
        lockMode = query.lockMode;
        projection = query.projection;
    }

    public abstract Q clone(EntityManager entityManager);

    @Override
    public Q clone() {
        return this.clone(this.entityManager);
    }

}
