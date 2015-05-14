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
package com.querydsl.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class QueryModifiersTest {

    @Test
    public void Limit() {
        QueryModifiers modifiers = QueryModifiers.limit(12L);
        assertEquals(Long.valueOf(12), modifiers.getLimit());
        assertNull(modifiers.getOffset());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void Offset() {
        QueryModifiers modifiers = QueryModifiers.offset(12L);
        assertEquals(Long.valueOf(12), modifiers.getOffset());
        assertNull(modifiers.getLimit());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void Both() {
        QueryModifiers modifiers = new QueryModifiers(1L,2L);
        assertEquals(Long.valueOf(1), modifiers.getLimit());
        assertEquals(Long.valueOf(2), modifiers.getOffset());
        assertTrue(modifiers.isRestricting());
    }

    @Test
    public void Empty() {
        QueryModifiers modifiers = new QueryModifiers(null, null);
        assertNull(modifiers.getLimit());
        assertNull(modifiers.getOffset());
        assertFalse(modifiers.isRestricting());
    }

    @Test
    public void HashCode() {
        QueryModifiers modifiers1 = new QueryModifiers(null, null);
        QueryModifiers modifiers2 = new QueryModifiers(1L, null);
        QueryModifiers modifiers3 = new QueryModifiers(null, 1L);

        assertEquals(modifiers1.hashCode(), QueryModifiers.EMPTY.hashCode());
        assertEquals(modifiers2.hashCode(), QueryModifiers.limit(1L).hashCode());
        assertEquals(modifiers3.hashCode(), QueryModifiers.offset(1L).hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalLimit() {
        QueryModifiers.limit(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalOffset() {
        QueryModifiers.offset(-1);
    }

    @Test
    public void SubList() {
        List<Integer> ints = Arrays.asList(1,2,3,4,5);
        assertEquals(Arrays.asList(3,4,5), QueryModifiers.offset(2).subList(ints));
        assertEquals(Arrays.asList(1,2,3), QueryModifiers.limit(3).subList(ints));
        assertEquals(Arrays.asList(2,3,4), new QueryModifiers(3L, 1L).subList(ints));
    }
}
