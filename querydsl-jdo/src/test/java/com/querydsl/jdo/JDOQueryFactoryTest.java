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
package com.querydsl.jdo;

import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;
import javax.jdo.PersistenceManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.jdo.test.domain.QProduct;

public class JDOQueryFactoryTest {

    private JDOQueryFactory queryFactory;

    @Before
    public void setUp() {
        Provider<PersistenceManager> provider = new Provider<PersistenceManager>() {
            @Override
            public PersistenceManager get() {
                return EasyMock.createNiceMock(PersistenceManager.class);
            }
        };
        queryFactory = new JDOQueryFactory(provider);
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
        assertNotNull(queryFactory.from(QProduct.product));
    }

    @Test
    public void Delete() {
        assertNotNull(queryFactory.delete(QProduct.product));
    }

}
