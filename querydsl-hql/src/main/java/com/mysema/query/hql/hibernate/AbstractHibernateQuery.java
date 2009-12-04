/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.expr.Expr;

/**
 * Abstract base class for Hibernate API based implementations of the HQLQuery interface
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractHibernateQuery<SubType extends AbstractHibernateQuery<SubType>> extends HQLQueryBase<SubType>{
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateQuery.class);
    
    private Boolean cacheable, readOnly;
    
    private String cacheRegion;

    private int fetchSize = 0;

    private final Session session;
    
    private int timeout = 0;    

    public AbstractHibernateQuery(QueryMetadata md, Session session, HQLTemplates patterns) {
        super(md, patterns);
        this.session = session;
    }
    
    public long count() {
        return uniqueResult(Expr.countAll());
    }       
    
    public long count(Expr<?> expr) {
        return uniqueResult(expr.count());
    }
    
    /**
     * Expose the original Hibernate query for the given projection 
     * 
     * @param expr
     * @return
     */
    public Query createQuery(Expr<?> expr){
        addToProjection(expr);
        String queryString = toQueryString();        
        return createQuery(queryString, getMetadata().getModifiers());   
    }

    /**
     * Expose the original Hibernate query for the given projection
     * 
     * @param expr1
     * @param expr2
     * @param rest
     * @return
     */
    public Query createQuery(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest){
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());   
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        Query query = session.createQuery(queryString);
        HibernateUtil.setConstants(query, getConstants());
        if (fetchSize > 0) query.setFetchSize(fetchSize);
        if (timeout > 0) query.setTimeout(timeout);
        if (cacheable != null) query.setCacheable(cacheable);        
        if (cacheRegion != null) query.setCacheRegion(cacheRegion);
        if (readOnly != null) query.setReadOnly(readOnly);
        if (modifiers != null && modifiers.isRestricting()) {
            if (modifiers.getLimit() != null) {
                query.setMaxResults(modifiers.getLimit().intValue());
            }
            if (modifiers.getOffset() != null) {
                query.setFirstResult(modifiers.getOffset().intValue());
            }
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
    @SuppressWarnings("unchecked")
    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        return  createQuery(e1, e2, rest).iterate();
    }

    /**
     * Return the query results as an <tt>Iterator</tt>. If the query
     * contains multiple results pre row, the results are returned in
     * an instance of <tt>Object[]</tt>.<br>
     * <br>
     * Entities returned as results are initialized on demand. The first
     * SQL query returns identifiers only.<br>
     */
    @SuppressWarnings("unchecked")
    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return createQuery(projection).iterate();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        return createQuery(expr1, expr2, rest).list();
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        return createQuery(expr).list();
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(toCountRowsString(), null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            String queryString = toQueryString();
            logQuery(queryString);
            query = createQuery(queryString, modifiers);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    protected void logQuery(String queryString){
        if (logger.isDebugEnabled()){
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
    public ScrollableResults scroll(ScrollMode mode, Expr<?> expr) {
        return createQuery(expr).scroll(mode);
    }
    
    /**
     * Return the query results as <tt>ScrollableResults</tt>. The
     * scrollability of the returned results depends upon JDBC driver
     * support for scrollable <tt>ResultSet</tt>s.<br>
     * 
     * @param mode
     * @param expr1
     * @param expr2
     * @param rest
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        return createQuery(expr1, expr2, rest).scroll(mode);
    }

    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    public SubType setCacheable(boolean cacheable){
        this.cacheable = cacheable;
        return _this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
     */
    public SubType setCacheRegion(String cacheRegion){
        this.cacheRegion = cacheRegion;
        return _this;
    }
    
    /**
     * Set a fetch size for the underlying JDBC query.
     * @param fetchSize the fetch size
     */
    public SubType setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return _this;
    }
    
    /**
     * Entities retrieved by this query will be loaded in 
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     */
    public SubType setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        return _this;
    }
    
    /**
     * Set a timeout for the underlying JDBC query.
     * @param timeout the timeout in seconds
     */
    public SubType setTimeout(int timeout){
        this.timeout = timeout;
        return _this;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, null);
        return (RT) query.uniqueResult();
    }
    
    

}
