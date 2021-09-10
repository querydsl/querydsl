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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import com.querydsl.core.CloseableIterator;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.domain.QGroup;
import com.querydsl.jpa.hibernate.DefaultSessionHolder;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.testutil.HibernateTestRunner;

/**
 * @author tiwe
 *
 */
@RunWith(HibernateTestRunner.class)
public class HibernateBase extends AbstractJPATest implements HibernateTest {

    private static final QCat cat = QCat.cat;

    @Rule
    @ClassRule
    public static TestRule jpaProviderRule = new JPAProviderRule();

    @Rule
    @ClassRule
    public static TestRule targetRule = new TargetRule();

    private Session session;

    @Override
    protected HibernateQuery<?> query() {
        return new HibernateQuery<Void>(session, getTemplates());
    }

    protected HibernateDeleteClause delete(EntityPath<?> path) {
        return new HibernateDeleteClause(session, path);
    }

    @Override
    protected HibernateQuery<?> testQuery() {
        return new HibernateQuery<Void>(new DefaultSessionHolder(session),
                getTemplates(), new DefaultQueryMetadata());
    }

    protected JPQLTemplates getTemplates() {
        return HQLTemplates.DEFAULT;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    protected void save(Object entity) {
        session.save(entity);
    }

    @Test
    public void query_exposure() {
//        save(new Cat());
        List<Cat> results = query().from(cat).select(cat).createQuery().list();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void delete() {
        assertEquals(0, delete(QGroup.group).execute());
    }

    @Test
    public void with_comment() {
        query().from(cat).setComment("my comment").select(cat).fetch();
    }

    @Test
    public void lockMode() {
        query().from(cat).setLockMode(cat, LockMode.PESSIMISTIC_WRITE).select(cat).fetch();
    }

    @Test
    public void flushMode() {
        query().from(cat).setFlushMode(org.hibernate.FlushMode.AUTO).select(cat).fetch();
    }

    @Test
    public void scroll() throws IOException {
        CloseableIterator<Cat> cats = new ScrollableResultsIterator<Cat>(query().from(cat)
                .select(cat).createQuery().scroll());
        assertTrue(cats.hasNext());
        while (cats.hasNext()) {
            assertNotNull(cats.next());
        }
        cats.close();
    }

    @Test
    public void scrollTuple() throws IOException {
        CloseableIterator<Tuple> rows = new ScrollableResultsIterator<Tuple>(query()
                .from(cat)
                .select(cat.name, cat.birthdate).createQuery().scroll());
        assertTrue(rows.hasNext());
        while (rows.hasNext()) {
            Tuple row = rows.next();
            assertEquals(2, row.size());
        }
        rows.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createQuery() {
        List<Tuple> rows = query().from(cat).select(cat.id, cat.name).createQuery().list();
        for (Tuple row : rows) {
            assertEquals(2, row.size());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void createQuery2() {
        List<Tuple> rows = query().from(cat).select(new Expression[]{cat.id, cat.name}).createQuery().list();
        for (Tuple row : rows) {
            assertEquals(2, row.size());
        }
    }

    @Test
    public void createQuery3() {
        List<String> rows = query().from(cat).select(cat.name).createQuery().list();
        for (String row : rows) {
            assertTrue(row instanceof String);
        }
    }

}
