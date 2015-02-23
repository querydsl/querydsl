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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;


public class SQLServer2005TemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    @Test
    public void NoFrom() {
        query.getMetadata().setProjection(Expressions.ONE);
        assertEquals("select 1", query.toString());
    }

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLServer2005Templates();
    }

    @SuppressWarnings("unchecked")
    @Test
    @Override
    public void Union() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        Path<Integer> col1 = Expressions.path(Integer.class,"col1");
        Union union = query.union(
            sq().select(one.as(col1)),
            sq().select(two),
            sq().select(three));
        assertEquals(
                "(select 1 as col1)\n" +
                "union\n" +
                "(select 2)\n" +
                "union\n" +
                "(select 3)", union.toString());
    }

    @Test
    public void Limit() {
        query.from(survey1).limit(5);
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select top (?) survey1.ID from SURVEY survey1", query.toString());
    }

    @Test
    public void Modifiers() {
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select * from (" +
      		"   select survey1.ID, row_number() over () as rn from SURVEY survey1) a " +
      		"where rn > ? and rn <= ? order by rn", query.toString());
    }

    @Test
    public void NextVal() {
        Operation<String> nextval = ExpressionUtils.operation(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new Configuration(new SQLServerTemplates())).handle(nextval).toString());
    }

    @Test
    public void Precedence() {
        // 1  ~ (Bitwise NOT)
        // 2  (Multiply), / (Division), % (Modulo)
        int p2 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        // 3 + (Positive), - (Negative), + (Add), (+ Concatenate), - (Subtract), & (Bitwise AND), ^ (Bitwise Exclusive OR), | (Bitwise OR)
        int p3 = getPrecedence(Ops.NEGATE, Ops.ADD, Ops.SUB, Ops.CONCAT);
        // 4 =, >, <, >=, <=, <>, !=, !>, !< (Comparison operators)
        int p4 = getPrecedence(Ops.EQ, Ops.GT, Ops.LT, Ops.GOE, Ops.LOE, Ops.NE);
        // 5 NOT
        int p5 = getPrecedence(Ops.NOT);
        // 6 AND
        int p6 = getPrecedence(Ops.AND);
        // 7 ALL, ANY, BETWEEN, IN, LIKE, OR, SOME
        int p7 = getPrecedence(Ops.BETWEEN, Ops.IN, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.OR);
        // 8 = (Assignment)

        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
    }

}
