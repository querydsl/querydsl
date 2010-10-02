/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.sql;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mysema.query.QueryMutability;
import com.mysema.query.jpa.domain.Cat;
import com.mysema.query.jpa.domain.sql.SAnimal;
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
    public void test() throws SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, IOException {
        entityManager.persist(new Cat("Beck", 1));
        entityManager.flush();

        SAnimal cat = new SAnimal("cat");
        JPASQLQuery query = query().from(cat);
        new QueryMutability(query).test(cat.id, cat.name);
    }

    @Test
    public void Clone(){
        SAnimal cat = new SAnimal("cat");
        JPASQLQuery query = query().from(cat).where(cat.name.isNotNull());
        JPASQLQuery query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat.id);
    }

}
