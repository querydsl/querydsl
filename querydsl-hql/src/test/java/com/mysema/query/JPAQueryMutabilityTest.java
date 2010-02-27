/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.jpa.JPAQuery;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@JPAConfig("derby")
public class JPAQueryMutabilityTest{
    
    private EntityManager entityManager;
    
    protected JPAQuery query(){
        return new JPAQuery(entityManager);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Test
    public void test(){
        QCat cat = QCat.cat;
        JPAQuery query = query().from(cat);
        
        query.count();
        assertProjectionEmpty(query);
        query.countDistinct();
        assertProjectionEmpty(query);
        
        query.iterate(cat);
        assertProjectionEmpty(query);
        query.iterate(cat,cat);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat,cat);
        assertProjectionEmpty(query);
        
        query.list(cat);
        assertProjectionEmpty(query);
        query.list(cat,cat);
        assertProjectionEmpty(query);
        query.listDistinct(cat);
        assertProjectionEmpty(query);
        query.listDistinct(cat,cat);
        assertProjectionEmpty(query);
        
        query.listResults(cat);
        assertProjectionEmpty(query);
        query.listDistinctResults(cat);
        assertProjectionEmpty(query);
        
        query.map(cat.name, cat);
        assertProjectionEmpty(query);
        
//        query.uniqueResult(cat);
//        assertProjectionEmpty(query);
//        query.uniqueResult(cat,cat);
//        assertProjectionEmpty(query);
        
    }
    
    @Test
    public void testClone(){
        QCat cat = QCat.cat;
        JPAQuery query = query().from(cat).where(cat.name.isNotNull());        
        JPAQuery query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat);
    }

    private void assertProjectionEmpty(JPAQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
