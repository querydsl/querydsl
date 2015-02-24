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

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Template;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.template.NumberTemplate;
import com.querydsl.sql.domain.QSurvey;

import junit.framework.Assert;

public abstract class AbstractSQLTemplatesTest {

    protected static final QSurvey survey1 = new QSurvey("survey1");

    protected static final QSurvey survey2 = new QSurvey("survey2");

    private SQLTemplates templates;

    protected SQLQuery query;

    protected abstract SQLTemplates createTemplates();

    @Before
    public void setUp() {
        templates = createTemplates();
        templates.newLineToSingleSpace();
        query = new SQLQuery(templates);
    }

    @Test
    public void NoFrom() {
        query.getMetadata().setProjection(NumberTemplate.ONE);
        if (templates.getDummyTable() == null) {
            assertEquals("select 1", query.toString());
        } else {
            assertEquals("select 1 from " + templates.getDummyTable(), query.toString());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void Union() {
        NumberExpression<Integer> one = NumberTemplate.ONE;
        NumberExpression<Integer> two = NumberTemplate.TWO;
        NumberExpression<Integer> three = NumberTemplate.THREE;
        Path<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Union union = query.union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));

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

    protected SQLSubQuery sq() {
        return new SQLSubQuery();
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
        int likePrecedence = templates.getPrecedence(Ops.LIKE);
        int eqPrecedence = templates.getPrecedence(Ops.EQ);
        if (templates.getPrecedence(Ops.EQ_IGNORE_CASE) != eqPrecedence) {
            Assert.fail("Unexpected precedence for EQ_IGNORE_CASE "
                    + templates.getPrecedence(Ops.EQ_IGNORE_CASE));
        }
        for (Operator op : Ops.values()) {
            Template template = templates.getTemplate(op);
            int precedence = templates.getPrecedence(op);
            if (template.toString().contains(" like ") && precedence != likePrecedence) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            } else if (!template.toString().contains("(") && precedence < 0) {
                Assert.fail("Unexpected precedence for " + op + " with template " + template);
            }

        }
    }


}
