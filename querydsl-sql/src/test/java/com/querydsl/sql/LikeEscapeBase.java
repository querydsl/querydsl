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
package com.querydsl.sql;

import static com.querydsl.sql.Constants.survey;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.sql.dml.SQLInsertClause;

public class LikeEscapeBase extends AbstractBaseTest {

    @Before
    public void setUp() throws SQLException {
        delete(survey).execute();
        SQLInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        insert.execute();
    }

    @After
    public void tearDown() throws SQLException {
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();
    }

    @Test
    @Ignore
    public void Like() {
        assertEquals(1L, query().from(survey).where(survey.name.like("a!%")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.like("a!_")).fetchCount());
        assertEquals(3L, query().from(survey).where(survey.name.like("a%")).fetchCount());
        assertEquals(2L, query().from(survey).where(survey.name.like("a_")).fetchCount());

        assertEquals(1L, query().from(survey).where(survey.name.startsWith("a_")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.startsWith("a%")).fetchCount());
    }

    @Test
    public void Like_with_Escape() {
        assertEquals(1L, query().from(survey).where(survey.name.like("a!%", '!')).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.like("a!_", '!')).fetchCount());
        assertEquals(3L, query().from(survey).where(survey.name.like("a%", '!')).fetchCount());
        assertEquals(2L, query().from(survey).where(survey.name.like("a_", '!')).fetchCount());
    }

}
