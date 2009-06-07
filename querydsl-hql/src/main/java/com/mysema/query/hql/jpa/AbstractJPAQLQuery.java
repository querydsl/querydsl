/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLGrammar;
import com.mysema.query.hql.HQLPatterns;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractJPAQLQuery<SubType extends AbstractJPAQLQuery<SubType>> extends HQLQueryBase<SubType> {

    private static final Logger logger = LoggerFactory.getLogger(JPAQLQueryImpl.class);

    private final EntityManager em;

    public AbstractJPAQLQuery(EntityManager em) {
        this(em, HQLPatterns.DEFAULT);
    }

    public AbstractJPAQLQuery(EntityManager em, HQLPatterns patterns) {
        super(patterns);
        this.em = em;
    }

    private Query createQuery(String queryString, QueryModifiers modifiers) {
        Query query = em.createQuery(queryString);
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
        for (Map.Entry<Object,String> entry : constants.entrySet()){
            String key = entry.getValue();
            Object val = entry.getKey();
            query.setParameter(key, val);
        }
    }

    @SuppressWarnings("unchecked")
    public <RT> List<RT> list(Expr<RT> expr) {
        addToProjection(expr);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> list(Expr<?> expr1, Expr<?> expr2, Expr<?>... rest) {
        addToProjection(expr1, expr2);
        addToProjection(rest);
        String queryString = toString();
        logger.debug("query : {}", queryString);
        Query query = createQuery(queryString, getMetadata().getModifiers());
        return query.getResultList();
    }

    public <RT> SearchResults<RT> listResults(Expr<RT> expr) {
        addToProjection(expr);
        Query query = createQuery(toCountRowsString(), null);
        long total = (Long) query.getSingleResult();
        if (total > 0) {
            QueryModifiers modifiers = getMetadata().getModifiers();
            String queryString = toString();
            logger.debug("query : {}", queryString);
            query = createQuery(queryString, modifiers);
            @SuppressWarnings("unchecked")
            List<RT> list = query.getResultList();
            return new SearchResults<RT>(list, modifiers, total);
        } else {
            return SearchResults.emptyResults();
        }
    }

    public long count() {
        return uniqueResult(HQLGrammar.count());
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        // TODO Auto-generated method stub
        return list(projection).iterator();
    }

    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        // TODO Auto-generated method stub
        return list(first, second, rest).iterator();
    }
}
