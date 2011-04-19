/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

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

    @Override
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

    @Test
    public void Hint(){
        javax.persistence.Query query = query().from(QCat.cat).setHint("org.hibernate.cacheable", true).createQuery(QCat.cat);
        assertNotNull(query);
        assertTrue(query.getHints().containsKey("org.hibernate.cacheable"));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void Hint2(){
        assertFalse(query().from(QCat.cat).setHint("org.hibernate.cacheable", true).list(QCat.cat).isEmpty());
    }

    @Test
    public void LockMode(){
        javax.persistence.Query query = query().from(QCat.cat).setLockMode(LockModeType.READ).createQuery(QCat.cat);
        assertTrue(query.getLockMode().equals(LockModeType.READ));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void LockMode2(){
        assertFalse(query().from(QCat.cat).setLockMode(LockModeType.READ).list(QCat.cat).isEmpty());
    }

    @Test
    public void Limit1_UniqueResult(){
        assertNotNull(query().from(QCat.cat).limit(1).uniqueResult(QCat.cat));
    }

    @Test
    public void Connection_Access(){
        assertNotNull(query().from(QCat.cat).createQuery(QCat.cat).unwrap(Connection.class));
    }

}
