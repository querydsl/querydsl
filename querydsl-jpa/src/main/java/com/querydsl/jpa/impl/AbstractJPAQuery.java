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
package com.querydsl.jpa.impl;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.jpa.JPAQueryBase;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Abstract base class for JPA API based implementations of the JPQLQuery interface
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractJPAQuery<Q extends AbstractJPAQuery<Q>> extends JPAQueryBase<Q> {

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
    public long count() {
        try {
            Query query = createQuery(null, true);
            return (Long) query.getSingleResult();
        } finally {
            reset();
        }
    }

    /**
     * Expose the original JPA querydsl for the given projection
     *
     * @param expr
     * @return
     */
    public Query createQuery(Expression<?> expr) {
        queryMixin.addProjection(expr);
        return createQuery(getMetadata().getModifiers(), false);
    }

    /**
     * Expose the original JPA querydsl for the given projection
     *
     * @param expr1
     * @param expr2
     * @param rest
     * @return
     */
    public Query createQuery(Expression<?> expr1, Expression<?> expr2, Expression<?>... rest) {
        queryMixin.addProjection(expr1);
        queryMixin.addProjection(expr2);
        queryMixin.addProjection(rest);
        return createQuery(getMetadata().getModifiers(), false);
    }

    /**
     * Expose the original JPA querydsl for the given projection
     *
     * @param args
     * @return
     */
    public Query createQuery(Expression<?>[] args) {
        queryMixin.addProjection(args);
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
                query.setMaxResults(limit.intValue());
            }
            if (offset != null) {
                query.setFirstResult(offset.intValue());
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
        List<? extends Expression<?>> projection = getMetadata().getProjection();

        FactoryExpression<?> wrapped = projection.size() > 1 ? FactoryExpressionUtils.wrap(projection) : null;

        if (!forCount && ((projection.size() == 1 && projection.get(0) instanceof FactoryExpression) || wrapped != null)) {
            Expression<?> expr = wrapped != null ? wrapped : projection.get(0);

            if (!queryHandler.transform(query, (FactoryExpression<?>)expr)) {
                this.projection = (FactoryExpression<?>)projection.get(0);
                if (wrapped != null) {
                    this.projection = wrapped;
                    getMetadata().clearProjection();
                    getMetadata().addProjection(wrapped);
                }
            }
        }

        return query;
    }

    /**
     * Transforms results using FactoryExpression if ResultTransformer can't be used
     *
     * @param query
     * @return
     */
    private List<?> getResultList(Query query) {
        // TODO : use lazy list here?
        if (projection != null) {
            List<?> results = query.getResultList();
            List<Object> rv = new ArrayList<Object>(results.size());
            for (Object o : results) {
                if (o != null) {
                    if (!o.getClass().isArray()) {
                        o = new Object[]{o};
                    }
                    rv.add(projection.newInstance((Object[])o));
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
     * @param query
     * @return
     */
    @Nullable
    private Object getSingleResult(Query query) {
        if (projection != null) {
            Object result = query.getSingleResult();
            if (result != null) {
                if (!result.getClass().isArray()) {
                    result = new Object[]{result};
                }
                return projection.newInstance((Object[])result);
            } else {
                return null;
            }
        } else {
            return query.getSingleResult();
        }
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> expr) {
        try {
            Query query = createQuery(expr);
            return queryHandler.<RT>iterate(query, projection);
        } finally {
            reset();
        }
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expression<RT> expr) {
        try {
            Query query = createQuery(expr);
            return (List<RT>) getResultList(query);
        } finally {
            reset();
        }
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(queryMixin.createProjection(args));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        try {
            queryMixin.addProjection(expr);
            Query countQuery = createQuery(null, true);
            long total = (Long) countQuery.getSingleResult();
            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
                Query query = createQuery(modifiers, false);
                @SuppressWarnings("unchecked")
                List<RT> list = (List<RT>) getResultList(query);
                return new SearchResults<RT>(list, modifiers, total);
            } else {
                return SearchResults.emptyResults();
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

    @Override
    protected void reset() {
        super.reset();
        cleanupMDC();
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        return uniqueResult();
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <RT> RT  uniqueResult() {
        try{
            Query query = createQuery(getMetadata().getModifiers(), false);
            return (RT) getSingleResult(query);
        } catch(javax.persistence.NoResultException e) {
            logger.trace(e.getMessage(),e);
            return null;
        } catch(javax.persistence.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        } finally {
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    public Q setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return (Q)this;
    }

    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
        return (Q)this;
    }

    @SuppressWarnings("unchecked")
    public Q setHint(String name, Object value) {
        hints.put(name, value);
        return (Q)this;
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
     * Clone the state of this querydsl to a new instance with the given EntityManager
     *
     * @param entityManager
     * @return
     */
    public abstract Q clone(EntityManager entityManager);

    /**
     * Clone the state of this querydsl to a new instance with the given EntityManager
     * and the specified templates
     *
     * @param entityManager
     * @param templates
     * @return
     */
    public abstract Q clone(EntityManager entityManager, JPQLTemplates templates);

    /**
     * Clone the state of this querydsl to a new instance
     *
     * @return
     */
    public Q clone() {
        return clone(entityManager, getTemplates());
    }

}
