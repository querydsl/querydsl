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
package com.querydsl.r2dbc;

import com.querydsl.r2dbc.dml.R2DBCInsertClause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.querydsl.r2dbc.Constants.survey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class LikeEscapeBase extends AbstractBaseTest {

    @Before
    public void setUp() {
        delete(survey).execute().block();
        R2DBCInsertClause insert = insert(survey);
        insert.set(survey.id, 5).set(survey.name, "aaa").addBatch();
        insert.set(survey.id, 6).set(survey.name, "a_").addBatch();
        insert.set(survey.id, 7).set(survey.name, "a%").addBatch();
        insert.execute().block();
    }

    @After
    public void tearDown() {
        delete(survey).execute().block();
        insert(survey).values(1, "Hello World", "Hello").execute().block();
    }

    @Test
    public void like() {
        assertEquals(0, (long) query().from(survey).where(survey.name.like("a!%")).fetchCount().block());
        assertEquals(0, (long) query().from(survey).where(survey.name.like("a!_")).fetchCount().block());
        assertEquals(3, (long) query().from(survey).where(survey.name.like("a%")).fetchCount().block());
        assertEquals(2, (long) query().from(survey).where(survey.name.like("a_")).fetchCount().block());

        assertEquals(1, (long) query().from(survey).where(survey.name.startsWith("a_")).fetchCount().block());
        assertEquals(1, (long) query().from(survey).where(survey.name.startsWith("a%")).fetchCount().block());
    }

    @Test
    public void like_with_escape() {
        assertEquals(1, (long) query().from(survey).where(survey.name.like("a!%", '!')).fetchCount().block());
        assertEquals(1, (long) query().from(survey).where(survey.name.like("a!_", '!')).fetchCount().block());
        assertEquals(3, (long) query().from(survey).where(survey.name.like("a%", '!')).fetchCount().block());
        assertEquals(2, (long) query().from(survey).where(survey.name.like("a_", '!')).fetchCount().block());
    }

    @Test
    public void like_escaping_conclusion() {
        assertTrue("Escaped like construct must return more results",
                query().from(survey).where(survey.name.like("a!%")).fetchCount().block()
                        < query().from(survey).where(survey.name.like("a!%", '!')).fetchCount().block());
        assertTrue("Escaped like construct must return more results",
                query().from(survey).where(survey.name.like("a!_")).fetchCount().block()
                        < query().from(survey).where(survey.name.like("a!_", '!')).fetchCount().block());
    }
}
