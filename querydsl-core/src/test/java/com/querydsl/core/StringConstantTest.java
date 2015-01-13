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

import org.junit.Test;

import com.querydsl.core.types.expr.StringExpression;

public class StringConstantTest {

    @Test
    public void test() {
        assertEquals("abc", expr("ab").append("c").toString());
        assertEquals("abc", expr("bc").prepend("a").toString());
        assertEquals("abc", expr("ABC").lower().toString());
        assertEquals("ABC", expr("abc").upper().toString());
        assertEquals("ab",  expr("abc").substring(0,2).toString());
    }

    @Test
    public void test2() {
        assertEquals("abc", expr("ab").append(expr("c")).toString());
        assertEquals("abc", expr("bc").prepend(expr("a")).toString());
        assertEquals("abc", expr("ABC").lower().toString());
        assertEquals("ABC", expr("abc").upper().toString());
        assertEquals("ab",  expr("abc").substring(0,2).toString());
    }

    private StringExpression expr(String str) {
        return StringConstant.create(str);
    }
}
