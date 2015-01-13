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

import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;

import org.easymock.EasyMock;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.jpa.domain.QAnimal;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;

public class HibernateQueryFactoryTest {

    private HibernateQueryFactory queryFactory;

    @Before
    public void setUp() {
        Provider<Session> provider = new Provider<Session>() {
            @Override
            public Session get() {
                return EasyMock.createNiceMock(Session.class);
            }
        };
        queryFactory = new HibernateQueryFactory(JPQLTemplates.DEFAULT, provider);
    }

    @Test
    public void Query() {
        assertNotNull(queryFactory.query());
    }

    @Test
    public void SubQuery() {
        assertNotNull(queryFactory.subQuery());
    }

    @Test
    public void From() {
        assertNotNull(queryFactory.from(QAnimal.animal));
    }

    @Test
    public void Delete() {
        assertNotNull(queryFactory.delete(QAnimal.animal));
    }

    @Test
    public void Update() {
        assertNotNull(queryFactory.update(QAnimal.animal));
    }

}
