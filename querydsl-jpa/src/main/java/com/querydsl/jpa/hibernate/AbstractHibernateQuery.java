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
package com.querydsl.jpa.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
import com.querydsl.core.types.Path;
import com.querydsl.jpa.*;

/**
 * Abstract base class for Hibernate API based implementations of the JPQL interface
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @author tiwe
 */
public abstract class AbstractHibernateQuery<T, Q extends AbstractHibernateQuery<T, Q>> extends JPAQueryBase<T, Q> {

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
    public long fetchCount() {
        QueryModifiers modifiers = getMetadata().getModifiers();
        try {
            Query query = createQuery(modifiers, true);
            Long rv = (Long) query.uniqueResult();
            if (rv != null) {
                return rv;
            } else {
                throw new QueryException("Query returned null");
            }
        } finally {
            reset();
        }
    }

    /**
     * Expose the original Hibernate query for the given projection
     *
     * @return query
     */
    public Query createQuery() {
        return createQuery(getMetadata().getModifiers(), false);
    }

    private Query createQuery(@Nullable QueryModifiers modifiers, boolean forCount) {
        JPQLSerializer serializer = serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString, serializer.getConstantToAllLabels());
        Query query = session.createQuery(queryString);
        HibernateUtil.setConstants(query, serializer.getConstantToNamedLabel(), serializer.getConstantToNumberedLabel(),
                getMetadata().getParams());
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
                query.setMaxResults(limit);
            }
            if (offset != null) {
                query.setFirstResult(offset);
            }
        }

        // set transformer, if necessary
        Expression<?> projection = getMetadata().getProjection();
        if (!forCount && projection instanceof FactoryExpression) {
            query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) projection));
        }
        return query;
    }


    /**
     * Return the query results as an <tt>Iterator</tt>. If the query
     * contains multiple results pre row, the results are returned in
     * an instance of <tt>Object[]</tt>.<br>
     * <br>
     * Entities returned as results are initialized on demand. The first
     * SQL query returns identifiers only.<br>
     */
    @Override
    public CloseableIterator<T> iterate() {
        try {
            Query query = createQuery();
            ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
            return new ScrollableResultsIterator<T>(results);
        } finally {
            reset();
        }
    }

    @Override
    public Stream<T> stream() {
        try {
            Query query = createQuery();
            return query.getResultStream();
        } finally {
            reset();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> fetch() {
        try {
            return createQuery().list();
        } finally {
            reset();
        }
    }

    @Override
    public QueryResults<T> fetchResults() {
        try {
            Query countQuery = createQuery(null, true);
            long total = (Long) countQuery.uniqueResult();

            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
                Query query = createQuery(modifiers, false);
                @SuppressWarnings("unchecked")
                List<T> list = query.list();
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

    /**
     * Return the query results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     *
     * @param mode scroll mode
     * @return scrollable results
     */
    public ScrollableResults scroll(ScrollMode mode) {
        try {
            return createQuery().scroll(mode);
        } finally {
            reset();
        }
    }

    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    @SuppressWarnings("unchecked")
    public Q setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return (Q) this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
     */
    @SuppressWarnings("unchecked")
    public Q setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
        return (Q) this;
    }

    /**
     * Add a comment to the generated SQL.
     * @param comment comment
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public Q setComment(String comment) {
        this.comment = comment;
        return (Q) this;
    }

    /**
     * Set a fetchJoin size for the underlying JDBC query.
     * @param fetchSize the fetchJoin size
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public Q setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return (Q) this;
    }

    /**
     * Set the lock mode for the given path.
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public Q setLockMode(Path<?> path, LockMode lockMode) {
        lockModes.put(path, lockMode);
        return (Q) this;
    }

    /**
     * Override the current session flush mode, just for this query.
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushMode flushMode) {
        this.flushMode = flushMode;
        return (Q) this;
    }

    /**
     * Entities retrieved by this query will be loaded in
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     * @return the current object
     *
     */
    @SuppressWarnings("unchecked")
    public Q setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return (Q) this;
    }

    /**
     * Set a timeout for the underlying JDBC query.
     * @param timeout the timeout in seconds
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public Q setTimeout(int timeout) {
        this.timeout = timeout;
        return (Q) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() throws NonUniqueResultException {
        try {
            QueryModifiers modifiers = getMetadata().getModifiers();
            Query query = createQuery(modifiers, false);
            try {
                return (T) query.uniqueResult();
            } catch (org.hibernate.NonUniqueResultException e) {
                throw new NonUniqueResultException(e);
            }
        } finally {
            reset();
        }
    }

    @Override
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
     * Clone the state of this query to a new instance with the given Session
     *
     * @param session session
     * @return cloned query
     */
    public Q clone(Session session) {
        return this.clone(new DefaultSessionHolder(session));
    }

    /**
     * Clone the state of this query to a new instance with the given StatelessSession
     *
     * @param session session
     * @return cloned query
     */
    public Q clone(StatelessSession session) {
        return this.clone(new StatelessSessionHolder(session));
    }

    /**
     * Clone the state of this query to a new instance
     *
     * @return closed query
     */
    @Override
    public Q clone() {
        return this.clone(this.session);
    }

}
