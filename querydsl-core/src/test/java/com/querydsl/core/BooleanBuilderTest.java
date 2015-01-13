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
package com.querydsl.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Templates;
import com.querydsl.core.types.ToStringVisitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.BooleanPath;

public class BooleanBuilderTest {

    private final BooleanExpression first = BooleanConstant.TRUE;

    private final BooleanExpression second = BooleanConstant.FALSE;

    @Test
    public void Null_In_Constructor() {
        assertNull(new BooleanBuilder(null).getValue());
    }

    @Test
    public void And_Empty() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(new BooleanBuilder());
        assertNull(ExpressionUtils.extract(builder));
    }

    @Test
    public void AndAnyOf() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, null);
        assertEquals(first, builder.getValue());
    }

    @Test
    public void AndAnyOf2() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(null, first);
        assertEquals(first, builder.getValue());
    }


    @Test
    public void OrAllOf() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.orAllOf(first, null);
        assertEquals(first, builder.getValue());
    }

    @Test
    public void OrAllOf2() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.orAllOf(null, first);
        assertEquals(first, builder.getValue());
    }

    @Test(expected=QueryException.class)
    @Ignore
    public void WrappedBooleanBuilder() {
        new BooleanBuilder(new BooleanBuilder());
    }

    @Test
    public void Basic() {
//        new BooleanBuilder().and(first).or(second);
        assertEquals(first.or(second).toString(),
            new BooleanBuilder().and(first).or(second).toString());
    }

    @Test
    public void Advanced() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(first, second, first);
        builder.orAllOf(first, second, first);
        assertEquals("true || false || true || true && false && true", builder.toString());
    }

    @Test
    public void If_Then_Else() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(null);
        builder.or(null);
        builder.and(second);
        assertEquals(second, builder.getValue());
    }

    @Test
    public void And_null_Supported() {
        assertEquals(first, first.and(null));
    }

    @Test
    public void Or_null_Supported() {
        assertEquals(first, first.or(null));
    }

    @Test
    public void And_Not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).andNot(second);
        assertEquals(first.and(second.not()), builder.getValue());
    }

    @Test
    public void Or_Not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).orNot(second);
        assertEquals(first.or(second.not()), builder.getValue());
    }

    @Test
    public void Not() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first).not();
        assertEquals(first.not(), builder.getValue());
    }

    @Test
    public void BooleanBuilder_Equals_BooleanBuilder() {
        assertEquals(new BooleanBuilder(first), new BooleanBuilder(first));
    }

    @Test
    public void Constant_Equals_BooleanBuilder() {
        assertFalse(first.equals(new BooleanBuilder(first)));
    }

    @Test
    public void BooleanBuilder_Equals_Constant() {
        assertFalse(new BooleanBuilder(first).equals(first));
    }

    @Test
    public void HashCode() {
        assertEquals(new BooleanBuilder(first).hashCode(), new BooleanBuilder(first).hashCode());
        assertEquals(new BooleanBuilder().hashCode(), new BooleanBuilder().hashCode());
    }

    @Test
    public void ToString() {
        BooleanBuilder builder = new BooleanBuilder().and(first);
        assertEquals("true", builder.toString());
        builder.or(new BooleanPath("condition"));
        assertEquals("true || condition", builder.toString());
    }

//    @Test
//    public void GetArg() {
//        BooleanBuilder builder = new BooleanBuilder().and(first);
//        assertEquals(first, builder.getArg(0));
//    }
//
//    @Test
//    public void GetArgs() {
//        BooleanBuilder builder = new BooleanBuilder().and(first);
//        assertEquals(Arrays.asList(first), builder.getArgs());
//    }

    @Test
    public void Accept() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(first);
        builder.or(new BooleanPath("condition"));
        assertEquals("true || condition", builder.accept(ToStringVisitor.DEFAULT, Templates.DEFAULT));
    }

}
