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
package com.querydsl.sql;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.testutil.IncludeIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.assertEquals;

public class UpdateBase extends AbstractBaseTest {

    protected void reset() throws SQLException{
        delete(survey).execute();
        insert(survey).values(1, "Hello World", "Hello").execute();
    }

    @Before
    public void setUp() throws SQLException{
        reset();
    }

    @After
    public void tearDown() throws SQLException{
        reset();
    }

    @Test
    public void Update() throws SQLException{
        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(survey.name, "S").execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with full update count
        assertEquals(count, update(survey).set(survey.name, "S").execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());
    }

    @Test
    @IncludeIn({CUBRID, H2, MYSQL, ORACLE, SQLSERVER})
    public void Update_Limit() {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        assertEquals(2, update(survey).set(survey.name, "S").limit(2).execute());
    }

    @Test
    public void Update2() throws SQLException{
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList("S");

        // original state
        long count = query().from(survey).count();
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with 0 update count
        assertEquals(0, update(survey).where(survey.name.eq("XXX")).set(paths, values).execute());
        assertEquals(0, query().from(survey).where(survey.name.eq("S")).count());

        // update call with full update count
        assertEquals(count, update(survey).set(paths, values).execute());
        assertEquals(count, query().from(survey).where(survey.name.eq("S")).count());

    }

    @Test
    public void Update3() {
        assertEquals(1, update(survey).set(survey.name, survey.name.append("X")).execute());
    }

    @Test
    public void Update4() {
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, update(survey).set(survey.name, "AA").where(survey.name.eq("A")).execute());
    }

    @Test
    public void Update5() {
        assertEquals(1, insert(survey).values(3, "B","C").execute());
        assertEquals(1, update(survey).set(survey.name, "BB").where(survey.name.eq("B")).execute());
    }

    @Test
    public void SetNull() {
        List<Path<?>> paths = Collections.<Path<?>>singletonList(survey.name);
        List<?> values = Collections.singletonList(null);
        long count = query().from(survey).count();
        assertEquals(count, update(survey).set(paths, values).execute());
    }

    @Test
    public void SetNull2() {
        long count = query().from(survey).count();
        assertEquals(count, update(survey).set(survey.name, (String)null).execute());
    }

    @Test
    public void Batch() throws SQLException{
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        SQLUpdateClause update = update(survey);
        update.set(survey.name, "AA").where(survey.name.eq("A")).addBatch();
        update.set(survey.name, "BB").where(survey.name.eq("B")).addBatch();
        assertEquals(2, update.execute());
    }

    @Test
    public void Batch_Templates() throws SQLException{
        assertEquals(1, insert(survey).values(2, "A","B").execute());
        assertEquals(1, insert(survey).values(3, "B","C").execute());

        SQLUpdateClause update = update(survey);
        update.set(survey.name, "AA").where(survey.name.eq(Expressions.stringTemplate("'A'"))).addBatch();
        update.set(survey.name, "BB").where(survey.name.eq(Expressions.stringTemplate("'B'"))).addBatch();
        assertEquals(2, update.execute());
    }

    @Test
    public void Update_with_SubQuery_exists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(new SQLSubQuery().from(employee).where(survey1.id.eq(employee.id)).exists());
        update.execute();
    }

    @Test
    public void Update_with_SubQuery_exists_Params() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");

        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        SQLSubQuery sq = sq().from(employee).where(employee.id.eq(param));
        sq.set(param, -12478923);

        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(sq.exists());
        update.execute();
    }

    @Test
    public void Update_with_SubQuery_exists2() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(new SQLSubQuery().from(employee).where(survey1.name.eq(employee.lastname)).exists());
        update.execute();
    }

    @Test
    public void Update_with_SubQuery_notExists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLUpdateClause update = update(survey1);
        update.set(survey1.name, "AA");
        update.where(sq().from(employee).where(survey1.id.eq(employee.id)).notExists());
        update.execute();
    }

    @Test
    public void Update_With_TempateExpression_In_Batch() {
        update(survey)
            .set(survey.id, 3)
            .set(survey.name, Expressions.stringTemplate("'Hello'"))
            .addBatch();
    }

}
