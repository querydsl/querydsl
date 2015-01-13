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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.path.StringPath;

public class NestedExpressionTest {

    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    StringPath str4 = new StringPath("str3");

    Concatenation concat1 = new Concatenation(new Concatenation(str1, str2), str3);
    Concatenation concat2 = new Concatenation(new Concatenation(str1, new Concatenation(str2, str3)), str4);

    @Test
    public void Wrapped_Projection_Has_Right_Arguments() {
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat1);
        assertEquals(Arrays.asList(str1, str2, str3), wrapped.getArgs());
    }

    @Test
    public void Wrapped_Projection_Compresses_Projection() {
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat1);
        assertEquals("123", wrapped.newInstance("1","2","3"));
    }

    @Test
    public void Deeply_Wrapped_Projection_Has_Right_Arguments() {
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat2);
        assertEquals(Arrays.asList(str1, str2, str3, str4), wrapped.getArgs());
    }

    @Test
    public void Deeply_Wrapped_Projection_Compresses_Projection() {
        FactoryExpression<String> wrapped = FactoryExpressionUtils.wrap(concat2);
        assertEquals("1234", wrapped.newInstance("1","2","3","4"));
    }

}
