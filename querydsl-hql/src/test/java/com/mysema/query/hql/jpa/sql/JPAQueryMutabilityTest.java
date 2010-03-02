/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.hql.domain.sql.SAnimal;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.testutil.JPAConfig;
import com.mysema.testutil.JPATestRunner;

@RunWith(JPATestRunner.class)
@JPAConfig("derby")
public class JPAQueryMutabilityTest{
    
    private static final SQLTemplates derbyTemplates = new DerbyTemplates();
    
    private EntityManager entityManager;
    
    protected JPASQLQuery query(){
        return new JPASQLQuery(entityManager, derbyTemplates);
    }
    
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Test
    public void test(){
        SAnimal cat = new SAnimal("cat");
        JPASQLQuery query = query().from(cat);
        
        query.count();
        assertProjectionEmpty(query);
        query.countDistinct();
        assertProjectionEmpty(query);
        
        query.iterate(cat.id);
        assertProjectionEmpty(query);
        query.iterate(cat.id,cat.name);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat.id);
        assertProjectionEmpty(query);
        query.iterateDistinct(cat.id,cat.name);
        assertProjectionEmpty(query);
        
        query.list(cat.id);
        assertProjectionEmpty(query);
        query.list(cat.id,cat.name);
        assertProjectionEmpty(query);
        query.listDistinct(cat.id);
        assertProjectionEmpty(query);
        query.listDistinct(cat.id,cat.name);
        assertProjectionEmpty(query);
        
        query.listResults(cat.id);
        assertProjectionEmpty(query);
        query.listDistinctResults(cat.id);
        assertProjectionEmpty(query);
        
        query.map(cat.id, cat.name);
        assertProjectionEmpty(query);
        
//        query.uniqueResult(cat.id);
//        assertProjectionEmpty(query);
        query.uniqueResult(cat.id,cat.name);
        assertProjectionEmpty(query);
        
    }
    
    @Test
    public void testClone(){
        SAnimal cat = new SAnimal("cat");
        JPASQLQuery query = query().from(cat).where(cat.name.isNotNull());        
        JPASQLQuery query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat.id);
    }

    private void assertProjectionEmpty(JPASQLQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
