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
package com.mysema.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

import com.SomeClass;

public class ClassPathUtilsTest {

    @Test
    public void ScanPackage() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classes = ClassPathUtils.scanPackage(classLoader, SomeClass.class.getPackage());
        assertFalse(classes.isEmpty());
    }

    @Test
    public void ScanPackage_Check_Initialized() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Set<Class<?>> classes = ClassPathUtils.scanPackage(classLoader, getClass().getPackage());
        assertFalse(classes.isEmpty());
        assertEquals("XXX", SomeOtherClass2.property);
    }

    @Test
    public void SafeClassForName() {
        assertNull(safeForName("com.sun.nio.file.ExtendedOpenOption"));
        assertNotNull(safeForName("com.suntanning.ShouldBeLoaded"));
        assertNotNull(safeForName("com.applejuice.ShouldBeLoaded"));
    }

    private Class<?> safeForName(String className) {
        return ClassPathUtils.safeClassForName(ClassPathUtilsTest.class.getClassLoader(), className);
    }
}
