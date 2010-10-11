/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLQueryBase;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;

/**
 * Abstract base class for JPA API based implementations of the JPQLQuery interface
 *
 * @author tiwe
 *
 * @param <Q>
 */
public abstract class AbstractJPAQuery<Q extends AbstractJPAQuery<Q>> extends JPQLQueryBase<Q> {

    private static final Logger logger = LoggerFactory.getLogger(JPAQuery.class);

    private final JPASessionHolder sessionHolder;
    
    private final Map<String,Object> hints = new HashMap<String,Object>();

    @Nullable
    private LockModeType lockMode;
    
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
    public Query createQuery(Expression<?> expr){
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
    public Query createQuery(Expression<?> expr1, Expression<?> expr2, Expression<?>... rest){
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
    public Query createQuery(Expression<?>[] args){
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
        
        if (lockMode != null){
            query.setLockMode(lockMode);    
        }
        
        for (Map.Entry<String, Object> entry : hints.entrySet()){
            query.setHint(entry.getKey(), entry.getValue());
        }

        // set transformer, if necessary and possible
        List<? extends Expression<?>> projection = getMetadata().getProjection();
        if (projection.size() == 1){
            Expression<?> expr = projection.get(0);
            if (expr instanceof FactoryExpression<?>  && !(expr instanceof ConstructorExpression<?>)){
                if (query.getClass().getName().startsWith("org.hibernate")){
                    try {
                        Class<?> cl = Class.forName("com.mysema.query.jpa.impl.JPAQueryTransformerTask");
                        cl.getConstructor(Query.class, FactoryExpression.class).newInstance(query, expr);
                    } catch (ClassNotFoundException e) {
                        throw new QueryException(e.getMessage(), e);
                    } catch (SecurityException e) {
                        throw new QueryException(e.getMessage(), e);
                    } catch (InstantiationException e) {
                        throw new QueryException(e.getMessage(), e);
                    } catch (IllegalAccessException e) {
                        throw new QueryException(e.getMessage(), e);
                    } catch (InvocationTargetException e) {
                        throw new QueryException(e.getMessage(), e);
                    } catch (NoSuchMethodException e) {
                        throw new QueryException(e.getMessage(), e);
                    }
                    
                }
            }
        }

        return query;
    }

    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator());
    }

    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator());
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expression<?>[] args) {
        Query query = createQuery(args);
        reset();
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expression<RT> expr) {
        Query query = createQuery(expr);
        reset();
        return query.getResultList();
    }

    public <RT> SearchResults<RT> listResults(Expression<RT> expr) {
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
    public <RT> RT uniqueResult(Expression<RT> expr) {
        getQueryMixin().addToProjection(expr);
        String queryString = toQueryString();
        logQuery(queryString);
        Query query = createQuery(queryString, null);
        reset();
        try{
            return (RT) query.getSingleResult();    
        }catch(NoResultException e){
            logger.debug(e.getMessage(),e);
            return null;            
        }
        
    }

    @SuppressWarnings("unchecked")
    public Q setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return (Q)this;
    }
    
    @SuppressWarnings("unchecked")
    public Q setHint(String name, Object value){
        hints.put(name, value);
        return (Q)this;
    }
    
    
}
