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

import java.io.IOException;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Session;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.hibernate.DefaultSessionHolder;
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
    public static MethodRule jpaProviderRule = new JPAProviderRule();

    @Rule
    public static MethodRule targetRule = new TargetRule();

    private Session session;

    @Override
    protected HibernateQuery<Void> query() {
        return new HibernateQuery<Void>(session, getTemplates());
    }

    @Override
    protected HibernateQuery<Void> testQuery() {
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
    public void QueryExposure() {
//        save(new Cat());
        List<Cat> results = query().from(cat).select(cat).createQuery().list();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }

    @Test
    public void WithComment() {
        query().from(cat).setComment("my comment").select(cat).fetch();
    }

    @Test
    public void LockMode() {
        query().from(cat).setLockMode(cat, LockMode.PESSIMISTIC_WRITE).select(cat).fetch();
    }

    @Test
    public void FlushMode() {
        query().from(cat).setFlushMode(org.hibernate.FlushMode.AUTO).select(cat).fetch();
    }

    @Test
    public void Scroll() throws IOException {
        CloseableIterator<Cat> cats = new ScrollableResultsIterator<Cat>(query().from(cat)
                .select(cat).createQuery().scroll());
        assertTrue(cats.hasNext());
        while (cats.hasNext()) {
            assertNotNull(cats.next());
        }
        cats.close();
    }

    @Test
    public void ScrollTuple() throws IOException {
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

    @Test
    public void createQuery() {
        List<Tuple> rows = query().from(cat).select(cat.id, cat.name).createQuery().list();
        for (Tuple row : rows) {
            assertEquals(2, row.size());
        }
    }

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
