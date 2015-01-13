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
package com.querydsl.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

}
