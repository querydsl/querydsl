/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.hql.domain.QCat;
import com.mysema.query.hql.hibernate.HibernateQuery;
import com.mysema.testutil.HibernateConfig;
import com.mysema.testutil.HibernateTestRunner;

@RunWith(HibernateTestRunner.class)
@HibernateConfig("derby.properties")
public class HibernateQueryMutabilityTest{
    
    private Session session;
    
    protected HibernateQuery query(){
        return new HibernateQuery(session);
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    @Test
    public void test() throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IOException {
        QCat cat = QCat.cat;
        HibernateQuery query = query().from(cat);
        new QueryMutability(query).test(cat, cat.name);
    }
    
    @Test
    public void testClone(){
        QCat cat = QCat.cat;
        HibernateQuery query = query().from(cat).where(cat.name.isNotNull());        
        HibernateQuery query2 = query.clone(session);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat);
    }

}
