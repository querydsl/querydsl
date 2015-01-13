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
package com.querydsl.jpa;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.path.NumberPath;

public class CastTest extends AbstractQueryTest {

    private static NumberExpression<Integer> expr = new NumberPath<Integer>(Integer.class,"int");

    @Test
    public void Byte() {
        assertEquals(Byte.class, expr.byteValue().getType());
    }
    
    @Test
    public void Double() {
        assertEquals(Double.class, expr.doubleValue().getType());
    }
    
    @Test
    public void Float() {
        assertEquals(Float.class, expr.floatValue().getType());
    }
    
    @Test
    public void Integer() {
        assertEquals(Integer.class, expr.intValue().getType());
    }
    
    @Test
    public void Long() {
        assertEquals(Long.class, expr.longValue().getType());
    }
    
    @Test
    public void Short() {
        assertEquals(Short.class, expr.shortValue().getType());
    }

    @Test
    public void StringCast() {
        assertEquals(String.class, expr.stringValue().getType());
    }
}
