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

import org.junit.Test;

import com.querydsl.core.types.path.StringPath;


public class ConvertersTest {

    private static final Converters converters = new Converters('\\');
    
    private static String constant = "abcDEF";
    
    private static StringPath path = new StringPath("string");
    
    @Test
    public void EscapeForLikeSpeed() {
        final int iterations = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            converters.escapeForLike("ab%cd_");
        }
        long duration = System.currentTimeMillis() - start;
        System.err.println(duration);
    }
    
    @Test
    public void ToLowerCase() {
        assertEquals("abcdef", converters.toLowerCase.apply(constant).toString());
        assertEquals("lower(string)", converters.toLowerCase.apply(path).toString());
    }
    
    @Test
    public void ToUpperCase() {
        assertEquals("ABCDEF", converters.toUpperCase.apply(constant).toString());
        assertEquals("upper(string)", converters.toUpperCase.apply(path).toString());
    }
    
    @Test
    public void ToStartsWithViaLike() {
        assertEquals("abcDEF%", converters.toStartsWithViaLike.apply(constant).toString());
        assertEquals("string + %", converters.toStartsWithViaLike.apply(path).toString());
    }
    
    @Test
    public void ToStartsWithViaLikeLower() {
        assertEquals("abcdef%", converters.toStartsWithViaLikeLower.apply(constant).toString());
        assertEquals("lower(string + %)", converters.toStartsWithViaLikeLower.apply(path).toString());
    }
    
    @Test
    public void ToEndsWithViaLike() {
        assertEquals("%abcDEF", converters.toEndsWithViaLike.apply(constant).toString());
        assertEquals("% + string", converters.toEndsWithViaLike.apply(path).toString());
    }
    
    @Test
    public void ToEndsWithViaLikeLower() {
        assertEquals("%abcdef", converters.toEndsWithViaLikeLower.apply(constant).toString());
        assertEquals("lower(% + string)", converters.toEndsWithViaLikeLower.apply(path).toString());
    }
    
    @Test
    public void ToContainsViaLike() {
        assertEquals("%abcDEF%", converters.toContainsViaLike.apply(constant).toString());
        assertEquals("% + string + %", converters.toContainsViaLike.apply(path).toString());
    }
    
    @Test
    public void ToContainsViaLikeLower() {
        assertEquals("%abcdef%", converters.toContainsViaLikeLower.apply(constant).toString());
        assertEquals("lower(% + string + %)", converters.toContainsViaLikeLower.apply(path).toString());
    }
}

