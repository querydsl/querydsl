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

import static com.querydsl.sql.SQLExpressions.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.SimplePath;

public class UnionSubQueryTest {

    private static final SimpleExpression<Integer> one = Expressions.numberTemplate(Integer.class, "1");

    private static final SimpleExpression<Integer> two = Expressions.numberTemplate(Integer.class, "2");

    private static final SimpleExpression<Integer> three = Expressions.numberTemplate(Integer.class,"3");

    private SQLTemplates templates = H2Templates.builder().newLineToSingleSpace().build();

    private SQLSerializer serializer = new SQLSerializer(new Configuration(templates));

    @Test
    public void In_Union() {
        NumberPath<Integer> intPath = Expressions.numberPath(Integer.class, "intPath");
        Expression<?> expr = intPath.in(union(
                select(one),
                select(two)));

        serializer.handle(expr);
        assertEquals(
            "intPath in ((select 1 from dual)\n" +
            "union\n" +
            "(select 2 from dual))", serializer.toString());
    }

    @Test
    public void Union_SubQuery() {
        SimplePath<Integer> col1 = Expressions.path(Integer.class, "col1");
        Expression<?> union = union(
                select(one.as(col1)),
                select(two),
                select(three));

        serializer.handle(union);
        assertEquals(
                "(select 1 as col1 from dual)\n" +
                "union\n" +
                "(select 2 from dual)\n" +
                "union\n" +
                "(select 3 from dual)", serializer.toString());
    }

    @Test
    public void UnionAll_SubQuery() {
        SimplePath<Integer> col1 = Expressions.path(Integer.class,"col1");
        Expression<?> union = unionAll(
                select(one.as(col1)),
                select(two),
                select(three));

        serializer.handle(union);
        assertEquals(
                "(select 1 as col1 from dual)\n" +
                "union all\n" +
                "(select 2 from dual)\n" +
                "union all\n" +
                "(select 3 from dual)", serializer.toString());
    }

}
