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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;

public class StringHandlingTest extends AbstractQueryTest {

    private List<String> data1 = Arrays.asList("petER", "THomas", "joHAN");

    private List<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");

    private List<String> data = Arrays.asList("abc", "aBC", "def");

    private final StringPath a = Expressions.stringPath("a");

    private final StringPath b = Expressions.stringPath("b");

    @Test
    public void equalsIgnoreCase() {
        Iterator<String> res = Arrays.asList("petER - PETer",
                "THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Tuple arr : query()
                .from(a, data1)
                .from(b, data2)
                .where(a.equalsIgnoreCase(b)).select(a, b).fetch()) {
            assertEquals(res.next(), arr.get(a) + " - " + arr.get(b));
        }
    }

    @Test
    public void startsWithIgnoreCase() {
        assertEquals(2, CollQueryFactory.from(a, data).where(a.startsWithIgnoreCase("AB")).fetchCount());
        assertEquals(2, CollQueryFactory.from(a, data).where(a.startsWithIgnoreCase("ab")).fetchCount());
    }

    @Test
    public void endsWithIgnoreCase() {
        assertEquals(2, CollQueryFactory.from(a, data).where(a.endsWithIgnoreCase("BC")).fetchCount());
        assertEquals(2, CollQueryFactory.from(a, data).where(a.endsWithIgnoreCase("bc")).fetchCount());
    }

}
