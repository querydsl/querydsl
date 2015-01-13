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

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateUpdateClause;
import com.querydsl.jpa.hibernate.HibernateUtil;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.testutil.HibernateTestRunner;

import antlr.RecognitionException;
import antlr.TokenStreamException;

@RunWith(HibernateTestRunner.class)
public class IntegrationBase extends ParsingTest implements HibernateTest {

    private Session session;

    @Override
    protected QueryHelper query() {
        return new QueryHelper(HQLTemplates.DEFAULT) {
            @Override
            public void parse() throws RecognitionException, TokenStreamException {
                try {
                    System.out.println("querydsl : " + toString().replace('\n', ' '));
                    JPQLSerializer serializer = new JPQLSerializer(HQLTemplates.DEFAULT);
                    serializer.serialize(getMetadata(), false, null);
                    Query query = session.createQuery(serializer.toString());
                    HibernateUtil.setConstants(query, serializer.getConstantToLabel(), getMetadata().getParams());
                    query.list();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } finally {
                    System.out.println();
                }
            }
        };
    }

    @Override
    @Test
    public void GroupBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }
    
    @Override
    @Test
    public void GroupBy_2() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Override
    @Test
    public void OrderBy() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    @Override
    @Test
    public void DocoExamples910() throws Exception {
        // NOTE : commented out, because HQLSDB doesn't support these queries
    }

    private HibernateDeleteClause delete(EntityPath<?> entity) {
        return new HibernateDeleteClause(session, entity);
    }

    private HibernateUpdateClause update(EntityPath<?> entity) {
        return new HibernateUpdateClause(session, entity);
    }

    @Test
    public void Scroll() {
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));

        QCat cat = QCat.cat;
        HibernateQuery query = new HibernateQuery(session);
        ScrollableResults results = query.from(cat).scroll(ScrollMode.SCROLL_INSENSITIVE, cat);
        while (results.next()) {
            System.out.println(results.get(0));
        }
        results.close();
    }

    @Test
    public void Update() {
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));

        QCat cat = QCat.cat;
        long amount = update(cat).where(cat.name.eq("Bob"))
            .set(cat.name, "Bobby")
            .set(cat.alive, false)
            .execute();
        assertEquals(1, amount);

        assertEquals(0l, query().from(cat).where(cat.name.eq("Bob")).count());
    }

    @Test
    public void Update_with_null() {
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));

        QCat cat = QCat.cat;
        long amount = update(cat).where(cat.name.eq("Bob"))
            .set(cat.name, (String)null)
            .set(cat.alive, false)
            .execute();
        assertEquals(1, amount);
    }

    @Test
    public void Delete() {
        session.save(new Cat("Bob",10));
        session.save(new Cat("Steve",11));

        QCat cat = QCat.cat;
        long amount = delete(cat).where(cat.name.eq("Bob"))
            .execute();
        assertEquals(1, amount);
    }

    @Test
    public void Collection() throws Exception{
        List<Cat> cats = Arrays.asList(new Cat("Bob",10), new Cat("Steve",11));
        for (Cat cat : cats) {
            session.save(cat);
        }

        query().from(cat)
            .innerJoin(cat.kittens, kitten)
            .where(kitten.in(cats))
            .parse();

    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

}
