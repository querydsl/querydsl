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
package com.querydsl.core.types.expr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.ArrayConstructorExpression;
import com.querydsl.core.types.path.StringPath;

public class ArrayConstructorExpressionTest {

    @SuppressWarnings("unchecked")
    @Test
    public void NewInstanceObjectArray() {
        ArrayConstructorExpression<String> constructor = new ArrayConstructorExpression<String>(
                String[].class,  new StringPath("test"), new StringPath("test2"));

        String[] strings = constructor.newInstance((Object[])new String[]{"1", "2"});
        assertEquals("1", strings[0]);
        assertEquals("2", strings[1]);

        strings = constructor.newInstance(new Object[]{"1", "2"});
        assertEquals("1", strings[0]);
        assertEquals("2", strings[1]);

    }

}
