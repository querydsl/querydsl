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

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.FactoryExpressionTransformer;
import com.mysema.query.jpa.NativeSQLSerializer;
import com.mysema.query.jpa.ScrollableResultsIterator;
import com.mysema.query.jpa.hibernate.DefaultSessionHolder;
import com.mysema.query.jpa.hibernate.HibernateUtil;
import com.mysema.query.jpa.hibernate.SessionHolder;
import com.mysema.query.jpa.hibernate.StatelessSessionHolder;
import com.mysema.query.sql.Configuration;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.TemplateExpression;

/**
 * AbstractHibernateSQLQuery is the base class for Hibernate Native SQL queries
 *
 * @author tiwe
 *
 * @param <Q>
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractHibernateSQLQuery<Q extends AbstractHibernateSQLQuery<Q> & com.mysema.query.Query<Q>> extends AbstractSQLQuery<Q> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractHibernateSQLQuery.class);

    protected Boolean cacheable, readOnly;

    protected String cacheRegion;

    @Nullable
    private Map<Object,String> constants;

    protected int fetchSize = 0;

    private final SessionHolder session;

    protected final Configuration configuration;

    protected int timeout = 0;

    public AbstractHibernateSQLQuery(Session session, Configuration conf) {
        this(new DefaultSessionHolder(session), conf, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(StatelessSession session, Configuration conf) {
        this(new StatelessSessionHolder(session), conf, new DefaultQueryMetadata());
    }

    public AbstractHibernateSQLQuery(SessionHolder session, Configuration conf, QueryMetadata metadata) {
        super(metadata);
        this.session = session;
        this.configuration = conf;
    }

    private String buildQueryString(boolean forCountRow) {
        NativeSQLSerializer serializer = new NativeSQLSerializer(configuration);
        if (union != null) {
            serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCountRow);
        }
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    public Query createQuery(Expression<?>... args) {
        queryMixin.getMetadata().setValidate(false);
        queryMixin.addProjection(args);
        return createQuery(toQueryString());
    }

    private void addEntity(org.hibernate.SQLQuery query, Expression<?> expr) {
        if (expr instanceof Operation) {
            expr = ((Operation)expr).getArg(0);
        } else if (expr instanceof TemplateExpression) {
            expr = (Expression<?>) ((TemplateExpression)expr).getArg(0);
        }
        System.err.println(expr);
        query.addEntity(expr.toString(), expr.getType());
    }

    private Query createQuery(String queryString) {
        logQuery(queryString);
        org.hibernate.SQLQuery query = session.createSQLQuery(queryString);
        // set constants
        HibernateUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());

        // set entity paths
        List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
        Expression<?> proj = projection.get(0);
        if (proj instanceof FactoryExpression) {
            for (Expression<?> expr : ((FactoryExpression<?>)proj).getArgs()) {
                if (isEntityExpression(expr)) {
                    addEntity(query, expr);
                }
            }
        } else if (isEntityExpression(proj)) {
            addEntity(query, proj);
        }

        // set result transformer, if projection is a FactoryExpression instance
        if (projection.size() == 1 && proj instanceof FactoryExpression) {
            query.setResultTransformer(new FactoryExpressionTransformer((FactoryExpression<?>) proj));
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
    public List<Tuple> list(Expression<?>... projection) {
        return list(queryMixin.createProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        return new ScrollableResultsIterator<RT>(results);
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(queryMixin.createProjection(args));
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addProjection(projection);
        Query query = createQuery(toCountRowsString());
        long total = ((Number)query.uniqueResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            String queryString = toQueryString();
            query = createQuery(queryString);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            reset();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            reset();
            return SearchResults.emptyResults();
        }
    }

    protected void logQuery(String queryString) {
        if (logger.isDebugEnabled()) {
            logger.debug(queryString.replace('\n', ' '));
        }
    }

    protected void reset() {
        queryMixin.getMetadata().reset();
        constants = null;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString() {
        return buildQueryString(false);
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(queryMixin.createProjection(args));
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

}
