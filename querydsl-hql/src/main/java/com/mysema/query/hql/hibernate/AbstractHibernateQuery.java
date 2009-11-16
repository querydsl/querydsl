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
    
    private int fetchSize = 0;
    
    private final Session session;

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

    private Query createQuery(Expr<?> expr){
        addToProjection(expr);
        String queryString = toQueryString();        
        return createQuery(queryString, getMetadata().getModifiers());   
    }
    
    private Query createQuery(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest){
        addToProjection(expr1, expr2);
        addToProjection(rest);
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
    
    @SuppressWarnings("unchecked")
    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        return  createQuery(e1, e2, rest).iterate();
    }

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

    public ScrollableResults scroll(ScrollMode mode, Expr<?> expr) {
        return createQuery(expr).scroll(mode);
    }

    public ScrollableResults scroll(ScrollMode mode, Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        return createQuery(expr1, expr2, rest).scroll(mode);
    }
    
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, QueryModifiers.limit(1));
        return (RT) query.uniqueResult();
    }
    
    

}
