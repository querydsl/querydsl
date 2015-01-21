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

import static com.querydsl.jpa.domain.QCat.cat;
import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.querydsl.jpa.domain.Cat;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.testutil.HibernateTestRunner;

@Ignore
@RunWith(HibernateTestRunner.class)
public class UniqueResultsTest implements HibernateTest {

    private Session session;

    @Test
    public void test() {
        session.save(new Cat("Bob1", 1));
        session.save(new Cat("Bob2", 2));
        session.save(new Cat("Bob3", 3));

        assertEquals(Integer.valueOf(1), query().from(cat).orderBy(cat.name.asc()).offset(0).limit(1).uniqueResult(cat.id));
        assertEquals(Integer.valueOf(2), query().from(cat).orderBy(cat.name.asc()).offset(1).limit(1).uniqueResult(cat.id));
        assertEquals(Integer.valueOf(3), query().from(cat).orderBy(cat.name.asc()).offset(2).limit(1).uniqueResult(cat.id));

        assertEquals(Long.valueOf(3), query().from(cat).uniqueResult(cat.count()));
    }

    private HibernateQuery query() {
        return new HibernateQuery(session);
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

}
