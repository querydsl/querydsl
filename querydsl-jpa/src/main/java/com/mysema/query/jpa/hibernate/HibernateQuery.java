/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;

/**
 * HibernateQuery is the default implementation of the JPQLQuery interface for Hibernate
 *
 * @author tiwe
 *
 */
public final class HibernateQuery extends AbstractHibernateQuery<HibernateQuery> implements JPQLQuery{

    /**
     * Creates a detached query
     * The query can be attached via the clone method
     */
    public HibernateQuery() {
        super(NoSessionHolder.DEFAULT, HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     *
     * @param session
     */
    public HibernateQuery(Session session) {
        super(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }
    
    /**
     * Creates a new Session bound query
     *
     * @param session
     */
    public HibernateQuery(Session session, QueryMetadata metadata) {
        super(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, metadata);
    }

    /**
     * Creates a new Session bound query
     *
     * @param session
     * @param templates
     */
    public HibernateQuery(Session session, JPQLTemplates templates) {
        super(new DefaultSessionHolder(session), templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Stateless session bound query
     *
     * @param session
     */
    public HibernateQuery(StatelessSession session) {
        super(new StatelessSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     * 
     * @param session
     * @param templates
     */
    public HibernateQuery(SessionHolder session, JPQLTemplates templates) {
        super(session, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     * 
     * @param session
     * @param templates
     * @param metadata
     */
    protected HibernateQuery(SessionHolder session, JPQLTemplates templates, QueryMetadata metadata) {
        super(session, templates, metadata);
    }

    /**
     * Clone the state of this query to a new HibernateQuery instance with the given Session
     *
     * @param session
     * @return
     */
    public HibernateQuery clone(Session session){
        return new HibernateQuery(new DefaultSessionHolder(session), getTemplates(), getMetadata().clone());
    }

}
