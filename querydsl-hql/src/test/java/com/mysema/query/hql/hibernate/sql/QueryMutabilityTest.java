/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.hql.domain.sql.SAnimal;
import com.mysema.query.sql.DerbyTemplates;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("derby.properties")
public class QueryMutabilityTest{
    
    private static final SQLTemplates derbyTemplates = new DerbyTemplates();
    
    private Session session;
    
    protected HibernateSQLQuery query(){
        return new HibernateSQLQuery(session, derbyTemplates);
    }
    public void setSession(Session session) {
        this.session = session;
    }
    
    @Test
    public void test(){
        SAnimal cat = new SAnimal("cat");
        HibernateSQLQuery query = query().from(cat);
        
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
        
        query.uniqueResult(cat.id);
        assertProjectionEmpty(query);
        query.uniqueResult(cat.id,cat.name);
        assertProjectionEmpty(query);
        
    }
    
    @Test
    public void testClone(){
        SAnimal cat = new SAnimal("cat");
        HibernateSQLQuery query = query().from(cat).where(cat.name.isNotNull());        
        HibernateSQLQuery query2 = query.clone(session);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat.id);
    }

    private void assertProjectionEmpty(HibernateSQLQuery query) {
        assertTrue(query.getMetadata().getProjection().isEmpty());        
    }

}
