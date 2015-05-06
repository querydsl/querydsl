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
package com.querydsl.jpa.hibernate.sql;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.jpa.AbstractSQLQuery;
import com.querydsl.jpa.FactoryExpressionTransformer;
import com.querydsl.jpa.NativeSQLSerializer;
import com.querydsl.jpa.ScrollableResultsIterator;
import com.querydsl.jpa.hibernate.DefaultSessionHolder;
import com.querydsl.jpa.hibernate.HibernateUtil;
import com.querydsl.jpa.hibernate.SessionHolder;
import com.querydsl.jpa.hibernate.StatelessSessionHolder;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLSerializer;

/**
 * {@code AbstractHibernateSQLQuery} is the base class for Hibernate Native SQL queries
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractHibernateSQLQuery<T, Q extends AbstractHibernateSQLQuery<T, Q>> extends AbstractSQLQuery<T, Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHibernateSQLQuery.class);

    protected Boolean cacheable, readOnly;

    protected String cacheRegion;

    protected int fetchSize = 0;

    private final SessionHolder session;

    protected int timeout = 0;

    public AbstractHibernateSQLQuery(Session session, Configuration conf) {
        this(new DefaultSessionHolder(session), conf, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(StatelessSession session, Configuration conf) {
        this(new StatelessSessionHolder(session), conf, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(SessionHolder session, Configuration conf, QueryMetadata metadata) {
        super(metadata, conf);
        this.session = session;
    }

    public Query createQuery() {
        return createQuery(false);
    }

    private Query createQuery(boolean forCount) {
        NativeSQLSerializer serializer = (NativeSQLSerializer) serialize(forCount);
        String queryString = serializer.toString();
        logQuery(queryString, serializer.getConstantToLabel());
        org.hibernate.SQLQuery query = session.createSQLQuery(queryString);
        // set constants
        HibernateUtil.setConstants(query, serializer.getConstantToLabel(), queryMixin.getMetadata().getParams());

        if (!forCount) {
            ListMultimap<Expression<?>, String> aliases = serializer.getAliases();
            Set<String> used = Sets.newHashSet();
            // set entity paths
            Expression<?> projection = queryMixin.getMetadata().getProjection();
            if (projection instanceof FactoryExpression) {
                for (Expression<?> expr : ((FactoryExpression<?>)projection).getArgs()) {
                    if (isEntityExpression(expr)) {
                        query.addEntity(extractEntityExpression(expr).toString(), expr.getType());
                    } else if (aliases.containsKey(expr)) {
                        for (String scalar : aliases.get(expr)) {
                            if (!used.contains(scalar)) {
                                query.addScalar(scalar);
                                used.add(scalar);
                                break;
                            }
                        }
                    }
                }
            } else if (isEntityExpression(projection)) {
                query.addEntity(extractEntityExpression(projection).toString(), projection.getType());
            } else if (aliases.containsKey(projection)) {
                for (String scalar : aliases.get(projection)) {
                    if (!used.contains(scalar)) {
                        query.addScalar(scalar);
                        used.add(scalar);
                        break;
                    }
                }
            }

            // set result transformer, if projection is a FactoryExpression instance
            if (projection instanceof FactoryExpression) {
                query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) projection));
            }
        }

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
        if (readOnly != null) {
            query.setReadOnly(readOnly);
        }
        return query;
    }

    @Override
    protected SQLSerializer createSerializer() {
        return new NativeSQLSerializer(configuration, true);
    }

    @Override
    public List<T> fetch() {
        try {
            return createQuery().list();
        } finally {
            reset();
        }
    }

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
    public QueryResults<T> fetchResults() {
        // TODO : handle entity projections as well
        try {
            Query query = createQuery(true);
            long total = ((Number)query.uniqueResult()).longValue();
            if (total > 0) {
                QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
                query = createQuery(false);
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
        queryMixin.getMetadata().reset();
        cleanupMDC();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T fetchOne() {
        try {
            Query query = createQuery();
            return (T)uniqueResult(query);
        } finally {
            reset();
        }
    }

    @Nullable
    private Object uniqueResult(Query query) {
        try{
            return query.uniqueResult();
        }catch (org.hibernate.NonUniqueResultException e) {
            throw new NonUniqueResultException();
        }
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
     * Set a fetchJoin size for the underlying JDBC query.
     * @param fetchSize the fetchJoin size
     */
    @SuppressWarnings("unchecked")
    public Q setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
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
    
    protected void clone(Q query) {
        super.clone(query);
        cacheable = query.cacheable;
        cacheRegion = query.cacheRegion;
        fetchSize = query.fetchSize;
        readOnly = query.readOnly;
        timeout = query.timeout;
    }
    
    protected abstract Q clone(SessionHolder session);

    public Q clone(Session session) {
        return this.clone(new DefaultSessionHolder(session));
    }   
    
    public Q clone(StatelessSession statelessSession) {
        return this.clone(new StatelessSessionHolder(statelessSession));
    }

    @Override
    public Q clone() {
        return this.clone(this.session);
    }

}
