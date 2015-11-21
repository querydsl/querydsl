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

import org.junit.Before;
import org.junit.Test;

import com.querydsl.jpa.domain.QCat;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.impl.JPAQuery;

public class JPQLQueryTest {

    private QCat cat = QCat.cat;

    private HibernateQuery<?> query = new HibernateQuery<Void>();

    @Before
    public void setUp() {
        query.from(cat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void innerJoinPEntityOfPPEntityOfP() {
        query.innerJoin(cat.mate, cat.mate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void innerJoinPathOfQextendsCollectionOfPPathOfP() {
        query.innerJoin(cat.kittens, cat.mate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void joinPEntityOfPPEntityOfP() {
        query.join(cat.mate, cat.mate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void joinPathOfQextendsCollectionOfPPathOfP() {
        query.join(cat.kittens, cat.mate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void leftJoinPEntityOfPPEntityOfP() {
        query.leftJoin(cat.mate, cat.mate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void leftJoinPathOfQextendsCollectionOfPPathOfP() {
        query.leftJoin(cat.kittens, cat.mate);
    }

    @Test
    public void toString_() {
        assertEquals("", new HibernateQuery<Void>().toString());
        assertEquals("", new JPAQuery<Void>().toString());
        assertEquals("select cat", new HibernateQuery<Void>().select(cat).toString());
        assertEquals("select cat", new JPAQuery<Void>().select(cat).toString());
        assertEquals("select cat\nfrom Cat cat", new HibernateQuery<Void>().from(cat).toString());
        assertEquals("select cat\nfrom Cat cat", new JPAQuery<Void>().from(cat).toString());
    }

}
