/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate.sql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.QueryMutability;
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
    public void test() throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IOException {
        SAnimal cat = new SAnimal("cat");
        HibernateSQLQuery query = query().from(cat);
        new QueryMutability(query).test(cat.id, cat.name);
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
    
}
