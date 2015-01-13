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
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.path.StringPath;
import static org.junit.Assert.assertTrue;

public class ConstructorExpressionTest {

    StringPath str1 = new StringPath("str1");
    StringPath str2 = new StringPath("str2");
    StringPath str3 = new StringPath("str3");
    Concatenation concat = new Concatenation(str1, str2);

    @Test
    public void Constructor() {
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        ProjectionExample instance = new ConstructorExpression<ProjectionExample>(ProjectionExample.class,
                new Class[]{long.class, String.class}, longVal, stringVal).newInstance(0l, "");
        assertNotNull(instance);
        assertEquals((Long) 0L, instance.id);
        assertTrue(instance.text.isEmpty());
    }

    @Test
    public void Create() {
        Expression<Long> longVal = ConstantImpl.create(1l);
        Expression<String> stringVal = ConstantImpl.create("");
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, longVal, stringVal).newInstance(0l, ""));
    }

    @Test
    public void Create2() {
        Expression<Long> longVal = ConstantImpl.create(1l);
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, longVal).newInstance(0l));
    }

    @Test
    public void Create3() {
        assertNotNull(ConstructorExpression.create(ProjectionExample.class).newInstance());
    }

    @Test
    public void Create4() {
        Expression<String> stringVal = ConstantImpl.create("");
        assertNotNull(ConstructorExpression.create(ProjectionExample.class, stringVal).newInstance(""));
    }

    @Test
    public void CreateNullPrimitive() {
        Expression<Boolean> booleanVal = ConstantImpl.create(false);
        Expression<Byte> byteVal = ConstantImpl.create((byte) 0);
        Expression<Character> charVal = ConstantImpl.create('\0');
        Expression<Short> shortVal = ConstantImpl.create((short) 0);
        Expression<Integer> intVal = ConstantImpl.create(0);
        Expression<Long> longVal = ConstantImpl.create(0L);
        Expression<Float> floatVal = ConstantImpl.create(0.0F);
        Expression<Double> doubleVal = ConstantImpl.create(0.0);
        ProjectionExample instance = ConstructorExpression.create(ProjectionExample.class,
                booleanVal, byteVal,
                charVal, shortVal,
                intVal, longVal,
                floatVal, doubleVal)
                .newInstance(null, null,
                        null, null,
                        null, null,
                        null, null);
        assertNotNull(instance);
    }

    @Test
    public void FactoryExpression_has_right_args() {
        FactoryExpression<ProjectionExample> constructor = ConstructorExpression.create(ProjectionExample.class, concat);
        constructor = FactoryExpressionUtils.wrap(constructor);
        assertEquals(Arrays.asList(str1, str2), constructor.getArgs());
    }

    @Test
    public void FactoryExpression_newInstance() {
        FactoryExpression<ProjectionExample> constructor = ConstructorExpression.create(ProjectionExample.class, concat);
        constructor = FactoryExpressionUtils.wrap(constructor);
        ProjectionExample projection = constructor.newInstance("12", "34");
        assertEquals("1234", projection.text);
    }

}
