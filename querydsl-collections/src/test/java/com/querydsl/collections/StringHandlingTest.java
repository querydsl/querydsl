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
package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.path.StringPath;

public class StringHandlingTest extends AbstractQueryTest {

    private List<String> data1 = Arrays.asList("petER", "THomas", "joHAN");

    private List<String> data2 = Arrays.asList("PETer", "thOMAS", "JOhan");

    private List<String> data = Arrays.asList("abc", "aBC", "def");

    private final StringPath a = new StringPath("a");

    private final StringPath b = new StringPath("b");

    @Test
    public void EqualsIgnoreCase() {
        Iterator<String> res = Arrays.asList("petER - PETer",
                "THomas - thOMAS", "joHAN - JOhan").iterator();
        for (Tuple arr : query()
                .from(a, data1)
                .from(b, data2)
                .where(a.equalsIgnoreCase(b)).list(a, b)) {
            assertEquals(res.next(), arr.get(a) + " - " + arr.get(b));
        }
    }

    @Test
    public void StartsWithIgnoreCase() {
        assertEquals(2, CollQueryFactory.from(a, data).where(a.startsWithIgnoreCase("AB")).count());
        assertEquals(2, CollQueryFactory.from(a, data).where(a.startsWithIgnoreCase("ab")).count());
    }

    @Test
    public void EndsWithIgnoreCase() {
        assertEquals(2, CollQueryFactory.from(a, data).where(a.endsWithIgnoreCase("BC")).count());
        assertEquals(2, CollQueryFactory.from(a, data).where(a.endsWithIgnoreCase("bc")).count());
    }

}
