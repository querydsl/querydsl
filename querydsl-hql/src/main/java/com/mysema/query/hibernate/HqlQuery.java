/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;

/**
 * HqlQuery provides a fluent statically typed interface for creating HQL queries.
 * 
 * @author tiwe
 * @version $Id$
 */
public class HqlQuery extends HqlQueryBase<HqlQuery>{

    private static final Logger logger = LoggerFactory.getLogger(HqlQuery.class);

    private Integer limit, offset;
    
    private final Session session;

    public HqlQuery(Session session) {
        this.session = session;
    }

    public HqlQuery(Session session, HqlOps ops) {
        super(ops);
        this.session = session;
    }
    
    private Query createQuery(String queryString, Integer limit, Integer offset) {
        Query query = session.createQuery(queryString);
        QueryUtil.setConstants(query, getConstants());
        
        if (limit != null) query.setMaxResults(limit);
        if (offset != null) query.setFirstResult(offset);
        return query;
    }
    
    public HqlQuery forExample(Path.Entity<?> entity, Map<String, Object> map) {
        select(entity).from(entity);
        try {            
            where(QueryUtil.createQBECondition(entity,map));
            return this;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw new RuntimeException(error, e);
        }
    }

    public HqlQuery limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public <RT> List<RT> list(Expr<RT> expr){
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.list();
    }
    
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        select(expr1, expr2);
        select(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.list();
    }
    
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        select(expr);
        Query query = createQuery(toCountRowsString(), null, null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, limit, offset);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            return new SearchResults<RT>(list,
                    limit == null ? Long.MAX_VALUE : limit.longValue(),
                    offset == null ? 0l : offset.longValue(), total);
        } else {
            return SearchResults.emptyResults();
        }
    }
    
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, 1, null);
        return (RT)query.uniqueResult();
    }

    public HqlQuery offset(int offset) {
        this.offset = offset;
        return this;
    }

    public HqlQuery restrict(QueryModifiers mod) {
        this.limit = mod.getLimit();
        this.offset = mod.getOffset();
        return this;
    }

}
