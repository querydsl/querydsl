/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.Projectable;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.grammar.HqlGrammar;
import com.mysema.query.grammar.HqlOps;
import com.mysema.query.grammar.HqlQueryBase;
import com.mysema.query.grammar.types.Expr;

/**
 * AbstractHqlQuery provides the same features as HqlQuery, but acts as a super class
 * for domain specific query subclasses
 *
 * @author tiwe
 * @version $Id$
 */
public class AbstractHqlQuery<A extends AbstractHqlQuery<A>> extends HqlQueryBase<A> implements Projectable{
    
    private static final Logger logger = LoggerFactory.getLogger(HqlQuery.class);
    
    private final Session session;
    
    public AbstractHqlQuery(Session session) {
        this(session, HqlOps.DEFAULT);
    }

    public AbstractHqlQuery(Session session, HqlOps ops) {
        super(ops);
        this.session = session;
    }
    
    private Query createQuery(String queryString, QueryModifiers modifiers) {
        Query query = session.createQuery(queryString);
        setConstants(query, getConstants());    
        if (modifiers != null){
        	if (modifiers.getLimit() != null){
        		query.setMaxResults(modifiers.getLimit().intValue());
        	}
            if (modifiers.getOffset() != null){
            	query.setFirstResult(modifiers.getOffset().intValue());	
            }
        }        
        return query;
    }
    
    public static void setConstants(Query query, List<?> constants){
        for (int i=0; i < constants.size(); i++){
            String key = "a"+(i+1);
            Object val = constants.get(i);            
            if (val instanceof Collection<?>){
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key,(Collection<?>)val);
            }else if (val.getClass().isArray()){
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key,(Object[])val);
            }else{
                // NOTE : parameter types should be given explicitly
                query.setParameter(key,val);    
            }
        }
    }
        
    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr){
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.list();        
    }
    
    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(toCountRowsString(),null);
        long total = (Long) query.uniqueResult();
        if (total > 0) {
        	QueryModifiers modifiers = getMetadata().getModifiers();
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, modifiers);
            @SuppressWarnings("unchecked")
            List<RT> list = query.list();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }
    
    public long count(){
        return uniqueResult(HqlGrammar.count());
    }
    
    public long count(Expr<?> expr){
        return uniqueResult(HqlGrammar.count(expr));
    }
    
    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, QueryModifiers.limit(1));
        return (RT)query.uniqueResult();
    }

    public Iterator<Object[]> iterate(Expr<?> e1, Expr<?> e2, Expr<?>... rest) {
        // TODO : optimize
        return list(e1, e2, rest).iterator();
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO : optimize
        return list(projection).iterator();
    }

}
