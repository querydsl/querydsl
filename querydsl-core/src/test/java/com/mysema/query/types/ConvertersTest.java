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
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;


public class ConvertersTest {

    private static final Converters converters = new Converters('\\');
    
    private static Constant<String> constant = ConstantImpl.create("abcDEF");
    
    private static StringPath path = new StringPath("string");
    
    @Test
    public void ToLowerCase() {
        assertEquals("abcdef", converters.toLowerCase.transform(constant).toString());
        assertEquals("lower(string)", converters.toLowerCase.transform(path).toString());
    }
    
    @Test
    public void ToUpperCase() {
        assertEquals("ABCDEF", converters.toUpperCase.transform(constant).toString());
        assertEquals("upper(string)", converters.toUpperCase.transform(path).toString());
    }
    
    @Test
    public void ToStartsWithViaLike() {
        assertEquals("abcDEF%", converters.toStartsWithViaLike.transform(constant).toString());
        assertEquals("string + %", converters.toStartsWithViaLike.transform(path).toString());
    }
    
    @Test
    public void ToStartsWithViaLikeLower() {
        assertEquals("abcdef%", converters.toStartsWithViaLikeLower.transform(constant).toString());
        assertEquals("lower(string + %)", converters.toStartsWithViaLikeLower.transform(path).toString());
    }
    
    @Test
    public void ToEndsWithViaLike() {
        assertEquals("%abcDEF", converters.toEndsWithViaLike.transform(constant).toString());
        assertEquals("% + string", converters.toEndsWithViaLike.transform(path).toString());
    }
    
    @Test
    public void ToEndsWithViaLikeLower() {
        assertEquals("%abcdef", converters.toEndsWithViaLikeLower.transform(constant).toString());
        assertEquals("lower(% + string)", converters.toEndsWithViaLikeLower.transform(path).toString());
    }
    
    @Test
    public void ToContainsViaLike() {
        assertEquals("%abcDEF%", converters.toContainsViaLike.transform(constant).toString());
        assertEquals("% + string + %", converters.toContainsViaLike.transform(path).toString());
    }
    
    @Test
    public void ToContainsViaLikeLower() {
        assertEquals("%abcdef%", converters.toContainsViaLikeLower.transform(constant).toString());
        assertEquals("lower(% + string + %)", converters.toContainsViaLikeLower.transform(path).toString());
    }
}

