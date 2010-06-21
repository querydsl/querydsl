/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;


/**
 * HibernateQuery is the default implementation of the HQLQuery interface for Hibernate
 * 
 * @author tiwe
 *
 */
public final class HibernateQuery extends AbstractHibernateQuery<HibernateQuery> implements HQLQuery{

    /**
     * Creates a detached query
     * The query can be attached via the clone method
     */
    public HibernateQuery() {
        this(NoSessionHolder.DEFAULT, HQLTemplates.DEFAULT);
    }
    
    /**
     * Creates a new Session bound query
     * 
     * @param session
     */
    public HibernateQuery(Session session) {
        this(new DefaultSessionHolder(session), HQLTemplates.DEFAULT);
    }    

    /**
     * Creates a new Session bound query
     * 
     * @param session
     * @param templates
     */
    public HibernateQuery(Session session, JPQLTemplates templates) {
    this(new DefaultSessionHolder(session), templates);
    }

    
    /**
     * Creates a new Stateless session bound query
     * 
     * @param session
     */
    public HibernateQuery(StatelessSession session) {
        this(new StatelessSessionHolder(session), HQLTemplates.DEFAULT);
    }
       
    /**
     * @param session
     * @param templates
     */
    public HibernateQuery(SessionHolder session, JPQLTemplates templates) {
        super(session, templates, new DefaultQueryMetadata());
    }

    /**
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
