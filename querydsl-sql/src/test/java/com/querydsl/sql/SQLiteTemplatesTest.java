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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

public class SQLiteTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLiteTemplates();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void Union() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        Path<Integer> col1 = Expressions.path(Integer.class,"col1");
        Union union = query.union(
                select(one.as(col1)),
                select(two),
                select(three));

        assertEquals(
                "select 1 as col1\n" +
                "union\n" +
                "select 2\n" +
                "union\n" +
                "select 3", union.toString());
    }

    @Test
    public void Precedence() {
        // ||
        // *    /    %
        int p1 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        // +    -
        int p2 = getPrecedence(Ops.ADD, Ops.SUB);
        // <<   >>   &    |
        // <    <=   >    >=
        int p3 = getPrecedence(Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        // =    ==   !=   <>   IS   IS NOT   IN   LIKE   GLOB   MATCH   REGEXP
        int p4 = getPrecedence(Ops.EQ, Ops.EQ_IGNORE_CASE, Ops.IS_NULL, Ops.IS_NOT_NULL,
                Ops.IN, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.MATCHES);
        // AND
        int p5 = getPrecedence(Ops.AND);
        //  OR
        int p6 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
    }

}
