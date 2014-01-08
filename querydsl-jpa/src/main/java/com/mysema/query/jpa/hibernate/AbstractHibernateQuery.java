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
package com.mysema.query.jpa.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.FlushMode;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.FactoryExpressionTransformer;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPAQueryBase;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.ScrollableResultsIterator;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;
import com.mysema.query.types.Path;

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
        super(metadata, patterns, null);
        this.session = session;
    }

    @Override
    public long count() {
        QueryModifiers modifiers = getMetadata().getModifiers();
        String queryString = toCountRowsString();
        logQuery(queryString);
        Query query = createQuery(queryString, modifiers, true);
        reset();
        Long rv = (Long)query.uniqueResult();
        if (rv != null) {
            return rv.longValue();
        } else {
            throw new QueryException("Query returned null");
        }
    }

    /**
     * Expose the original Hibernate query for the given projection
     *
     * @param expr
     * @return
     */
    public Query createQuery(Expression<?> expr) {
        queryMixin.addProjection(expr);
        String queryString = toQueryString();
        return createQuery(queryString, getMetadata().getModifiers(), false);
    }

    /**
     * Expose the original Hibernate query for the given projection
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
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers(), false);
    }

    /**
     * Expose the original Hibernate query for the given projection
     *
     * @param args
     * @return
     */
    public Query createQuery(Expression<?>[] args) {
        queryMixin.addProjection(args);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers(), false);
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers, boolean forCount) {
        Query query = session.createQuery(queryString);
        HibernateUtil.setConstants(query, getConstants(), getMetadata().getParams());
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
     * Return the query results as an <tt>Iterator</tt>.<br>
     * <br>
     * Entities returned as results are initialized on demand. The first
     * SQL query returns identifiers only.<br>
     */
    @Override
    @SuppressWarnings("unchecked")
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
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
    @SuppressWarnings("unchecked")
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        return new ScrollableResultsIterator<RT>(results);
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(queryMixin.createProjection(args));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expression<RT> expr) {
        Query query = createQuery(expr);
        reset();
        return query.list();
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(queryMixin.createProjection(args));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
        queryMixin.addProjection(expr);
        Query countQuery = createQuery(toCountRowsString(), null, true);
        long total = (Long) countQuery.uniqueResult();
        try{
            if (total > 0) {
                QueryModifiers modifiers = getMetadata().getModifiers();
                String queryString = toQueryString();
                logQuery(queryString);
                Query query = createQuery(queryString, modifiers, false);
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

    protected void logQuery(String queryString) {
        if (logger.isDebugEnabled()) {
            logger.debug(queryString.replace('\n', ' '));
        }
    }

    /**
     * Return the query results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     *
     * @param mode
     * @param expr
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expression<?> expr) {
        Query query = createQuery(expr);
        reset();
        return query.scroll(mode);
    }

    /**
     * Return the query results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     *
     * @param mode
     * @param args
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expression<?>... args) {
        Query query = createQuery(args);
        reset();
        return query.scroll(mode);
    }

    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    @SuppressWarnings("unchecked")
    public Q setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return (Q)this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
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
     * Set a fetch size for the underlying JDBC query.
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
     * Override the current session flush mode, just for this query.
     */
    @SuppressWarnings("unchecked")
    public Q setFlushMode(FlushMode flushMode) {
        this.flushMode = flushMode;
        return (Q)this;
    }

    /**
     * Entities retrieved by this query will be loaded in
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
     * Set a timeout for the underlying JDBC query.
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
        QueryModifiers modifiers = getMetadata().getModifiers();
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, modifiers, false);
        reset();
        try{
            return query.uniqueResult();
        } catch (org.hibernate.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        }
    }

}
