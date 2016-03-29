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

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLUpdateClause;


public class SQLServer2012TemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    @Test
    public void noFrom() {
        query.getMetadata().setProjection(Expressions.ONE);
        assertEquals("select 1", query.toString());
    }

    @Override
    protected SQLTemplates createTemplates() {
        return new SQLServer2012Templates();
    }

    @SuppressWarnings("unchecked")
    @Test
    @Override
    public void union() {
        NumberExpression<Integer> one = Expressions.ONE;
        NumberExpression<Integer> two = Expressions.TWO;
        NumberExpression<Integer> three = Expressions.THREE;
        Path<Integer> col1 = Expressions.path(Integer.class,"col1");
        Union union = query.union(
                select(one.as(col1)),
                select(two),
                select(three));
        assertEquals(
                "(select 1 as col1)\n" +
                "union\n" +
                "(select 2)\n" +
                "union\n" +
                "(select 3)", union.toString());
    }

    @Test
    public void limit() {
        query.from(survey1).limit(5);
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select top 5 survey1.ID from SURVEY survey1", query.toString());
    }

    @Test
    public void limitOffset() {
        query.from(survey1).limit(5).offset(5);
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select survey1.ID from SURVEY survey1 " +
                "order by 1 asc " +
                "offset ? rows fetch next ? rows only", query.toString());
    }

    @Test
    public void delete_limit() {
        SQLDeleteClause clause = new SQLDeleteClause(null, createTemplates(), survey1);
        clause.where(survey1.name.eq("Bob"));
        clause.limit(5);
        assertEquals("delete top 5 from SURVEY\n" +
                "where SURVEY.NAME = ?", clause.toString());
    }

    @Test
    public void update_limit() {
        SQLUpdateClause clause = new SQLUpdateClause(null, createTemplates(), survey1);
        clause.set(survey1.name, "Bob");
        clause.limit(5);
        assertEquals("update top 5 SURVEY\n" +
                "set NAME = ?", clause.toString());
    }

    @Test
    public void modifiers() {
        query.from(survey1).limit(5).offset(3).orderBy(survey1.id.asc());
        query.getMetadata().setProjection(survey1.id);
        assertEquals("select survey1.ID from SURVEY survey1 order by survey1.ID asc offset ? rows fetch next ? rows only", query.toString());
    }

    @Test
    public void nextVal() {
        Operation<String> nextval = ExpressionUtils.operation(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("myseq.nextval", new SQLSerializer(new Configuration(new SQLServerTemplates())).handle(nextval).toString());
    }

}
