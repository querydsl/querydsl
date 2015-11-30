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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;

public class DerbyTemplatesTest extends AbstractSQLTemplatesTest {

    @Override
    protected SQLTemplates createTemplates() {
        return new DerbyTemplates();
    }

    @Test
    public void nextVal() {
        Operation<String> nextval = ExpressionUtils.operation(String.class, SQLOps.NEXTVAL, ConstantImpl.create("myseq"));
        assertEquals("next value for myseq", new SQLSerializer(new Configuration(new DerbyTemplates())).handle(nextval).toString());
    }

    @Test
    public void precedence() {
        // unary + and -
        int p1 = getPrecedence(Ops.NEGATE);
        // *, /, || (concatenation)
        int p2 = getPrecedence(Ops.MULT, Ops.DIV);
        // binary + and -
        int p3 = getPrecedence(Ops.ADD, Ops.SUB);
        // comparisons, quantified comparisons, EXISTS, IN, IS NULL, LIKE, BETWEEN, IS
        int p4 = getPrecedence(Ops.EQ, Ops.NE, Ops.LT, Ops.GT, Ops.LOE, Ops.GOE, Ops.EXISTS,
                Ops.IN, Ops.IS_NULL, Ops.LIKE, Ops.BETWEEN, Ops.IS_NOT_NULL);
        // NOT
        int p5 = getPrecedence(Ops.NOT);
        // AND
        int p6 = getPrecedence(Ops.AND);
        // OR
        int p7 = getPrecedence(Ops.OR);

        assertTrue(p1 < p2);
        assertTrue(p2 < p3);
        assertTrue(p3 < p4);
        assertTrue(p4 < p5);
        assertTrue(p5 < p6);
        assertTrue(p6 < p7);
    }

}
