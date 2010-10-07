/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;

import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.QCat;
import com.mysema.query.jpa.impl.JPAQuery;

/**
 * @author tiwe
 *
 */
public abstract class AbstractJPATest extends AbstractStandardTest{

    private EntityManager entityManager;

    protected JPAQuery query(){
        return new JPAQuery(entityManager, getTemplates());
    }

    protected JPQLTemplates getTemplates(){
        return HQLTemplates.DEFAULT;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void save(Object entity) {
        entityManager.persist(entity);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void QueryExposure(){
        save(new Cat());
        List results = query().from(QCat.cat).createQuery(QCat.cat).getResultList();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

}
