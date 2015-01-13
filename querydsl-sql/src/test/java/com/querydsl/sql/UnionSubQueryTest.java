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

import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SimplePath;

public class UnionSubQueryTest {

    private SQLTemplates templates = H2Templates.builder().newLineToSingleSpace().build();

    private SQLSerializer serializer = new SQLSerializer(new Configuration(templates));

    @Test
    public void In_Union() {
        SimplePath<Integer> one = new SimplePath<Integer>(Integer.class,"1");
        SimplePath<Integer> two = new SimplePath<Integer>(Integer.class,"2");
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "intPath");

        Expression<?> expr = intPath.in(sq().union(
                sq().unique(one),
                sq().unique(two)));

        serializer.handle(expr);
        assertEquals(
                "intPath in ((select 1 from dual)\n" +
        	"union\n" +
        	"(select 2 from dual))", serializer.toString());
    }

    @Test
    public void Union_SubQuery() {
        SimplePath<Integer> one = new SimplePath<Integer>(Integer.class,"1");
        SimplePath<Integer> two = new SimplePath<Integer>(Integer.class,"2");
        SimplePath<Integer> three = new SimplePath<Integer>(Integer.class,"3");
        SimplePath<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Expression<?> union = sq().union(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));

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
        SimplePath<Integer> one = new SimplePath<Integer>(Integer.class,"1");
        SimplePath<Integer> two = new SimplePath<Integer>(Integer.class,"2");
        SimplePath<Integer> three = new SimplePath<Integer>(Integer.class,"3");
        SimplePath<Integer> col1 = new SimplePath<Integer>(Integer.class,"col1");
        Expression<?> union = sq().unionAll(
            sq().unique(one.as(col1)),
            sq().unique(two),
            sq().unique(three));

        serializer.handle(union);
        assertEquals(
                "(select 1 as col1 from dual)\n" +
                "union all\n" +
                "(select 2 from dual)\n" +
                "union all\n" +
                "(select 3 from dual)", serializer.toString());
    }


    protected SQLSubQuery sq() {
        return new SQLSubQuery();
    }

}
