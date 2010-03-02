/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.AbstractSQLQuery;
import com.mysema.query.hql.HibernateSQLSerializer;
import com.mysema.query.hql.hibernate.DefaultSessionHolder;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.query.hql.hibernate.HibernateUtil;
import com.mysema.query.hql.hibernate.SessionHolder;
import com.mysema.query.hql.hibernate.StatelessSessionHolder;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.Path;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality 
 * to execute queries
 * 
 * @author tiwe
 *
 */
public final class HibernateSQLQuery extends AbstractSQLQuery<HibernateSQLQuery>{
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateQuery.class);
    
    private Boolean cacheable, readOnly;
    
    private String cacheRegion;    
    
    private Map<Object,String> constants;
    
    private List<Path<?>> entityPaths;
    
    private int fetchSize = 0;
    
    private String queryString, countRowsString;

    private final SessionHolder session;
    
    private final SQLTemplates sqlTemplates;    
    
    private int timeout = 0;

    public HibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        this(new DefaultSessionHolder(session), sqlTemplates, new DefaultQueryMetadata());
    }
        
    protected HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(metadata);
        this.session = session;
        this.sqlTemplates = sqlTemplates;
    }
    
    public HibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates){
        this(new StatelessSessionHolder(session), sqlTemplates, new DefaultQueryMetadata());
    }
    
    private String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        HibernateSQLSerializer serializer = new HibernateSQLSerializer(sqlTemplates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow);
        constants = serializer.getConstantToLabel();
        entityPaths = serializer.getEntityPaths();
        return serializer.toString();
    }

    public HibernateSQLQuery clone(Session session){
        return new HibernateSQLQuery(new DefaultSessionHolder(session), sqlTemplates, getMetadata().clone());
    }

    public org.hibernate.SQLQuery createQuery(Expr<?>... args){
        queryMixin.addToProjection(args);
        return createQuery(toQueryString());   
    }

    @SuppressWarnings("unchecked")
    private org.hibernate.SQLQuery createQuery(String queryString) {
        logQuery(queryString);
        org.hibernate.SQLQuery query = session.createSQLQuery(queryString);
        // set constants
        HibernateUtil.setConstants(query, constants);
        // set entity paths
        for (Path<?> path : entityPaths){
            query.addEntity(path.toString(), path.getType());
        }
        // set result transformer, if projection is an EConstructor instance
        List<? extends Expr<?>> projection = queryMixin.getMetadata().getProjection();
        if (projection.size() == 1 && projection.get(0) instanceof EConstructor){
            query.setResultTransformer(new ConstructorResultTransformer((EConstructor<?>) projection.get(0)));
        }
        if (fetchSize > 0){
            query.setFetchSize(fetchSize);
        }
        if (timeout > 0){
            query.setTimeout(timeout);
        }
        if (cacheable != null){
            query.setCacheable(cacheable);        
        }
        if (cacheRegion != null){
            query.setCacheRegion(cacheRegion);
        }
        if (readOnly != null){
            query.setReadOnly(readOnly);        
        }
        return query;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expr<?>[] projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.list();
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(projection);
        Query query = createQuery(toCountRowsString());
        long total = ((Integer)query.uniqueResult()).longValue();
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
    
    protected void logQuery(String queryString){
        if (logger.isDebugEnabled()){
            logger.debug(queryString.replace('\n', ' '));    
        }        
    }
    
    protected void reset() {
        queryMixin.getMetadata().reset();
        countRowsString = null;
        queryString = null;
        entityPaths = null;
        constants = null;
    }
    
    protected String toCountRowsString() {
        if (countRowsString == null) {
            countRowsString = buildQueryString(true);
        }
        return countRowsString;
    }
   
    protected String toQueryString(){
      if (queryString == null) {
          queryString = buildQueryString(false);
      }
      return queryString;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        org.hibernate.SQLQuery query = createQuery(expr);
        reset();
        return (RT) query.uniqueResult();
    }
    
    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    public HibernateSQLQuery setCacheable(boolean cacheable){
        this.cacheable = cacheable;
        return this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
     */
    public HibernateSQLQuery setCacheRegion(String cacheRegion){
        this.cacheRegion = cacheRegion;
        return this;
    }
    
    /**
     * Set a fetch size for the underlying JDBC query.
     * @param fetchSize the fetch size
     */
    public HibernateSQLQuery setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }
    
    /**
     * Entities retrieved by this query will be loaded in 
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     */
    public HibernateSQLQuery setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        return this;
    }
    
    /**
     * Set a timeout for the underlying JDBC query.
     * @param timeout the timeout in seconds
     */
    public HibernateSQLQuery setTimeout(int timeout){
        this.timeout = timeout;
        return this;
    }

}
