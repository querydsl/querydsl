/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.hql.HQLQueryBase;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.expr.Expr;

/**
 * Abstract base class for JPA API based implementations of the HQLQuery interface
 * 
 * @author tiwe
 *
 * @param <SubType>
 */
public abstract class AbstractJPAQuery<SubType extends AbstractJPAQuery<SubType>> extends HQLQueryBase<SubType> {

    private static final Logger logger = LoggerFactory.getLogger(JPAQuery.class);
    
    private final EntityManager em;

    public AbstractJPAQuery(QueryMetadata md, EntityManager em, HQLTemplates patterns) {
        super(md, patterns);
        this.em = em;
    }

    private Query createQuery(String queryString, @Nullable QueryModifiers modifiers) {
        Query query = em.createQuery(queryString);
        JPAUtil.setConstants(query, getConstants());
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
        return uniqueResult(Expr.countAll());
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return list(projection).iterator();
    }

    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return list(first, second, rest).iterator();
    }
}
