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

import static com.querydsl.sql.SQLExpressions.select;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.TemplatesTestUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.sql.domain.QSurvey;

public abstract class AbstractSQLTemplatesTest {

    protected static final QSurvey survey1 = new QSurvey("survey1");

    protected static final QSurvey survey2 = new QSurvey("survey2");

    private SQLTemplates templates;

    protected SQLQuery<?> query;

    protected abstract SQLTemplates createTemplates();

    @Before
    public void setUp() {
        templates = createTemplates();
        templates.newLineToSingleSpace();
        query = new SQLQuery<Void>(new Configuration(templates));
    }

    @Test
    public void NoFrom() {
        query.getMetadata().setProjection(Expressions.ONE);
        if (templates.getDummyTable() == null) {
            assertEquals("select 1", query.toString());
        } else {
            assertEquals("select 1 from " + templates.getDummyTable(), query.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        Path<Integer> col1 = Expressions.path(Integer.class,"col1");
        Union union = query.union(
            select(one.as(col1)),
            select(two),
            select(three));

        if (templates.getDummyTable() == null) {
            assertEquals(
                    "(select 1 as col1)\n" +
                    "union\n" +
                    "(select 2)\n" +
                    "union\n" +
                    "(select 3)", union.toString());
        } else {
            String dummyTable = templates.getDummyTable();
            assertEquals(
                    "(select 1 as col1 from "+dummyTable+")\n" +
                    "union\n" +
                    "(select 2 from "+dummyTable+")\n" +
                    "union\n" +
                    "(select 3 from "+dummyTable+")", union.toString());
        }
    }

    @Test
    public void InnerJoin() {
        query.from(survey1).innerJoin(survey2);
        assertEquals("from SURVEY survey1 inner join SURVEY survey2", query.toString());
    }

    protected int getPrecedence(Operator... ops) {
        int precedence = templates.getPrecedence(ops[0]);
        for (int i = 1; i < ops.length; i++) {
            assertEquals(ops[i].name(), precedence, templates.getPrecedence(ops[i]));
        }
        return precedence;
    }

    @Test
    public void Generic_Precedence() {
        TemplatesTestUtils.testPrecedence(templates);
    }

    @Test
    public void Arithmetic() {
        NumberExpression<Integer> one = Expressions.numberPath(Integer.class, "one");
        NumberExpression<Integer> two = Expressions.numberPath(Integer.class, "two");

        // add
        assertSerialized(one.add(two), "one + two");
        assertSerialized(one.add(two).multiply(1), "(one + two) * ?");
        assertSerialized(one.add(two).divide(1), "(one + two) / ?");
        assertSerialized(one.add(two).add(1), "one + two + ?");

        assertSerialized(one.add(two.multiply(1)), "one + two * ?");
        assertSerialized(one.add(two.divide(1)), "one + two / ?");
        assertSerialized(one.add(two.add(1)), "one + (two + ?)"); // XXX could be better

        // sub
        assertSerialized(one.subtract(two), "one - two");
        assertSerialized(one.subtract(two).multiply(1), "(one - two) * ?");
        assertSerialized(one.subtract(two).divide(1), "(one - two) / ?");
        assertSerialized(one.subtract(two).add(1), "one - two + ?");

        assertSerialized(one.subtract(two.multiply(1)), "one - two * ?");
        assertSerialized(one.subtract(two.divide(1)), "one - two / ?");
        assertSerialized(one.subtract(two.add(1)), "one - (two + ?)");

        // mult
        assertSerialized(one.multiply(two), "one * two");
        assertSerialized(one.multiply(two).multiply(1), "one * two * ?");
        assertSerialized(one.multiply(two).divide(1), "one * two / ?");
        assertSerialized(one.multiply(two).add(1), "one * two + ?");

        assertSerialized(one.multiply(two.multiply(1)), "one * (two * ?)"); // XXX could better
        assertSerialized(one.multiply(two.divide(1)), "one * (two / ?)");
        assertSerialized(one.multiply(two.add(1)), "one * (two + ?)");
    }

    private void assertSerialized(Expression<?> expr, String serialized) {
        SQLSerializer serializer = new SQLSerializer(new Configuration(templates));
        serializer.handle(expr);
        assertEquals(serialized, serializer.toString());
    }


}
