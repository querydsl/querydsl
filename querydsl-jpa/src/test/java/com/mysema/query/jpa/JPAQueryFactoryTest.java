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
package com.mysema.query.jpa;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.jpa.domain.QAnimal;
import com.mysema.query.jpa.impl.JPAQueryFactory;

public class JPAQueryFactoryTest {

    private JPAQueryFactory queryFactory;

    private JPQLQueryFactory queryFactory2;
    
    @Before
    public void setUp() {
        Provider<EntityManager> provider = new Provider<EntityManager>() {
            @Override
            public EntityManager get() {
                return EasyMock.createNiceMock(EntityManager.class);
            }
        };
        queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, provider);
        queryFactory2 = queryFactory;
    }

    @Test
    public void Query() {
        assertNotNull(queryFactory.query());
    }
    
    @Test
    public void Query2() {
        queryFactory2.query().from(QAnimal.animal);
    }

    @Test
    public void SubQuery() {
        assertNotNull(queryFactory.subQuery());
    }
    
    @Test
    public void SubQuery2() {
        queryFactory2.subQuery().from(QAnimal.animal);
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
    public void Delete2() {
        queryFactory2.delete(QAnimal.animal)
            .where(QAnimal.animal.bodyWeight.gt(0));
    }

    @Test
    public void Update() {
        assertNotNull(queryFactory.update(QAnimal.animal));
    }
    
    @Test
    public void Update2() {
        queryFactory2.update(QAnimal.animal)
            .set(QAnimal.animal.birthdate, new Date())
            .where(QAnimal.animal.birthdate.isNull());
    }

}
