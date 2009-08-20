/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import javax.persistence.EntityManager;

import com.mysema.query.hql.HQLQuery;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.jpa.JPAQueryImpl;

/**
 * @author tiwe
 *
 */
public abstract class AbstractJPATest extends AbstractStandardTest{
    
    private static final HQLTemplates templates = new HQLTemplates();
    
    private EntityManager entityManager;
    
    protected HQLQuery query(){
        return new JPAQueryImpl(entityManager, templates);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void save(Object entity) {
        entityManager.persist(entity);
    }

}
