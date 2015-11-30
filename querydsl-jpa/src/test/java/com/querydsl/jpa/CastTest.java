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
package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

public class CastTest extends AbstractQueryTest {

    private static NumberExpression<Integer> expr = Expressions.numberPath(Integer.class, "int");

    @Test
    public void bytes() {
        assertEquals(Byte.class, expr.byteValue().getType());
    }

    @Test
    public void doubles() {
        assertEquals(Double.class, expr.doubleValue().getType());
    }

    @Test
    public void floats() {
        assertEquals(Float.class, expr.floatValue().getType());
    }

    @Test
    public void integers() {
        assertEquals(Integer.class, expr.intValue().getType());
    }

    @Test
    public void longs() {
        assertEquals(Long.class, expr.longValue().getType());
    }

    @Test
    public void shorts() {
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void stringCast() {
        assertEquals(String.class, expr.stringValue().getType());
    }
}
