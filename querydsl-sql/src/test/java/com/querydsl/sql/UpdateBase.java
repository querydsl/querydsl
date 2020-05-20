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

import static com.querydsl.core.Target.*;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.sql.SQLExpressions.selectOne;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;

public class UpdateBase extends AbstractBaseTest {

    protected void reset() throws SQLException {
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();
    }

    @Before
    public void setUp() throws SQLException {
        reset();
    }

    @After
    public void tearDown() throws SQLException {
        reset();
    }

    @Test
    public void update() throws SQLException {
        // original state
        long count = query().from(survey).fetchCount();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).fetchCount());

        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(survey.name, "S").execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).fetchCount());

        // update call with full update count
        assertEquals(count, update(survey).set(survey.name, "S").execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).fetchCount());
    }

    @Test
    @IncludeIn({CUBRID, H2, MYSQL, ORACLE, SQLSERVER})
    public void update_limit() {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        assertEquals(2, update(survey).set(survey.name, "S").limit(2).execute());
    }

    @Test
    public void update2() throws SQLException {
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList("S");

        // original state
        long count = query().from(survey).fetchCount();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).fetchCount());

        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(paths, values).execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).fetchCount());

        // update call with full update count
        assertEquals(count, update(survey).set(paths, values).execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).fetchCount());

    }

    @Test
    public void update3() {
        assertEquals(1, update(survey).set(survey.name, survey.name.append("X")).execute());
    }

    @Test
    public void update4() {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, update(survey).set(survey.name, "AA").where(survey.name.eq("A")).execute());
    }

    @Test
    public void update5() {
        assertEquals(1, insert(survey).values(3, "B","C").execute());
        assertEquals(1, update(survey).set(survey.name, "BB").where(survey.name.eq("B")).execute());
    }

    @Test
    public void setNull() {
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList(null);
        long count = query().from(survey).fetchCount();
        assertEquals(count, update(survey).set(paths, values).execute());
    }

    @Test
    public void setNull2() {
        long count = query().from(survey).fetchCount();
        assertEquals(count, update(survey).set(survey.name, (String) null).execute());
    }

    @Test
    @SkipForQuoted
    @ExcludeIn({DB2, DERBY})
    public void setNullEmptyRootPath() {
        StringPath name = Expressions.stringPath("name");
        long count = query().from(survey).fetchCount();
        assertEquals(count, execute(update(survey).setNull(name)));
    }

    @Test
    public void batch() throws SQLException {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        SQLUpdateClause update = update(survey);
        update.set(survey.name, "AA").where(survey.name.eq("A")).addBatch();
        assertEquals(1, update.getBatchCount());
        update.set(survey.name, "BB").where(survey.name.eq("B")).addBatch();
        assertEquals(2, update.getBatchCount());
        assertEquals(2, update.execute());
    }

    @Test
    public void batch_templates() throws SQLException {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        SQLUpdateClause update = update(survey);
        update.set(survey.name, "AA").where(survey.name.eq(Expressions.stringTemplate("'A'"))).addBatch();
        update.set(survey.name, "BB").where(survey.name.eq(Expressions.stringTemplate("'B'"))).addBatch();
        assertEquals(2, update.execute());
    }

    @Test
    public void update_with_subQuery_exists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(selectOne().from(employee).where(survey1.id.eq(employee.id)).exists());
        assertEquals(1, update.execute());
    }

    @Test
    public void update_with_subQuery_exists_Params() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");

        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        SQLQuery<?> sq = query().from(employee).where(employee.id.eq(param));
        sq.set(param, -12478923);

        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(sq.exists());
        assertEquals(0, update.execute());
    }

    @Test
    public void update_with_subQuery_exists2() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(selectOne().from(employee).where(survey1.name.eq(employee.lastname)).exists());
        assertEquals(0, update.execute());
    }

    @Test
    public void update_with_subQuery_notExists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(query().from(employee).where(survey1.id.eq(employee.id)).notExists());
        assertEquals(0, update.execute());
    }

    @Test
    @ExcludeIn(TERADATA)
    public void update_with_templateExpression_in_batch() {
        assertEquals(1, update(survey)
            .set(survey.id, 3)
            .set(survey.name, Expressions.stringTemplate("'Hello'"))
            .addBatch()
            .execute());
    }

}
