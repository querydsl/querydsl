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
package com.querydsl.core.types;

import java.util.Arrays;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.path.StringPath;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class QTupleTest {

    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    StringPath str4 = new StringPath("str4");
    Expression<?>[] exprs1 = new Expression[]{str1, str2};
    Expression<?>[] exprs2 = new Expression[]{str3, str4};

    Concatenation concat = new Concatenation(str1, str2);

    @Test
    public void Alias() {
        Expression<?> expr = str1.as("s");
        QTuple qTuple = new QTuple(expr);
        Tuple tuple = qTuple.newInstance("arg");
        assertEquals("arg", tuple.get(expr));
        assertEquals("arg", tuple.get(new StringPath("s")));
    }

    @Test
    public void TwoExpressions_getArgs() {
        assertEquals(Arrays.asList(str1, str2), new QTuple(str1, str2).getArgs());
    }

    @Test
    public void OneArray_getArgs() {
        assertEquals(Arrays.asList(str1, str2), new QTuple(exprs1).getArgs());
    }

    @Test
    public void TwoExpressionArrays_getArgs() {
        assertEquals(Arrays.asList(str1, str2, str3, str4), new QTuple(exprs1, exprs2).getArgs());
    }

    @Test
    public void NestedProjection_getArgs() {
        assertEquals(Arrays.asList(str1, str2), FactoryExpressionUtils.wrap(new QTuple(concat)).getArgs());
    }

    @Test
    public void NestedProjection_getArgs2() {
        assertEquals(Arrays.asList(str1, str2, str3), FactoryExpressionUtils.wrap(new QTuple(concat, str3)).getArgs());
    }

    @Test
    public void NestedProjection_newInstance() {
        QTuple expr = new QTuple(concat);
        assertEquals("1234", FactoryExpressionUtils.wrap(expr).newInstance("12", "34").get(concat));
    }

    @Test
    public void NestedProjection_newInstance2() {
        QTuple expr = new QTuple(str1, str2, concat);
        assertEquals("1234", FactoryExpressionUtils.wrap(expr).newInstance("1", "2", "12", "34").get(concat));
    }

    @Test
    public void Tuple_Equals() {
        QTuple expr = new QTuple(str1, str2);
        assertEquals(expr.newInstance("str1", "str2"), expr.newInstance("str1", "str2"));
    }

    @Test
    public void Tuple_hashCode() {
        QTuple expr = new QTuple(str1, str2);
        assertEquals(expr.newInstance("str1", "str2").hashCode(), expr.newInstance("str1", "str2").hashCode());
    }

    @Test
    @Ignore
    public void Duplicates() {
        QTuple expr = new QTuple(str1, str1);
        assertEquals(1, expr.getArgs().size());
        assertEquals(str1, expr.getArgs().get(0));
    }

    @Test
    @Ignore
    public void Duplicates2() {
        QTuple expr = new QTuple(ImmutableList.<Expression<?>>of(str1, str1));
        assertEquals(1, expr.getArgs().size());
        assertEquals(str1, expr.getArgs().get(0));
    }

    @Test
    public void NewInstance() {
        assertNotNull(new QTuple(str1, str1).newInstance(null, null));
        assertNull(new QTuple(str1, str1).skipNulls().newInstance(null, null));
    }
}
