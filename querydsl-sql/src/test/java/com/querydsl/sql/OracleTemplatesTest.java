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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.QueryFlag;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;

public class OracleTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new OracleTemplates();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Test
    public void union() {
        SimpleExpression<Integer> one = Expressions.template(Integer.class, "1");
        SimpleExpression<Integer> two = Expressions.template(Integer.class,"2");
        SimpleExpression<Integer> three = Expressions.template(Integer.class,"3");
        NumberPath<Integer> col1 = Expressions.numberPath(Integer.class, "col1");
        Union union = query.union(
            select(one.as(col1)),
            select(two),
            select(three));
        assertEquals(
                "(select 1 col1 from dual)\n" +
                "union\n" +
                "(select 2 from dual)\n" +
                "union\n" +
                "(select 3 from dual)", union.toString());
    }

    @Test
    public void modifiers() {
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select * from (  " +
                "select a.*, rownum rn from (   " +
                "select survey1.ID from SURVEY survey1  ) " +
                "a) " +
                "where rn > 3 and rownum <= 5", query.toString());
    }

    @Test
    public void modifiers2() {
        query.from(survey1).limit(5).offset(3);
        query.getMetadata().setProjection(survey1.id);
        query.getMetadata().addFlag(new QueryFlag(QueryFlag.Position.AFTER_PROJECTION, ", count(*) over() "));

        assertEquals("select * from (  " +
            "select a.*, rownum rn from (   " +
            "select survey1.ID, count(*) over()  from SURVEY survey1  ) " +
            "a) " +
            "where rn > 3 and rownum <= 5", query.toString());
    }

    @Test
    public void in() {
        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 0; i < 2000; i++) {
            ids.add(i);
        }
        query.where(survey1.id.isNotNull());
        query.where(survey1.id.in(ids));
        assertTrue(query.toString().startsWith("from dual where survey1.ID is not null and (survey1.ID in "));
    }

    @Test
    public void nextVal() {
        Operation<String> nextval = ExpressionUtils.operation(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new Configuration(new OracleTemplates())).handle(nextval).toString());
    }

    @Test
    public void precedence() {
        // +, - (as unary operators), PRIOR, CONNECT_BY_ROOT  identity, negation, location in hierarchy
        int p1 = getPrecedence(Ops.NEGATE);
        // *, / multiplication, division
        int p2 = getPrecedence(Ops.MULT, Ops.DIV);
        // +, - (as binary operators), || addition, subtraction, concatenation
        int p3 = getPrecedence(Ops.ADD, Ops.SUB, Ops.CONCAT);
        // =, !=, <, >, <=, >=, comparison
        int p4 = getPrecedence(Ops.EQ, Ops.NE, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE);
        // IS [NOT] NULL, LIKE, [NOT] BETWEEN, [NOT] IN, EXISTS, IS OF type comparison
        int p5 = getPrecedence(Ops.IS_NULL, Ops.IS_NOT_NULL, Ops.LIKE, Ops.LIKE_ESCAPE, Ops.BETWEEN, Ops.IN, Ops.NOT_IN, Ops.EXISTS);
        // NOT exponentiation, logical negation
        int p6 = getPrecedence(Ops.NOT);
        // AND conjunction
        int p7 = getPrecedence(Ops.AND);
        // OR disjunction
        int p8 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
    }

}
