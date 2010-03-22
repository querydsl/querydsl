/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;

/**
 * Abstract base class for Hibernate API based implementations of the HQLQuery interface
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractHibernateQuery<Q extends AbstractHibernateQuery<Q>> extends HQLQueryBase<Q>{
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateQuery.class);

    private Boolean cacheable, readOnly;
    
    private String cacheRegion;
    
    private int fetchSize = 0;

    private Map<Path<?>,LockMode> lockModes = new HashMap<Path<?>,LockMode>();

    private final SessionHolder session;
    
    private int timeout = 0;    

    public AbstractHibernateQuery(Session session) {
        this(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }
    
    public AbstractHibernateQuery(SessionHolder session, HQLTemplates patterns, QueryMetadata metadata) {
        super(metadata, patterns);
        this.session = session;
    }
    
    @Override
    public long count() {
        QueryModifiers modifiers = getMetadata().getModifiers();
        String queryString = toCountRowsString();
        logQuery(queryString);
        Query query = createQuery(queryString, modifiers);
        reset();
        Long rv = (Long)query.uniqueResult();
        if (rv != null){
            return rv.longValue();
        }else{
            throw new QueryException("Query returned null");
        }
    }       
    
    /**
     * Expose the original Hibernate query for the given projection 
     * 
     * @param expr
     * @return
     */
    public Query createQuery(Expr<?> expr){
        getQueryMixin().addToProjection(expr);
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
        getQueryMixin().addToProjection(expr1, expr2);
        getQueryMixin().addToProjection(rest);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());   
    }
    
    /**
     * Expose the original Hibernate query for the given projection
     * 
     * @param args
     * @return
     */
    public Query createQuery(Expr<?>[] args){
        getQueryMixin().addToProjection(args);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());   
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        Query query = session.createQuery(queryString);
        HibernateUtil.setConstants(query, getConstants());
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
        for (Map.Entry<Path<?>, LockMode> entry : lockModes.entrySet()){
            query.setLockMode(entry.getKey().toString(), entry.getValue());
        }
        
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
    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        Query query = createQuery(args);
        reset();
        return new IteratorAdapter<Object[]>(query.iterate());
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
    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return new IteratorAdapter<RT>(query.iterate());
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?>[] args) {
        Query query = createQuery(args);
        reset();
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        Query query = createQuery(expr);
        reset();
        return query.list();
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        getQueryMixin().addToProjection(expr);
        Query query = createQuery(toCountRowsString(), null);
        long total = (Long) query.uniqueResult();
        try{
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
        }finally{
            reset();
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
     * @param expr1
     * @param expr2
     * @param rest
     * @return
     */
    public ScrollableResults scroll(ScrollMode mode, Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        Query query = createQuery(expr1, expr2, rest);
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
    public ScrollableResults scroll(ScrollMode mode, Expr<?>[] args) {
        Query query = createQuery(args);
        reset();
        return query.scroll(mode);
    }

    /**
     * Enable caching of this query result set.
     * @param cacheable Should the query results be cacheable?
     */
    @SuppressWarnings("unchecked")
    public Q setCacheable(boolean cacheable){
        this.cacheable = cacheable;
        return (Q)this;
    }

    /**
     * Set the name of the cache region.
     * @param cacheRegion the name of a query cache region, or <tt>null</tt>
     * for the default query cache
     */
    @SuppressWarnings("unchecked")
    public Q setCacheRegion(String cacheRegion){
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
     * Set the lock mode for the given path.
     */
    @SuppressWarnings("unchecked")
    public Q setLockMode(Path<?> path, LockMode lockMode){
        lockModes.put(path, lockMode);
        return (Q)this;
    }
    
    /**
     * Entities retrieved by this query will be loaded in 
     * a read-only mode where Hibernate will never dirty-check
     * them or make changes persistent.
     *
     */
    @SuppressWarnings("unchecked")
    public Q setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        return (Q)this;
    }
    
    /**
     * Set a timeout for the underlying JDBC query.
     * @param timeout the timeout in seconds
     */
    @SuppressWarnings("unchecked")
    public Q setTimeout(int timeout){
        this.timeout = timeout;
        return (Q)this;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        getQueryMixin().addToProjection(expr);
        QueryModifiers modifiers = getMetadata().getModifiers();
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, modifiers);
        reset();
        return (RT) query.uniqueResult();
    }
    
    

}
