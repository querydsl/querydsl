/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.core.QueryMutability;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.sql.SAnimal;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.DerbyTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.jpa.testutil.JPATestRunner;

@Ignore
@RunWith(JPATestRunner.class)
public class JPAQueryMutabilityTest implements JPATest {

    private static final SQLTemplates derbyTemplates = new DerbyTemplates();

    private EntityManager entityManager;

    protected JPASQLQuery query() {
        return new JPASQLQuery(entityManager, derbyTemplates);
    }

    @Override
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
    public void Clone() {
        SAnimal cat = new SAnimal("cat");
        JPASQLQuery query = query().from(cat).where(cat.name.isNotNull());
        JPASQLQuery query2 = query.clone(entityManager);
        assertEquals(query.getMetadata().getJoins(), query2.getMetadata().getJoins());
        assertEquals(query.getMetadata().getWhere(), query2.getMetadata().getWhere());
        query2.list(cat.id);
    }

}
