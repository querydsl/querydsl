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

import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;
import com.mysema.query.hql.domain.Cat;
import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.jpa.JPAQuery;

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
    public void queryExposure(){
        save(new Cat());
        
        List results = query().from(QCat.cat).createQuery(QCat.cat).getResultList();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
    
//    @Test
//    public void testQuery(){
//        String queryStr = "select cat.name, otherCat.name " +
//            "from Cat cat, Cat otherCat " +
//            "where length(cat.name) > :a1 and length(otherCat.name) > :a1 and locate(otherCat.name,cat.name) - 1 > :a1";
//        
//        javax.persistence.Query query = entityManager.createQuery(queryStr);
//        query.setParameter("a1", 1);
//        query.getResultList();
//    }

//    @Test
//    public void testQuery2(){
//        String queryStr = "select cat.name, otherCat.name " +
//            "from Cat cat, Cat otherCat " + 
//            "where length(cat.name) > :a1 and length(otherCat.name) > :a1 and " +
//            "cat.name like concat(:a2,concat(substring(otherCat.name,:a3+1,:a4),:a2))";
//        
//        javax.persistence.Query query = entityManager.createQuery(queryStr);
//        query.setParameter("a1", 0);
//        query.setParameter("a2", "XXX");
//        query.setParameter("a3",0);
//        query.setParameter("a4",0);
//        query.getResultList();
//    }
    
    @Test
    @Override
    public void tupleProjection(){
    // not yet supported with JPA
    }
    
    @Test
    @Override
    public void arrayProjection(){
    // not yet supported with JPA
    }

}
