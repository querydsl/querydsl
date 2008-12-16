/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.types.Expr;

/**
 * JpaqlQuery provides
 *
 * @author tiwe
 * @version $Id$
 */
public class JpaqlQuery extends HqlQueryBase<JpaqlQuery>{
    
    private static final Logger logger = LoggerFactory.getLogger(JpaqlQuery.class);

    private static final HqlOps OPS_DEFAULT = new HqlOps();
    
    private final EntityManager em;

    public JpaqlQuery(EntityManager em) {
        this(em, OPS_DEFAULT);
    }

    public JpaqlQuery(EntityManager em, HqlOps ops) {
        super(ops);
        this.em = em;
    }
    
    private Query createQuery(String queryString, Integer limit, Integer offset) {
        Query query = em.createQuery(queryString);
        setConstants(query, getConstants());        
        if (limit != null) query.setMaxResults(limit);
        if (offset != null) query.setFirstResult(offset);
        return query;
    }
        
    public static void setConstants(Query query, List<Object> constants) {
        for (int i=0; i < constants.size(); i++){
            String key = "a"+(i+1);
            Object val = constants.get(i);            
            query.setParameter(key,val);
        }        
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr){
        select(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        select(expr1, expr2);
        select(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, limit, offset);
        return query.getResultList();
    }
    

}
