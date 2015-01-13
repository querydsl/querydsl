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
package com.querydsl.jpa.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.*;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.*;

/**
 * Abstract base class for Hibernate API based implementations of the JPQL interface
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractHibernateQuery<Q extends AbstractHibernateQuery<Q>> extends JPAQueryBase<Q> {

    private static final Logger logger = LoggerFactory.getLogger(HibernateQuery.class);

    @Nullable
    protected Boolean cacheable, readOnly;

    @Nullable
    protected String cacheRegion, comment;

    protected int fetchSize = 0;

    protected final Map<Path<?>,LockMode> lockModes = new HashMap<Path<?>,LockMode>();

    @Nullable
    protected FlushMode flushMode;

    private final SessionHolder session;

    protected int timeout = 0;

    public AbstractHibernateQuery(Session session) {
        this(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public AbstractHibernateQuery(SessionHolder session, JPQLTemplates patterns, QueryMetadata metadata) {
        super(metadata, patterns);
        this.session = session;
    }

    @Override
    public long count() {
        QueryModifiers modifiers = getMetadata().getModifiers();
        try {
            Query query = createQuery(modifiers, true);
            Long rv = (Long)query.uniqueResult();
            if (rv != null) {
                return rv.longValue();
            } else {
                throw new QueryException("Query returned null");
            }
        } finally {
            reset();
        }
    }

    /**
     * Expose the original Hibernate querydsl for the given projection
     *
     * @param expr
     * @return
     */
    public Query createQuery(Expression<?> expr) {
        queryMixin.addProjection(expr);
        return createQuery(getMetadata().getModifiers(), false);
    }

    /**
     * Expose the original Hibernate querydsl for the given projection
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
     * Expose the original Hibernate querydsl for the given projection
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
        Query query = session.createQuery(queryString);
        HibernateUtil.setConstants(query, serializer.getConstantToLabel(), getMetadata().getParams());
        if (fetchSize > 0) {
            query.setFetchSize(fetchSize);
        }
        if (timeout > 0) {
            query.setTimeout(timeout);
        }
        if (cacheable != null) {
            query.setCacheable(cacheable);
        }
        if (cacheRegion != null) {
            query.setCacheRegion(cacheRegion);
        }
        if (comment != null) {
            query.setComment(comment);
        }
        if (readOnly != null) {
            query.setReadOnly(readOnly);
        }
        for (Map.Entry<Path<?>, LockMode> entry : lockModes.entrySet()) {
            query.setLockMode(entry.getKey().toString(), entry.getValue());
        }
        if (flushMode != null) {
            query.setFlushMode(flushMode);
        }

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

        // set transformer, if necessary
        List<? extends Expression<?>> projection = getMetadata().getProjection();
        if (projection.size() == 1 && !forCount) {
            Expression<?> expr = projection.get(0);
            if (expr instanceof FactoryExpression<?>) {
                query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) projection.get(0)));
            }
        } else if (!forCount) {
            FactoryExpression<?> proj = FactoryExpressionUtils.wrap(projection);
            if (proj != null) {
                query.setResultTransformer(new FactoryExpressionTransformer(proj));
            }
        }
        return query;
    }

    /**
     * Return the querydsl results as an <tt>Iterator</tt>.<br>
     * <br>
     * Entities returned as results are initialized on demand. The first
     * SQL querydsl returns identifiers only.<br>
     */
    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
    }

    /**
     * Return the querydsl results as an <tt>Iterator</tt>. If the querydsl
     * contains multiple results pre row, the results are returned in
     * an instance of <tt>Object[]</tt>.<br>
     * <br>
     * Entities returned as results are initialized on demand. The first
     * SQL querydsl returns identifiers only.<br>
     */
    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        try {
            Query query = createQuery(projection);
            ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
            return new ScrollableResultsIterator<RT>(results);
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
            return createQuery(expr).list();
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
        try{
            queryMixin.addProjection(expr);
            Query countQuery = createQuery(null, true);
            long total = (Long) countQuery.uniqueResult();

            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
                Query query = createQuery(modifiers, false);
                @SuppressWarnings("unchecked")
                List<RT> list = query.list();
                return new SearchResults<RT>(list, modifiers, total);
            } else {
                return SearchResults.emptyResults();
            }
        }finally{
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
        super.reset();
        cleanupMDC();
    }

    /**
     * Return the querydsl results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     *
     * @param mode
     * @param expr
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expression<?> expr) {
        try {
            return createQuery(expr).scroll(mode);
        } finally {
            reset();
        }
    }

    /**
     * Return the querydsl results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     *
     * @param mode
     * @param args
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expression<?>... args) {
        try {
            return createQuery(args).scroll(mode);
        } finally {
            reset();
        }
    }

    /**
     * Enable caching of this querydsl result set.
     * @param cacheable Should the querydsl results be cacheable?
     */
    @SuppressWarnings("unchecked")
    public Q setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return (Q)this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a querydsl cache region, or <tt>null</tt>
     * for the default querydsl cache
     */
    @SuppressWarnings("unchecked")
    public Q setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
        return (Q)this;
    }

    /**
     * Add a comment to the generated SQL.
     * @param comment
     * @return
     */
    @SuppressWarnings("unchecked")
    public Q setComment(String comment) {
        this.comment = comment;
        return (Q)this;
    }

    /**
     * Set a fetch size for the underlying JDBC querydsl.
     * @param fetchSize the fetch size
     */
    @SuppressWarnings("unchecked")
    public Q setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return (Q)this;
    }

    /**
     * Set the lock mode for the given path.
     */
    @SuppressWarnings("unchecked")
    public Q setLockMode(Path<?> path, LockMode lockMode) {
        lockModes.put(path, lockMode);
        return (Q)this;
    }

    /**
     * Override the current session flush mode, just for this querydsl.
     */
    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushMode flushMode) {
        this.flushMode = flushMode;
        return (Q)this;
    }

    /**
     * Entities retrieved by this querydsl will be loaded in
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     */
    @SuppressWarnings("unchecked")
    public Q setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return (Q)this;
    }

    /**
     * Set a timeout for the underlying JDBC querydsl.
     * @param timeout the timeout in seconds
     */
    @SuppressWarnings("unchecked")
    public Q setTimeout(int timeout) {
        this.timeout = timeout;
        return (Q)this;
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        return (RT)uniqueResult();
    }

    private Object uniqueResult() {
        try {
            QueryModifiers modifiers = getMetadata().getModifiers();
            Query query = createQuery(modifiers, false);
            try{
                return query.uniqueResult();
            } catch (org.hibernate.NonUniqueResultException e) {
                throw new NonUniqueResultException();
            }
        } finally {
            reset();
        }
    }
    
    protected JPQLSerializer createSerializer() {
        return new JPQLSerializer(getTemplates());
    }
    
    protected void clone(Q query) {
        cacheable = query.cacheable;
        cacheRegion = query.cacheRegion;
        fetchSize = query.fetchSize;
        flushMode = query.flushMode;
        lockModes.putAll(query.lockModes);
        readOnly = query.readOnly;
        timeout = query.timeout;
    }
    
    protected abstract Q clone(SessionHolder sessionHolder);

    /**
     * Clone the state of this querydsl to a new instance with the given Session
     *
     * @param session
     * @return
     */
    public Q clone(Session session) {
        return this.clone(new DefaultSessionHolder(session));
    }

    /**
     * Clone the state of this querydsl to a new instance with the given StatelessSession
     *
     * @param session
     * @return
     */
    public Q clone(StatelessSession session) {
        return this.clone(new StatelessSessionHolder(session));
    }
    
    /**
     * Clone the state of this querydsl to a new instance
     *
     * @return
     */
    public Q clone() {
        return this.clone(this.session);
    }

}
