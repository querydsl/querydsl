/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractHibernateQuery<SubType extends AbstractHibernateQuery<SubType>> extends HQLQueryBase<SubType>{
    
    private static final Logger logger = LoggerFactory.getLogger(HibernateQueryImpl.class);
    
    private final Session session;

    public AbstractHibernateQuery(Session session, HQLTemplates patterns) {
        super(patterns);
        this.session = session;
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        Query query = session.createQuery(queryString);
        setConstants(query, getConstants());
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

    public static void setConstants(Query query, Map<Object,String> constants) {
        for (Map.Entry<Object, String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            
            if (val instanceof Collection<?>) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Collection<?>) val);
            } else if (val.getClass().isArray()) {
                // NOTE : parameter types should be given explicitly
                query.setParameterList(key, (Object[]) val);
            } else {
                // NOTE : parameter types should be given explicitly
                query.setParameter(key, val);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.list();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.list();
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(toCountRowsString(), null);
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

    public long count() {
        return uniqueResult(Expr.countAll());
    }

    public long count(Expr<?> expr) {
        return uniqueResult(expr.count());
    }

    @SuppressWarnings("unchecked")
    public <RT> RT uniqueResult(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, QueryModifiers.limit(1));
        return (RT) query.uniqueResult();
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
