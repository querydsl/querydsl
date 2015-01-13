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

import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.domain.QEmployee;
import com.querydsl.sql.domain.QSurvey;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.types.expr.Param;
import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static com.querydsl.sql.Constants.survey;
import static com.querydsl.core.Target.*;
import static org.junit.Assert.assertEquals;

public class DeleteBase extends AbstractBaseTest{

    private void reset() throws SQLException{
        delete(survey).where(survey.name.isNotNull()).execute();
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
    public void Batch() throws SQLException{
        insert(survey).values(2, "A","B").execute();
        insert(survey).values(3, "B","C").execute();

        SQLDeleteClause delete = delete(survey);
        delete.where(survey.name.eq("A")).addBatch();
        delete.where(survey.name.eq("B")).addBatch();
        assertEquals(2, delete.execute());
    }

    @Test
    @ExcludeIn({CUBRID, SQLITE})
    public void Batch_Templates() throws SQLException{
        insert(survey).values(2, "A","B").execute();
        insert(survey).values(3, "B","C").execute();

        SQLDeleteClause delete = delete(survey);
        delete.where(survey.name.eq(Expressions.stringTemplate("'A'"))).addBatch();
        delete.where(survey.name.eq(Expressions.stringTemplate("'B'"))).addBatch();
        assertEquals(2, delete.execute());
    }

    @Test
    @ExcludeIn(MYSQL)
    public void Delete() throws SQLException{
        long count = query().from(survey).count();
        assertEquals(0, delete(survey).where(survey.name.eq("XXX")).execute());
        assertEquals(count, delete(survey).execute());
    }

    @Test
    @IncludeIn({CUBRID, H2, MYSQL, ORACLE, SQLSERVER})
    public void Delete_Limit() {
        insert(survey).values(2, "A","B").execute();
        insert(survey).values(3, "B","C").execute();
        insert(survey).values(4, "D","E").execute();

        assertEquals(2, delete(survey).limit(2).execute());
    }

    @Test
    public void Delete_with_SubQuery_exists() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLDeleteClause delete = delete(survey1);
        delete.where(survey1.name.eq("XXX"),
                sq().from(employee).where(survey1.id.eq(employee.id)).exists());
        delete.execute();
    }

    @Test
    public void Delete_with_SubQuery_exists_Params() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");

        Param<Integer> param = new Param<Integer>(Integer.class, "param");
        SQLSubQuery sq = sq().from(employee).where(employee.id.eq(param));
        sq.set(param, -12478923);

        SQLDeleteClause delete = delete(survey1);
        delete.where(survey1.name.eq("XXX"), sq.exists());
        delete.execute();
    }

    @Test
    public void Delete_with_SubQuery_exists2() {
        QSurvey survey1 = new QSurvey("s1");
        QEmployee employee = new QEmployee("e");
        SQLDeleteClause delete = delete(survey1);
        delete.where(survey1.name.eq("XXX"),
                sq().from(employee).where(survey1.name.eq(employee.lastname)).exists());
        delete.execute();
    }


    @Test
    public void Delete_With_TempateExpression_In_Batch() {
        delete(survey)
            .where(Expressions.booleanTemplate("true"))
            .addBatch();
    }


}
