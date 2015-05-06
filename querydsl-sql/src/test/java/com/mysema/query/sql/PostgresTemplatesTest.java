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
package com.mysema.query.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.template.NumberTemplate;


public class PostgresTemplatesTest extends AbstractSQLTemplatesTest{

    @Override
    protected SQLTemplates createTemplates() {
        return new PostgresTemplates();
    }

    @Test
    public void NoFrom() {
        query.getMetadata().addProjection(NumberTemplate.ONE);
        assertEquals("select 1", query.toString());
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
        assertEquals(
                "(select 1 as col1)\n" +
                        "union\n" +
                        "(select 2)\n" +
                        "union\n" +
                        "(select 3)", union.toString());
    }

    @Test
    public void Precedence() {
        //.	left	table/column name separator
        //        ::	left	PostgreSQL-style typecast
        //[ ]	left	array element selection
        //+ -	right	unary plus, unary minus
        int p0 = getPrecedence(Ops.NEGATE);
        //        ^	left	exponentiation
        //        * / %	left	multiplication, division, modulo
        int p1 = getPrecedence(Ops.MULT, Ops.DIV, Ops.MOD);
        //+ -	left	addition, subtraction
        int p2 = getPrecedence(Ops.ADD, Ops.SUB);
        //IS	 	IS TRUE, IS FALSE, IS NULL, etc
        int p3 = getPrecedence(Ops.IS_NULL, Ops.IS_NOT_NULL);
        //ISNULL	 	test for null
        //NOTNULL	 	test for not null
        //(any other)	left	all other native and user-defined operators
        //IN	 	set membership
        int p4 = getPrecedence(Ops.IN);
        //BETWEEN	 	range containment
        int p5 = getPrecedence(Ops.BETWEEN);
        //OVERLAPS	 	time interval overlap
        //LIKE ILIKE SIMILAR	 	string pattern matching
        int p6 = getPrecedence(Ops.LIKE, Ops.LIKE_ESCAPE);
        //< >	 	less than, greater than
        int p7 = getPrecedence(Ops.LT, Ops.GT);
        //        =	right	equality, assignment
        int p8 = getPrecedence(Ops.EQ);
        //NOT	right	logical negation
        int p9 = getPrecedence(Ops.NOT);
        //AND	left	logical conjunction
        int p10 = getPrecedence(Ops.AND);
        //OR	left	logical disjunction
        int p11 = getPrecedence(Ops.OR);

        assertTrue(p0 < p1);
        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
        assertTrue(p7 < p8);
        assertTrue(p8 < p9);
        assertTrue(p9 < p10);
        assertTrue(p10 < p11);
    }

}
