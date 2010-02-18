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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryMixin;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.query.hql.hibernate.HibernateUtil;
import com.mysema.query.hql.hibernate.SessionHolder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.Union;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality 
 * to execute queries
 * 
 * @author tiwe
 *
 */
public class HibernateSQLQuery extends ProjectableQuery<HibernateSQLQuery> implements SQLQuery{
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateQuery.class);
    
    private Boolean cacheable, readOnly;
    
    private String cacheRegion;    
    
    private Map<Object,String> constants;
    
    private int fetchSize = 0;
    
    private String queryString, countRowsString;

    private final SessionHolder session;
    
    private final SQLTemplates sqlTemplates;    
    
    private int timeout = 0;

    public HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates) {
        super(new QueryMixin<HibernateSQLQuery>());
        this.queryMixin.setSelf(this);
        this.session = session;
        this.sqlTemplates = sqlTemplates;
    }

    private String buildQueryString(boolean forCountRow) {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No joins given");
        }
        SQLSerializer serializer = new SQLSerializer(sqlTemplates);
        serializer.serialize(queryMixin.getMetadata(), forCountRow);
        constants = serializer.getConstantToLabel();
        return serializer.toString();
    }

    @Override
    public long count() {
        return uniqueResult(Expr.countAll());
    }

    public org.hibernate.SQLQuery createQuery(Expr<?>... args){
        queryMixin.addToProjection(args);
        String queryString = toQueryString();
        logQuery(queryString);
        return createQuery(queryString, queryMixin.getMetadata().getModifiers());   
    }

    private org.hibernate.SQLQuery createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        org.hibernate.SQLQuery query = session.createSQLQuery(queryString);
        HibernateUtil.setConstants(query, constants);
        if (fetchSize > 0) query.setFetchSize(fetchSize);
        if (timeout > 0) query.setTimeout(timeout);
        if (cacheable != null) query.setCacheable(cacheable);        
        if (cacheRegion != null) query.setCacheRegion(cacheRegion);
        if (readOnly != null) query.setReadOnly(readOnly);
        // modifiers are serialized into SQL string
        return query;
    }

    @Override
    public SQLQuery from(PEntity<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    public SQLQuery fullJoin(PEntity<?> o) {
        return queryMixin.fullJoin(o);
    }

    @Override
    public SQLQuery innerJoin(PEntity<?> o) {
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

    @Override
    public SQLQuery join(PEntity<?> o) {
        return queryMixin.join(o);
    }
    
    @Override
    public SQLQuery leftJoin(PEntity<?> o) {
        return queryMixin.leftJoin(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> list(Expr<?>[] args) {
        return createQuery(args).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> List<RT> list(Expr<RT> projection) {
        // TODO : handle entity projections as well
        return createQuery(projection).list();
    }
    
    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(projection);
        Query query = createQuery(toCountRowsString(), null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
            QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
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
    
    @Override
    public SQLQuery on(EBoolean... conditions) {
        return queryMixin.on(conditions);
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
    
    @Override
    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        // TODO 
        throw new UnsupportedOperationException();
    }

    @Override
    public <RT> Union<RT> union(ObjectSubQuery<RT>... sq) {
        // TODO
        throw new UnsupportedOperationException();
    }
    
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        // TODO : handle entity projections as well
        queryMixin.addToProjection(expr);
        QueryModifiers modifiers = queryMixin.getMetadata().getModifiers();
        String queryString = toQueryString();
        org.hibernate.SQLQuery query = createQuery(queryString, modifiers);
        return (RT) query.uniqueResult();
    }

}
