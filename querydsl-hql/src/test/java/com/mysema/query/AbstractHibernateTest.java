/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.Test;

import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.hibernate.HibernateQuery;

/**
 * @author tiwe
 *
 */
public abstract class AbstractHibernateTest extends AbstractStandardTest{
    
    private Session session;
    
    protected HibernateQuery query(){
        return new HibernateQuery(session, getTemplates());
    }
    
    protected HQLTemplates getTemplates(){
	return HQLTemplates.DEFAULT;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    protected void save(Object entity) {
        session.save(entity);        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void queryExposure(){
        save(new Cat());
        
        List results = query().from(QCat.cat).createQuery(QCat.cat).list();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
    
    @Test
    public void lockMode(){
        query().from(QCat.cat).setLockMode(QCat.cat, LockMode.UPGRADE).list(QCat.cat);
    }

}
