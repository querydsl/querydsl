/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;


/**
 * HibernateQuery is the default implementation of the HQLQuery interface for Hibernate
 * 
 * @author tiwe
 *
 */
public class HibernateQuery extends AbstractHibernateQuery<HibernateQuery> implements HQLQuery{

    public HibernateQuery(Session session) {
        this(new DefaultSessionHolder(session), HQLTemplates.DEFAULT);
    }    

    public HibernateQuery(StatelessSession session) {
        this(new StatelessSessionHolder(session), HQLTemplates.DEFAULT);
    }
        
    public HibernateQuery(SessionHolder session, HQLTemplates templates) {
        super(session, templates);
    }
    
}
