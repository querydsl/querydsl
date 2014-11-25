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
package com.mysema.query.jpa.hibernate.sql;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.*;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.FactoryExpressionTransformer;
import com.mysema.query.jpa.NativeSQLSerializer;
import com.mysema.query.jpa.ScrollableResultsIterator;
import com.mysema.query.jpa.hibernate.DefaultSessionHolder;
import com.mysema.query.jpa.hibernate.HibernateUtil;
import com.mysema.query.jpa.hibernate.SessionHolder;
import com.mysema.query.jpa.hibernate.StatelessSessionHolder;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import org.hibernate.Query;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * AbstractHibernateSQLQuery is the base class for Hibernate Native SQL queries
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractHibernateSQLQuery<Q extends AbstractHibernateSQLQuery<Q>> extends AbstractSQLQuery<Q> {

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

    public Query createQuery(Expression<?>... args) {
        queryMixin.getMetadata().setValidate(false);
        queryMixin.addProjection(args);
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
            List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
            Expression<?> proj = projection.get(0);
            if (proj instanceof FactoryExpression) {
                for (Expression<?> expr : ((FactoryExpression<?>)proj).getArgs()) {
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
            } else if (isEntityExpression(proj)) {
                query.addEntity(extractEntityExpression(proj).toString(), proj.getType());
            } else if (aliases.containsKey(proj)) {
                for (String scalar : aliases.get(proj)) {
                    if (!used.contains(scalar)) {
                        query.addScalar(scalar);
                        used.add(scalar);
                        break;
                    }
                }
            }

            // set result transformer, if projection is a FactoryExpression instance
            if (projection.size() == 1 && proj instanceof FactoryExpression) {
                query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) proj));
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

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        return new ScrollableResultsIterator<RT>(results);
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addProjection(projection);
        Query query = createQuery(true);
        long total = ((Number)query.uniqueResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            query = createQuery(false);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            reset();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            reset();
            return SearchResults.emptyResults();
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
    public <RT> RT uniqueResult(Expression<RT> expr) {
        Query query = createQuery(expr);
        return (RT)uniqueResult(query);
    }

    @Nullable
    private Object uniqueResult(Query query) {
        reset();
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
     * Set a fetch size for the underlying JDBC query.
     * @param fetchSize the fetch size
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
