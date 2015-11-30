/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import com.querydsl.core.Target;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.Color;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.sql.SAnimal;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.jpa.testutil.JPATestRunner;
import com.querydsl.sql.SQLTemplates;

@RunWith(JPATestRunner.class)
public class JPASQLBase extends AbstractSQLTest implements JPATest {

    @Rule
    @ClassRule
    public static TestRule targetRule = new TargetRule();

    @Rule
    @ClassRule
    public static TestRule hibernateOnly = new JPAProviderRule();

    private final SQLTemplates templates = Mode.getSQLTemplates();

    private EntityManager entityManager;

    @Override
    protected JPASQLQuery<?> query() {
        return new JPASQLQuery<Void>(entityManager, templates);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Before
    public void setUp() {
        if (query().from(cat).fetchCount() == 0) {
            entityManager.persist(new Cat("Beck", 1, Color.BLACK));
            entityManager.persist(new Cat("Kate", 2, Color.BLACK));
            entityManager.persist(new Cat("Kitty", 3, Color.BLACK));
            entityManager.persist(new Cat("Bobby", 4, Color.BLACK));
            entityManager.persist(new Cat("Harold", 5, Color.BLACK));
            entityManager.persist(new Cat("Tim", 6, Color.BLACK));
            entityManager.flush();
        }
    }

    @Test
    public void EntityQueries_CreateQuery() {
        SAnimal cat = new SAnimal("cat");
        QCat catEntity = QCat.cat;

        Query query = query().from(cat).select(catEntity).createQuery();
        assertEquals(6, query.getResultList().size());
    }

    @Test
    @ExcludeIn(Target.MYSQL)
    public void EntityQueries_CreateQuery2() {
        SAnimal cat = new SAnimal("CAT");
        QCat catEntity = QCat.cat;

        Query query = query().from(cat).select(catEntity).createQuery();
        assertEquals(6, query.getResultList().size());
    }

}
