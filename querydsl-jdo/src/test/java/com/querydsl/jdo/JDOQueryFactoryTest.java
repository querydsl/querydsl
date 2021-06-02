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
package com.querydsl.jdo;

import static org.junit.Assert.assertNotNull;

import javax.jdo.PersistenceManager;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.jdo.test.domain.QProduct;

import java.util.function.Supplier;

public class JDOQueryFactoryTest {

    private JDOQueryFactory queryFactory;

    @Before
    public void setUp() {
        Supplier<PersistenceManager> provider = () -> EasyMock.<PersistenceManager> createNiceMock(PersistenceManager.class);
        queryFactory = new JDOQueryFactory(provider);
    }

    @Test
    public void query() {
        assertNotNull(queryFactory.query());
    }

    @Test
    public void from() {
        assertNotNull(queryFactory.from(QProduct.product));
    }

    @Test
    public void delete() {
        assertNotNull(queryFactory.delete(QProduct.product));
    }

}
