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

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Target;
import com.querydsl.jpa.domain.*;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.jpa.testutil.JPATestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(JPATestRunner.class)
public class JPABase extends AbstractJPATest implements JPATest {

    private static final QCat cat = QCat.cat;

    @Rule
    public static MethodRule targetRule = new TargetRule();

    @Rule
    public static MethodRule jpaProviderRule = new JPAProviderRule();

    private EntityManager entityManager;

    @Override
    protected JPAQuery query() {
        return new JPAQuery(entityManager);
    }

    protected JPADeleteClause delete(EntityPath<?> path) {
        return new JPADeleteClause(entityManager, path);
    }

    @Override
    protected JPAQuery testQuery() {
        return new JPAQuery(entityManager, new DefaultQueryMetadata().noValidate());
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
    public void Connection_Access() {
        assertNotNull(query().from(cat).createQuery(cat).unwrap(Connection.class));
    }

    @Test
    @Ignore
    public void Delete() {
        delete(cat).execute();
    }

    @Test
    @NoBatooJPA
    public void Delete_Where() {
        delete(cat).where(cat.name.eq("XXX")).execute();
    }

    @Test
    @ExcludeIn(Target.MYSQL)
    public void Delete_Where_Any() {
        delete(cat).where(cat.kittens.any().name.eq("XXX")).execute();
    }

    @Test
    @NoBatooJPA
    @ExcludeIn(Target.MYSQL)
    public void Delete_Where_SubQuery_Exists() {
        QCat parent = cat;
        QCat child = new QCat("kitten");

        delete(child)
            .where(child.id.eq(-100), new JPASubQuery()
               .from(parent)
               .where(parent.id.eq(-200),
                      child.in(parent.kittens)).exists())
            .execute();
    }

    @Test
    @NoBatooJPA
    public void Delete_Where_SubQuery2() {
        QChild child = QChild.child;
        QParent parent = QParent.parent;

        JPASubQuery subQuery = new JPASubQuery()
            .from(parent)
            .where(parent.id.eq(2),
                   child.parent.eq(parent));
                   //child.in(parent.children));

        delete(child)
            .where(child.id.eq(1), subQuery.exists())
            .execute();
    }

    @Test
    public void Finder() {
        Map<String,Object> conditions = new HashMap<String,Object>();
        conditions.put("name", "Bob123");

        List<Cat> cats = CustomFinder.findCustom(entityManager, Cat.class, conditions, "name");
        assertEquals(1, cats.size());
        assertEquals("Bob123", cats.get(0).getName());
    }

    @Test
    public void FlushMode() {
        assertFalse(query().from(cat).setFlushMode(FlushModeType.AUTO).list(cat).isEmpty());
    }

    @Test
    @NoEclipseLink @NoOpenJPA
    public void Hint() {
        javax.persistence.Query query = query().from(cat)
                .setHint("org.hibernate.cacheable", true)
                .createQuery(cat);

        assertNotNull(query);
        assertTrue(query.getHints().containsKey("org.hibernate.cacheable"));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void Hint2() {
        assertFalse(query().from(cat).setHint("org.hibernate.cacheable", true)
                .list(cat).isEmpty());
    }

    @Test @Ignore
    @NoHibernate @NoOpenJPA @NoBatooJPA
    public void Hint3() {
        javax.persistence.Query query = query().from(cat)
                .setHint("eclipselink.batch.type", "IN")
                .setHint("eclipselink.batch", "person.workAddress")
                .setHint("eclipselink.batch", "person.homeAddress")
                .createQuery(cat);

        assertNotNull(query);
        assertEquals("person.homeAddress", query.getHints().get("eclipselink.batch"));
    }

    @Test
    @ExcludeIn(Target.DERBY)
    public void Iterate() {
        CloseableIterator<Cat> cats = query().from(cat).iterate(cat);
        while (cats.hasNext()) {
            Cat cat = cats.next();
            assertNotNull(cat);
        }
        cats.close();
    }

    @Test
    public void Limit1_UniqueResult() {
        assertNotNull(query().from(cat).limit(1).uniqueResult(cat));
    }

    @Test
    public void LockMode() {
        javax.persistence.Query query = query().from(cat)
                .setLockMode(LockModeType.PESSIMISTIC_READ).createQuery(cat);
        assertTrue(query.getLockMode().equals(LockModeType.PESSIMISTIC_READ));
        assertFalse(query.getResultList().isEmpty());
    }

    @Test
    public void LockMode2() {
        assertFalse(query().from(cat).setLockMode(LockModeType.PESSIMISTIC_READ)
                .list(cat).isEmpty());
    }

    @Test
    public void QueryExposure() {
        //save(new Cat(20));
        List<Cat> results = query().from(cat).createQuery(cat).getResultList();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    @Ignore // isn't a valid JPQL querydsl
    public void Subquery_UniqueResult() {
        QCat cat2 = new QCat("cat2");

        BooleanExpression exists = new JPASubQuery().from(cat2).where(cat2.eyecolor.isNotNull()).exists();
        assertNotNull(query().from(cat)
                .where(cat.breed.eq(0).not())
                .singleResult(new QCatSummary(cat.breed.count(), exists)));
    }

}
