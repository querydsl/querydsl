/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;
import com.mysema.query.types.Expr;

/**
 * Abstract base class for JPA API based implementations of the HQLQuery interface
 * 
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractJPAQuery<Q extends AbstractJPAQuery<Q>> extends HQLQueryBase<Q> {

    private static final Logger logger = LoggerFactory.getLogger(JPAQuery.class);
    
    private final JPASessionHolder sessionHolder;

    public AbstractJPAQuery(EntityManager em) {
        this(new DefaultSessionHolder(em), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }
    
    public AbstractJPAQuery(JPASessionHolder sessionHolder, JPQLTemplates patterns, QueryMetadata metadata) {
        super(metadata, patterns);
        this.sessionHolder = sessionHolder;
    }

    public long count() {
        String queryString = toCountRowsString();
        logQuery(queryString);
        Query query = createQuery(queryString, null);
        reset();
        return (Long) query.getSingleResult();
    }
      
    /**
     * Expose the original JPA query for the given projection 
     * 
     * @param expr
     * @return
     */
    public Query createQuery(Expr<?> expr){
        getQueryMixin().addToProjection(expr);
        String queryString = toString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());        
    }
    
    /**
     * Expose the original JPA query for the given projection 
     * 
     * @param expr
     * @return
     */
    public Query createQuery(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest){
        getQueryMixin().addToProjection(expr1, expr2);
        getQueryMixin().addToProjection(rest);
        String queryString = toString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());
    }
    
    /**
     * Expose the original JPA query for the given projection
     * 
     * @param args
     * @return
     */
    public Query createQuery(Expr<?>[] args){
        getQueryMixin().addToProjection(args);
        String queryString = toString();
        logQuery(queryString);
        return createQuery(queryString, getMetadata().getModifiers());
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        Query query = sessionHolder.createQuery(queryString);
        JPAUtil.setConstants(query, getConstants(), getMetadata().getParams());
        if (modifiers != null && modifiers.isRestricting()) {
            if (modifiers.getLimit() != null) {
                query.setMaxResults(modifiers.getLimit().intValue());
            }
            if (modifiers.getOffset() != null) {
                query.setFirstResult(modifiers.getOffset().intValue());
            }
        }
        

        // set transformer, if necessary
//        List<? extends Expr<?>> projection = getMetadata().getProjection();
//        if (projection.size() == 1){
//            Expr<?> expr = projection.get(0);
//            if (expr instanceof EConstructor<?>  && !(expr.getClass().equals(EConstructor.class))){
//            if (query instanceof HibernateQuery){
//                ((HibernateQuery)query).getHibernateQuery().setResultTransformer(
//                    new ConstructorTransformer((EConstructor<?>) projection.get(0)));
//            }
//            }
//        }
        
        return query;
    }

    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator());
    }

    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator());
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?>[] args) {       
        Query query = createQuery(args);
        reset();
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        Query query = createQuery(expr);
        reset();
        return query.getResultList();
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        getQueryMixin().addToProjection(expr);
        Query query = createQuery(toCountRowsString(), null);
        long total = (Long) query.getSingleResult();
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            String queryString = toString();
            logQuery(queryString);
            query = createQuery(queryString, modifiers);
            @SuppressWarnings("unchecked")
            List<RT> list = query.getResultList();
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
    
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        getQueryMixin().addToProjection(expr);
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, null);
        reset();
        return (RT) query.getSingleResult();
    }
}
