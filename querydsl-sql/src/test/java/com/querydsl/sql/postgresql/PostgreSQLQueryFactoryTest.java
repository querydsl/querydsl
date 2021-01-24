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
package com.querydsl.sql.postgresql;

import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.util.function.Supplier;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.domain.QSurvey;

public class PostgreSQLQueryFactoryTest {

    private PostgreSQLQueryFactory queryFactory;

    @Before
    public void setUp() {
        Supplier<Connection> provider = () -> EasyMock.createNiceMock(Connection.class);
        queryFactory = new PostgreSQLQueryFactory(SQLTemplates.DEFAULT, provider);
    }

    @Test
    public void query() {
        assertNotNull(queryFactory.query());
    }

    @Test
    public void from() {
        assertNotNull(queryFactory.from(QSurvey.survey));
    }

    @Test
    public void delete() {
        assertNotNull(queryFactory.delete(QSurvey.survey));
    }

    @Test
    public void insert() {
        assertNotNull(queryFactory.insert(QSurvey.survey));
    }

    @Test
    public void update() {
        assertNotNull(queryFactory.update(QSurvey.survey));
    }

    @Test
    public void merge() {
        assertNotNull(queryFactory.merge(QSurvey.survey));
    }

}
