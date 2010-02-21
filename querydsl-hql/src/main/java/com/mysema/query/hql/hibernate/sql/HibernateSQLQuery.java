/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryMixin;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.hibernate.DefaultSessionHolder;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.query.hql.hibernate.HibernateUtil;
import com.mysema.query.hql.hibernate.SessionHolder;
import com.mysema.query.hql.hibernate.StatelessSessionHolder;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality 
 * to execute queries
 * 
 * @author tiwe
 *
 */
public class HibernateSQLQuery extends ProjectableQuery<HibernateSQLQuery>{
    
    private static final ENumber<Integer> COUNT_ALL_AGG_EXPR = ONumber.create(Integer.class, Ops.AggOps.COUNT_ALL_AGG);
    
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
        super(new QueryMixin<HibernateSQLQuery>(metadata));
        this.queryMixin.setSelf(this);
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

    @Override
    public long count() {
        return uniqueResult(COUNT_ALL_AGG_EXPR);
    }

    public org.hibernate.SQLQuery createQuery(Expr<?>... args){
        queryMixin.addToProjection(args);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, queryMixin.getMetadata().getModifiers());   
    }

    @SuppressWarnings("unchecked")
    private org.hibernate.SQLQuery createQuery(String queryString, @Nullable QueryModifiers modifiers) {
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
        if (fetchSize > 0) query.setFetchSize(fetchSize);
        if (timeout > 0) query.setTimeout(timeout);
        if (cacheable != null) query.setCacheable(cacheable);        
        if (cacheRegion != null) query.setCacheRegion(cacheRegion);
        if (readOnly != null) query.setReadOnly(readOnly);        
        return query;
    }
    
    public HibernateSQLQuery from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    public HibernateSQLQuery fullJoin(PEntity<?> o) {
        return queryMixin.fullJoin(o);
    }

    public QueryMetadata getMetadata(){
        return queryMixin.getMetadata();
    }

    public HibernateSQLQuery innerJoin(PEntity<?> o) {
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

    public HibernateSQLQuery join(PEntity<?> o) {
        return queryMixin.join(o);
    }
    
    public HibernateSQLQuery leftJoin(PEntity<?> o) {
        return queryMixin.leftJoin(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expr<?>[] args) {
        Query query = createQuery(args);
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
        Query query = createQuery(toCountRowsString(), null);
        long total = ((Integer)query.uniqueResult()).longValue();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
            String queryString = toQueryString();
            logQuery(queryString);
            query = createQuery(queryString, modifiers);
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
    
    public HibernateSQLQuery on(EBoolean... conditions) {
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
        // TODO : handle entity projections as well
        queryMixin.addToProjection(expr);
        QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
        String queryString = toQueryString();
        org.hibernate.SQLQuery query = createQuery(queryString, modifiers);
        reset();
        return (RT) query.uniqueResult();
    }

}
