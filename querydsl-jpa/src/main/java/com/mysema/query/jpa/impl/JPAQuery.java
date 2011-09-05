/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.impl;

import javax.persistence.EntityManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;

/**
 * JPAQuery is the default implementation of the JPQLQuery interface for JPA
 *
 * @author tiwe
 *
 */
public final class JPAQuery extends AbstractJPAQuery<JPAQuery> implements JPQLQuery{

    /**
     * Creates a new detached query
     * The query can be attached via the clone method
     */
    public JPAQuery(){
        super(new NoSessionHolder(), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param entityManager
     */
    public JPAQuery(EntityManager entityManager) {
        super(new DefaultSessionHolder(entityManager), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }
    
    /**
     * Creates a new EntityManager bound query
     *
     * @param entityManager
     */
    public JPAQuery(EntityManager entityManager, QueryMetadata metadata) {
        super(new DefaultSessionHolder(entityManager), HQLTemplates.DEFAULT, metadata);
    }

    /**
     * Creates a new query
     *
     * @param entityManager
     * @param patterns
     */
    public JPAQuery(EntityManager entityManager, JPQLTemplates patterns) {
        super(new DefaultSessionHolder(entityManager), patterns, new DefaultQueryMetadata());
    }

    /**
     * Creates a new query
     * 
     * @param session
     * @param templates
     * @param metadata
     */
    public JPAQuery(JPASessionHolder session, JPQLTemplates templates, QueryMetadata metadata) {
        super(session, templates, metadata);
    }

    /**
     * Clone the state of this query to a new JPAQuery instance with the given EntityManager
     *
     * @param entityManager
     * @return
     */
    public JPAQuery clone(EntityManager entityManager){
        return new JPAQuery(new DefaultSessionHolder(entityManager), getTemplates(), getMetadata().clone());
    }

}
