/*
 * Copyright 2014, Mysema Ltd
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
package com.querydsl.core.util;

import static com.querydsl.core.util.ConstructorUtils.*;
import static com.querydsl.core.util.ArrayUtils.isEmpty;
import static org.junit.Assert.*;

import com.querydsl.core.types.ProjectionExample;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import org.junit.Test;

/**
 *
 * @author Shredder121
 */
public class ConstructorUtilsTest {

    @Test
    public void GetDefaultConstructor() {
        Class<?>[] args = {};
        Constructor<?> emptyDefaultConstructor = getConstructor(ProjectionExample.class, args);
        Constructor<?> nullDefaultConstructor = getConstructor(ProjectionExample.class, null);
        assertNotNull(emptyDefaultConstructor);
        assertNotNull(nullDefaultConstructor);
        assertTrue(isEmpty(emptyDefaultConstructor.getParameterTypes())
                && isEmpty(nullDefaultConstructor.getParameterTypes()));
    }

    @Test
    public void GetSimpleConstructor() {
        Class<?>[] args = {Long.class};
        Constructor<?> constructor = getConstructor(ProjectionExample.class, args);
        assertNotNull(constructor);
        assertArrayEquals(args, constructor.getParameterTypes());
    }

    @Test
    public void GetDefaultConstructorParameters() {
        Class<?>[] args = {Long.class, String.class};
        Class<?>[] expected = {Long.TYPE, String.class};
        Class<?>[] constructorParameters = getConstructorParameters(ProjectionExample.class, args);
        assertArrayEquals("Constructorparameters not equal", expected, constructorParameters);
    }

    private <C> Constructor<C> getConstructor(Class<C> type, Class<?>[] givenTypes) {
        Constructor<C> rv = null;
        try {
            rv = ConstructorUtils.getConstructor(type, givenTypes);
        } catch (NoSuchMethodException ex) {
            fail("No constructor found for " + type.toString()
                    + " with parameters: " + Arrays.toString(givenTypes));
        }
        return rv;
    }

}
