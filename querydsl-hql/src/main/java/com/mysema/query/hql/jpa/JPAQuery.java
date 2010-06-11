/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import javax.persistence.EntityManager;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;

/**
 * JPAQuery is the default implementation of the HQLQuery interface for JPA
 * 
 * @author tiwe
 *
 */
public final class JPAQuery extends AbstractJPAQuery<JPAQuery> implements HQLQuery{

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
     * Creates a new query
     * 
     * @param entityManager
     * @param patterns
     */
    public JPAQuery(EntityManager entityManager, JPQLTemplates patterns) {
        super(new DefaultSessionHolder(entityManager), patterns, new DefaultQueryMetadata());
    }
    
    /**
     * @param session
     * @param templates
     * @param metadata
     */
    protected JPAQuery(JPASessionHolder session, JPQLTemplates templates, QueryMetadata metadata) {
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
