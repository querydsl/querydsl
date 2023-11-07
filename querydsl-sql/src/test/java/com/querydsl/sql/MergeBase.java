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
import static com.querydsl.sql.Constants.survey2;
import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.querydsl.core.types.Path;
import com.querydsl.sql.dml.SQLMergeUsingClause;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.testutil.ExcludeIn;
import com.querydsl.core.testutil.IncludeIn;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.domain.QSurvey;

public class MergeBase extends AbstractBaseTest {

    private void reset() throws SQLException {
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
    @ExcludeIn({H2, CUBRID, SQLSERVER})
    public void merge_with_keys() throws SQLException {
        ResultSet rs = merge(survey).keys(survey.id)
                .set(survey.id, 7)
                .set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }

    @Test
    @ExcludeIn({H2, CUBRID, SQLSERVER})
    public void merge_with_keys_listener() throws SQLException {
        final AtomicBoolean result = new AtomicBoolean();
        SQLListener listener = new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                result.set(true);
            }
        };
        SQLMergeClause clause = merge(survey).keys(survey.id)
                .set(survey.id, 7)
                .set(survey.name, "Hello World");
        clause.addListener(listener);
        ResultSet rs = clause.executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
        assertTrue(result.get());
    }

    @Test
    @IncludeIn(H2)
    public void merge_with_keys_and_subQuery() {
        assertEquals(1, insert(survey).set(survey.id, 6).set(survey.name, "H").execute());

        // keys + subquery
        QSurvey survey2 = new QSurvey("survey2");
        assertEquals(2, merge(survey).keys(survey.id).select(
                query().from(survey2).select(survey2.id.add(1), survey2.name, survey2.name2)).execute());
    }

    @Test
    @IncludeIn(H2)
    public void merge_with_keys_and_values() {
        // NOTE : doesn't work with composite merge implementation
        // keys + values
        assertEquals(1, merge(survey).keys(survey.id).values(5, "Hello World", "Hello").execute());
    }

    @Test
    public void merge_with_keys_columns_and_values() {
        // keys + columns + values
        assertEquals(1, merge(survey).keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, "Hello World").execute());
    }

    @Test
    public void merge_with_keys_columns_and_values_using_null() {
        // keys + columns + values
        assertEquals(1, merge(survey).keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, (String) null).execute());
    }

    @Test
    @ExcludeIn({CUBRID, DB2, DERBY, POSTGRESQL, SQLSERVER, TERADATA})
    public void merge_with_keys_Null_Id() throws SQLException {
        ResultSet rs = merge(survey).keys(survey.id)
                .setNull(survey.id)
                .set(survey.name, "Hello World").executeWithKeys();
        assertTrue(rs.next());
        assertTrue(rs.getObject(1) != null);
        rs.close();
    }

    @Test
    @ExcludeIn({H2, CUBRID, SQLSERVER})
    public void merge_with_keys_Projected() throws SQLException {
        assertNotNull(merge(survey).keys(survey.id)
                .set(survey.id, 8)
                .set(survey.name, "Hello you").executeWithKey(survey.id));
    }

    @Test
    @ExcludeIn({H2, CUBRID, SQLSERVER})
    public void merge_with_keys_Projected2() throws SQLException {
        Path<Object> idPath = ExpressionUtils.path(Object.class, "id");
        Object id = merge(survey).keys(survey.id)
                .set(survey.id, 9)
                .set(survey.name, "Hello you").executeWithKey(idPath);
        assertNotNull(id);
    }

    @Test
    @IncludeIn(H2)
    public void mergeBatch() {
        SQLMergeClause merge = merge(survey)
            .keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, "5")
            .addBatch();
        assertEquals(1, merge.getBatchCount());
        assertFalse(merge.isEmpty());

        merge
            .keys(survey.id)
            .set(survey.id, 6)
            .set(survey.name, "6")
            .addBatch();

        assertEquals(2, merge.getBatchCount());
        assertEquals(2, merge.execute());

        assertEquals(1L, query().from(survey).where(survey.name.eq("5")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.eq("6")).fetchCount());
    }

    @Test
    @IncludeIn(H2)
    public void mergeBatch_templates() {
        SQLMergeClause merge = merge(survey)
            .keys(survey.id)
            .set(survey.id, 5)
            .set(survey.name, Expressions.stringTemplate("'5'"))
            .addBatch();

        merge
            .keys(survey.id)
            .set(survey.id, 6)
            .set(survey.name, Expressions.stringTemplate("'6'"))
            .addBatch();

        assertEquals(2, merge.execute());

        assertEquals(1L, query().from(survey).where(survey.name.eq("5")).fetchCount());
        assertEquals(1L, query().from(survey).where(survey.name.eq("6")).fetchCount());
    }


    @Test
    @IncludeIn(H2)
    public void mergeBatch_with_subquery() {
        SQLMergeClause merge = merge(survey)
            .keys(survey.id)
            .columns(survey.id, survey.name)
            .select(query().from(survey2).select(survey2.id.add(20), survey2.name))
            .addBatch();

        merge(survey)
            .keys(survey.id)
            .columns(survey.id, survey.name)
            .select(query().from(survey2).select(survey2.id.add(40), survey2.name))
            .addBatch();

        assertEquals(1, merge.execute());
    }

    @Test
    @IncludeIn(H2)
    public void merge_with_templateExpression_in_batch() {
        SQLMergeClause merge = merge(survey)
                .keys(survey.id)
                .set(survey.id, 5)
                .set(survey.name, Expressions.stringTemplate("'5'"))
                .addBatch();

        assertEquals(1, merge.execute());
    }

    @Test
    @IncludeIn({DB2, SQLSERVER})
    public void merge_with_using() {
        QSurvey usingSubqueryAlias = new QSurvey("USING_SUBSELECT");
        SQLMergeUsingClause merge = merge(survey)
                .using(query().from(survey2).select(survey2.id.add(40).as("ID"), survey2.name).as(usingSubqueryAlias))
                .on(survey.id.eq(usingSubqueryAlias.id))
                .whenNotMatched().thenInsert(Arrays.asList(survey.id, survey.name), Arrays.asList(usingSubqueryAlias.id, usingSubqueryAlias.name))
                .whenMatched().and(survey.id.goe(10)).thenDelete()
                .whenMatched().thenUpdate(Collections.singletonList(survey.name), Collections.singletonList(usingSubqueryAlias.name));

        assertEquals(1, merge.execute());
    }

    @Test
    public void merge_listener() {
        final AtomicInteger calls = new AtomicInteger(0);
        SQLListener listener = new SQLBaseListener() {
            @Override
            public void end(SQLListenerContext context) {
                if (context.getData(AbstractSQLQuery.PARENT_CONTEXT) == null) {
                    calls.incrementAndGet();
                }
            }
        };

        SQLMergeClause clause = merge(survey).keys(survey.id)
                .set(survey.id, 5)
                .set(survey.name, "Hello World");
        clause.addListener(listener);
        assertEquals(1, clause.execute());
        assertEquals(1, calls.intValue());
    }

}
