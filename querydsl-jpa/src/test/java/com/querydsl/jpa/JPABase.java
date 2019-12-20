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

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static com.querydsl.jpa.JPAExpressions.selectOne;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Target;
import com.querydsl.core.Tuple;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.domain.*;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
public class JPABase extends AbstractJPATest implements JPATest {

    private static final QCat cat = QCat.cat;

    @Rule
    @ClassRule
    public static TestRule targetRule = new TargetRule();

    @Rule
    @ClassRule
    public static TestRule jpaProviderRule = new JPAProviderRule();

    private EntityManager entityManager;

    @Override
    protected JPAQuery<?> query() {
        return new JPAQuery<Void>(entityManager);
    }

    protected JPADeleteClause delete(EntityPath<?> path) {
        return new JPADeleteClause(entityManager, path);
    }

    @Override
    protected JPAQuery<?> testQuery() {
        return new JPAQuery<Void>(entityManager, new DefaultQueryMetadata());
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void save(Object entity) {
        entityManager.persist(entity);
    }

    @Test
    @NoEclipseLink
    @NoOpenJPA
    @NoHibernate
    public void connection_access() {
        assertNotNull(query().from(cat).select(cat).createQuery().unwrap(Connection.class));
    }

    @Test
    @Ignore
    public void delete() {
        delete(cat).execute();
    }

    @Test
    public void delete2() {
        assertEquals(0, delete(QGroup.group).execute());
    }

    @Test
    @NoBatooJPA
    public void delete_where() {
        delete(cat).where(cat.name.eq("XXX")).execute();
    }

    @Test
    @ExcludeIn(Target.MYSQL)
    public void delete_where_any() {
        delete(cat).where(cat.kittens.any().name.eq("XXX")).execute();
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(Target.MYSQL)
    public void delete_where_subQuery_exists() {
        QCat parent = cat;
        QCat child = new QCat("kitten");

        delete(child)
            .where(child.id.eq(-100), selectOne().from(parent)
               .where(parent.id.eq(-200),
                      child.in(parent.kittens)).exists())
            .execute();
    }

    @Test
    @NoBatooJPA
    public void delete_where_subQuery2() {
        QChild child = QChild.child;
        QParent parent = QParent.parent;

        JPQLQuery<?> subQuery = selectFrom(parent)
            .where(parent.id.eq(2),
                    child.parent.eq(parent));
                   //child.in(parent.children));

        delete(child)
            .where(child.id.eq(1), subQuery.exists())
            .execute();
    }

    @Test
    public void finder() {
        Map<String,Object> conditions = new HashMap<String,Object>();
        conditions.put("name", "Bob123");

        List<Cat> cats = CustomFinder.findCustom(entityManager, Cat.class, conditions, "name");
        assertEquals(1, cats.size());
        assertEquals("Bob123", cats.get(0).getName());
    }

    @Test
    public void flushMode() {
        assertFalse(query().from(cat).setFlushMode(FlushModeType.AUTO).select(cat).fetch().isEmpty());
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    public void hint() {
        javax.persistence.Query query = query().from(cat)
                .setHint("org.hibernate.cacheable", true)
                .select(cat).createQuery();

        assertNotNull(query);
        assertTrue(query.getHints().containsKey("org.hibernate.cacheable"));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void hint2() {
        assertFalse(query().from(cat).setHint("org.hibernate.cacheable", true)
                .select(cat).fetch().isEmpty());
    }

    @Test @Ignore
    @NoHibernate @NoOpenJPA @NoBatooJPA
    public void hint3() {
        javax.persistence.Query query = query().from(cat)
                .setHint("eclipselink.batch.type", "IN")
                .setHint("eclipselink.batch", "person.workAddress")
                .setHint("eclipselink.batch", "person.homeAddress")
                .select(cat).createQuery();

        assertNotNull(query);
        assertEquals("person.homeAddress", query.getHints().get("eclipselink.batch"));
    }

    @Test
    @ExcludeIn(Target.DERBY)
    public void iterate() {
        CloseableIterator<Cat> cats = query().from(cat).select(cat).iterate();
        while (cats.hasNext()) {
            Cat cat = cats.next();
            assertNotNull(cat);
        }
        cats.close();
    }

    @Test
    public void limit1_uniqueResult() {
        assertNotNull(query().from(cat).limit(1).select(cat).fetchOne());
    }

    @Test
    public void lockMode() {
        javax.persistence.Query query = query().from(cat)
                .setLockMode(LockModeType.PESSIMISTIC_READ)
                .select(cat).createQuery();
        assertTrue(query.getLockMode().equals(LockModeType.PESSIMISTIC_READ));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void lockMode2() {
        assertFalse(query().from(cat).setLockMode(LockModeType.PESSIMISTIC_READ)
                .select(cat).fetch().isEmpty());
    }

    @Test
    public void queryExposure() {
        //save(new Cat(20));
        List<Cat> results = query().from(cat)
                .select(cat).createQuery().getResultList();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @Ignore // isn't a valid JPQL query
    public void subquery_uniqueResult() {
        QCat cat2 = new QCat("cat2");

        BooleanExpression exists = selectOne().from(cat2).where(cat2.eyecolor.isNotNull()).exists();
        assertNotNull(query().from(cat)
                .where(cat.breed.eq(0).not())
                .select(new QCatSummary(cat.breed.count(), exists)).fetchOne());
    }

    @SuppressWarnings("unchecked")
    @Test
    @NoEclipseLink
    @NoBatooJPA
    public void createQuery() {
        List<Object[]> rows = query().from(cat)
                .select(cat.id, cat.name).createQuery().getResultList();
        for (Object[] row : rows) {
            assertEquals(2, row.length);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    @NoEclipseLink
    @NoBatooJPA
    public void createQuery2() {
        List<Object[]> rows = query().from(cat)
                .select(cat.id, cat.name).createQuery().getResultList();
        for (Object[] row : rows) {
            assertEquals(2, row.length);
        }
    }

    @Test
    public void createQuery3() {
        List<String> rows = query().from(cat)
                .select(cat.name).createQuery().getResultList();
        for (String row : rows) {
            assertTrue(row instanceof String);
        }
    }
}
