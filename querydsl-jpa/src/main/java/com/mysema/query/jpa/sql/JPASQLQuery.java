/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.sql;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.jpa.AbstractSQLQuery;
import com.mysema.query.jpa.HibernateSQLSerializer;
import com.mysema.query.jpa.impl.DefaultSessionHolder;
import com.mysema.query.jpa.impl.JPASessionHolder;
import com.mysema.query.jpa.impl.JPAUtil;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;

/**
 * JPASQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
// TODO : add support for constructor projections
public final class JPASQLQuery extends AbstractSQLQuery<JPASQLQuery> implements SQLCommonQuery<JPASQLQuery>{

    private static final Logger logger = LoggerFactory.getLogger(JPASQLQuery.class);

    @Nullable
    private Map<Object,String> constants;

//    private List<Path<?>> entityPaths;

    private final JPASessionHolder session;

    private final SQLTemplates sqlTemplates;

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        this(new DefaultSessionHolder(entityManager), sqlTemplates, new DefaultQueryMetadata());
    }

    protected JPASQLQuery(JPASessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(metadata);
        this.session = session;
        this.sqlTemplates = sqlTemplates;
    }

    private String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        HibernateSQLSerializer serializer = new HibernateSQLSerializer(sqlTemplates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow);
        constants = serializer.getConstantToLabel();
//        entityPaths = serializer.getEntityPaths();
        return serializer.toString();
    }

    public JPASQLQuery clone(EntityManager entityManager){
        return new JPASQLQuery(new DefaultSessionHolder(entityManager), sqlTemplates, getMetadata().clone());
    }

    public Query createQuery(Expression<?>... args){
        queryMixin.addToProjection(args);
        return createQuery(toQueryString());
    }

    @SuppressWarnings("unchecked")
    private Query createQuery(String queryString) {
        logQuery(queryString);
        List<? extends Expression<?>> projection = queryMixin.getMetadata().getProjection();
        Query query;
        if (projection.get(0) instanceof EntityPath){
            if (projection.size() == 1){
                query = session.createSQLQuery(queryString, projection.get(0).getType());
            }else{
                throw new IllegalArgumentException("Only single element entity projections are supported");
            }

        }else{
            query = session.createSQLQuery(queryString);
        }
        // set constants
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        return query;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expression<?>[] args) {
        Query query = createQuery(args);
        reset();
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expression<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.getResultList();
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expression<?>[] args) {
        return new IteratorAdapter<Object[]>(list(args).iterator());
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expression<RT> projection) {
        return new IteratorAdapter<RT>(list(projection).iterator());
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expression<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(projection);
        Query query = createQuery(toCountRowsString());
        long total = ((Integer)query.getSingleResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            String queryString = toQueryString();
            query = createQuery(queryString);
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

    protected void reset() {
        queryMixin.getMetadata().reset();
//        entityPaths = null;
        constants = null;
    }

    protected String toCountRowsString() {
        return buildQueryString(true);
    }

    protected String toQueryString(){
        return buildQueryString(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expression<RT> expr) {
        Query query = createQuery(expr);
        reset();
        try{
            return (RT) query.getSingleResult();
        }catch(javax.persistence.NoResultException e){
            logger.debug(e.getMessage(),e);
            return null;
        }catch(javax.persistence.NonUniqueResultException e){
            throw new NonUniqueResultException();
        }
    }

}
