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
package com.querydsl.collections;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class CollQueryFunctionsTest {

    @Test
    public void coalesce() {
        assertEquals("1", CollQueryFunctions.coalesce("1",null));
        assertEquals("1", CollQueryFunctions.coalesce(null,"1","2"));
        assertNull(CollQueryFunctions.coalesce(null,null));
    }

    @Test
    @Ignore
    public void likeSpeed() {
        // 3015
        final int iterations = 1000000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < iterations; i++) {
            CollQueryFunctions.like("abcDOG", "%DOG");
            CollQueryFunctions.like("DOGabc", "DOG%");
            CollQueryFunctions.like("abcDOGabc", "%DOG%");
        }
        long duration = System.currentTimeMillis() - start;
        System.err.println(duration);
    }

    @Test
    public void like() {
        assertTrue(CollQueryFunctions.like("abcDOG", "%DOG"));
        assertTrue(CollQueryFunctions.like("DOGabc", "DOG%"));
        assertTrue(CollQueryFunctions.like("abcDOGabc", "%DOG%"));
    }

    @Test
    public void like_with_special_chars() {
        assertTrue(CollQueryFunctions.like("$DOG", "$DOG"));
        assertTrue(CollQueryFunctions.like("$DOGabc", "$DOG%"));
    }
}
