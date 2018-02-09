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
package com.querydsl.core;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Templates;
import com.querydsl.core.types.ToStringVisitor;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;

public class BooleanBuilderTest {

    private final BooleanExpression first = BooleanConstant.TRUE;

    private final BooleanExpression second = BooleanConstant.FALSE;

    @Test
    public void null_in_constructor() {
        assertNull(new BooleanBuilder(null).getValue());
    }

    @Test
    public void and_empty() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(new BooleanBuilder());
        assertNull(ExpressionUtils.extract(builder));
    }

    @Test
    public void and_any_of() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, null);
        assertEquals(first, builder.getValue());
    }

    @Test
    public void and_any_of2() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(null, first);
        assertEquals(first, builder.getValue());
    }


    @Test
    public void or_all_of() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.orAllOf(first, null);
        assertEquals(first, builder.getValue());
    }

    @Test
    public void or_all_of2() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.orAllOf(null, first);
        assertEquals(first, builder.getValue());
    }

    @Test(expected = QueryException.class)
    @Ignore
    public void wrapped_booleanBuilder() {
        new BooleanBuilder(new BooleanBuilder());
    }

    @Test
    public void basic() {
//        new BooleanBuilder().and(first).or(second);
        assertEquals(first.or(second).toString(),
                new BooleanBuilder().and(first).or(second).toString());
    }

    @Test
    public void advanced() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        assertEquals("true || false || true || true && false && true", builder.toString());
    }

    @Test
    public void if_then_else() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(null);
        builder.or(null);
        builder.and(second);
        assertEquals(second, builder.getValue());
    }

    @Test
    public void and_null_supported() {
        assertEquals(first, first.and(null));
    }

    @Test
    public void or_null_supported() {
        assertEquals(first, first.or(null));
    }

    @Test
    public void and_not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).andNot(second);
        assertEquals(first.and(second.not()), builder.getValue());
    }

    @Test
    public void or_not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).orNot(second);
        assertEquals(first.or(second.not()), builder.getValue());
    }

    @Test
    public void not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).not();
        assertEquals(first.not(), builder.getValue());
    }

    @Test
    public void booleanBuilder_equals_booleanBuilder() {
        assertEquals(new BooleanBuilder(first), new BooleanBuilder(first));
    }

    @Test
    public void constant_equals_booleanBuilder() {
        assertFalse(first.equals(new BooleanBuilder(first)));
    }

    @Test
    public void booleanBuilder_equals_constant() {
        assertFalse(new BooleanBuilder(first).equals(first));
    }

    @Test
    public void hashCode_() {
        assertEquals(new BooleanBuilder(first).hashCode(), new BooleanBuilder(first).hashCode());
        assertEquals(new BooleanBuilder().hashCode(), new BooleanBuilder().hashCode());
    }

    @Test
    public void toString_() {
        BooleanBuilder builder = new BooleanBuilder().and(first);
        assertEquals("true", builder.toString());
        builder.or(Expressions.booleanPath("condition"));
        assertEquals("true || condition", builder.toString());
    }

//    @Test
//    public void getArg() {
//        BooleanBuilder builder = new BooleanBuilder().and(first);
//        assertEquals(first, builder.getArg(0));
//    }
//
//    @Test
//    public void getArgs() {
//        BooleanBuilder builder = new BooleanBuilder().and(first);
//        assertEquals(Arrays.asList(first), builder.getArgs());
//    }

    @Test
    public void accept() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        builder.or(Expressions.booleanPath("condition"));
        assertEquals("true || condition", builder.accept(ToStringVisitor.DEFAULT, Templates.DEFAULT));
    }

}
