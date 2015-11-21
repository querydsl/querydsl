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

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Map;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.querydsl.jpa.domain.QAnimal;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class JPAQueryFactoryTest {

    private EntityManagerFactory factoryMock;

    private EntityManager mock;

    private JPAQueryFactory queryFactory;

    private JPQLQueryFactory queryFactory2;

    private JPAQueryFactory queryFactory3;

    private Map<String, Object> properties = Maps.newHashMap();

    @Before
    public void setUp() {
        factoryMock = EasyMock.createMock(EntityManagerFactory.class);
        mock = EasyMock.createMock(EntityManager.class);
        Provider<EntityManager> provider = new Provider<EntityManager>() {
            @Override
            public EntityManager get() {
                return mock;
            }
        };
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, provider);
        queryFactory2 = queryFactory;

        queryFactory3 = new JPAQueryFactory(provider);

    }

    @Test
    public void query() {
        assertNotNull(queryFactory.query());
    }

    @Test
    public void query2() {
        queryFactory2.query().from(QAnimal.animal);
    }

    @Test
    public void query3() {
        EasyMock.expect(mock.getEntityManagerFactory()).andReturn(factoryMock);
        EasyMock.expect(factoryMock.getProperties()).andReturn(properties);
        EasyMock.expect(mock.getDelegate()).andReturn(mock).atLeastOnce();
        EasyMock.replay(mock, factoryMock);

        queryFactory3.query().from(QAnimal.animal);

        EasyMock.verify(mock, factoryMock);
    }

    @Test
    public void from() {
        assertNotNull(queryFactory.from(QAnimal.animal));
    }

    @Test
    public void delete() {
        assertNotNull(queryFactory.delete(QAnimal.animal));
    }

    @Test
    public void delete2() {
        queryFactory2.delete(QAnimal.animal)
            .where(QAnimal.animal.bodyWeight.gt(0));
    }

    @Test
    public void delete3() {
        EasyMock.expect(mock.getEntityManagerFactory()).andReturn(factoryMock);
        EasyMock.expect(factoryMock.getProperties()).andReturn(properties);
        EasyMock.expect(mock.getDelegate()).andReturn(mock).atLeastOnce();
        EasyMock.replay(mock, factoryMock);

        assertNotNull(queryFactory3.delete(QAnimal.animal));

        EasyMock.verify(mock, factoryMock);
    }

    @Test
    public void update() {
        assertNotNull(queryFactory.update(QAnimal.animal));
    }

    @Test
    public void update2() {
        queryFactory2.update(QAnimal.animal)
            .set(QAnimal.animal.birthdate, new Date())
            .where(QAnimal.animal.birthdate.isNull());
    }

    @Test
    public void update3() {
        EasyMock.expect(mock.getEntityManagerFactory()).andReturn(factoryMock);
        EasyMock.expect(factoryMock.getProperties()).andReturn(properties);
        EasyMock.expect(mock.getDelegate()).andReturn(mock).atLeastOnce();
        EasyMock.replay(mock, factoryMock);

        assertNotNull(queryFactory3.update(QAnimal.animal));

        EasyMock.verify(mock, factoryMock);
    }

}
