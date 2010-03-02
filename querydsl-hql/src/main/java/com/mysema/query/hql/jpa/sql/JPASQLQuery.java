/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa.sql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HibernateSQLSerializer;
import com.mysema.query.hql.jpa.DefaultSessionHolder;
import com.mysema.query.hql.jpa.JPASessionHolder;
import com.mysema.query.hql.jpa.JPAUtil;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * JPASQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality 
 * to execute queries
 * 
 * @author tiwe
 *
 */
// TODO : add support for constructor projections
public final class JPASQLQuery extends ProjectableQuery<JPASQLQuery>{
    
    private static final ENumber<Integer> COUNT_ALL_AGG_EXPR = ONumber.create(Integer.class, Ops.AggOps.COUNT_ALL_AGG);
    
    private static final Logger logger = LoggerFactory.getLogger(JPASQLQuery.class);
        
    private Map<Object,String> constants;
    
    private List<Path<?>> entityPaths;
    
    private String queryString, countRowsString;

    private final JPASessionHolder session;
    
    private final SQLTemplates sqlTemplates;    

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        this(new DefaultSessionHolder(entityManager), sqlTemplates, new DefaultQueryMetadata());
    }
        
    protected JPASQLQuery(JPASessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(new QueryMixin<JPASQLQuery>(metadata));
        this.queryMixin.setSelf(this);
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
        entityPaths = serializer.getEntityPaths();
        return serializer.toString();
    }

    public JPASQLQuery clone(EntityManager entityManager){
        return new JPASQLQuery(new DefaultSessionHolder(entityManager), sqlTemplates, getMetadata().clone());
    }

    @Override
    public long count() {
        return uniqueResult(COUNT_ALL_AGG_EXPR);
    }

    public Query createQuery(Expr<?>... args){
        queryMixin.addToProjection(args);
        return createQuery(toQueryString());   
    }

    @SuppressWarnings("unchecked")
    private Query createQuery(String queryString) {
        logQuery(queryString);
        List<? extends Expr<?>> projection = queryMixin.getMetadata().getProjection();
        Query query;
        if (projection.get(0) instanceof PEntity){
            if (projection.size() == 1){
                query = session.createSQLQuery(queryString, projection.get(0).getType());    
            }else{
                throw new IllegalArgumentException("Only single element entity projections are supported");
            }
            
        }else{
            query = session.createSQLQuery(queryString);
        } 
        // set constants
        JPAUtil.setConstants(query, constants);        
        return query;
    }
    
    public JPASQLQuery from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public JPASQLQuery fullJoin(PEntity<?> o) {
        return queryMixin.fullJoin(o);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public JPASQLQuery innerJoin(PEntity<?> o) {
        return queryMixin.innerJoin(o);
    }

    @Override
    public Iterator<Object[]> iterate(Expr<?>[] args) {
        return list(args).iterator();
    }

    @Override
    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return list(projection).iterator();
    }

    public JPASQLQuery join(PEntity<?> o) {
        return queryMixin.join(o);
    }
    
    public JPASQLQuery leftJoin(PEntity<?> o) {
        return queryMixin.leftJoin(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expr<?>[] args) {
        Query query = createQuery(args);
        reset();
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        Query query = createQuery(projection);
        reset();
        return query.getResultList();
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
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
    
    public JPASQLQuery on(EBoolean... conditions) {
        return queryMixin.on(conditions);
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
        queryMixin.addToProjection(expr);
        Query query = createQuery(toQueryString());
        reset();
        return (RT) query.getSingleResult();
    }
    

}
