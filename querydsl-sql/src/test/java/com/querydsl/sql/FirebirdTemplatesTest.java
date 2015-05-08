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

public class FirebirdTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new FirebirdTemplates();
    }

    @Override
    public void Arithmetic() {
        // uses additional casts
    }

    @Test
    @Override
    public void Union() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        Path<Integer> col1 = Expressions.numberPath(Integer.class,"col1");
        Union union = query.union(
                select(one.as(col1)),
                select(two),
                select(three));

        assertEquals(
                "select 1 as col1 from RDB$DATABASE\n" +
                "union\n" +
                "select 2 from RDB$DATABASE\n" +
                "union\n" +
                "select 3 from RDB$DATABASE", union.toString());
    }

    @Test
    public void Precedence() {
        // concat
        // *, /, +, -
        // comparison
        // NOT
        // AND
        // OR

        int p1 = getPrecedence(Ops.CONCAT);
        int p2 = getPrecedence(Ops.NEGATE);
        int p3 = getPrecedence(Ops.MULT, Ops.DIV);
        int p4 = getPrecedence(Ops.SUB, Ops.ADD);
        int p5 = getPrecedence(Ops.EQ, Ops.GOE, Ops.GT, Ops.LT, Ops.NE, Ops.IS_NULL, Ops.IS_NOT_NULL,
                               Ops.MATCHES, Ops.IN, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN);
        int p6 = getPrecedence(Ops.NOT);
        int p7 = getPrecedence(Ops.AND);
        int p8 = getPrecedence(Ops.XOR, Ops.XNOR);
        int p9 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
        assertTrue(p8 < p9);
    }

}
