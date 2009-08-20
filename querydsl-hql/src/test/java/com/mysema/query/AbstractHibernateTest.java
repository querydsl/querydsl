/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import org.hibernate.Session;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.hibernate.HibernateQueryImpl;

/**
 * @author tiwe
 *
 */
public abstract class AbstractHibernateTest extends AbstractStandardTest{
    
    private static final HQLTemplates templates = new HQLTemplates();
    
    private Session session;
    
    protected HQLQuery query(){
        return new HibernateQueryImpl(session, templates);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    protected void save(Object entity) {
        session.save(entity);        
    }

}
