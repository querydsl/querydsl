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
import java.util.List;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

public class DistinctTest extends AbstractQueryTest {

    private NumberPath<Integer> intVar1 = Expressions.numberPath(Integer.class, "var1");
    private NumberPath<Integer> intVar2 = Expressions.numberPath(Integer.class, "var2");
    private List<Integer> list1 = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
    private List<Integer> list2 = Arrays.asList(2, 2, 3, 3, 3, 4, 4, 4, 4, 4);

    @Test
    public void singleSource() {
        assertEquals(list1, CollQueryFactory.from(intVar1, list1).fetch());
        assertEquals(Arrays.asList(1, 2, 3, 4), CollQueryFactory.from(intVar1, list1).distinct().fetch());
        assertEquals(Arrays.asList(2, 3, 4), CollQueryFactory.from(intVar2, list2).distinct().fetch());

        assertEquals(Arrays.asList(2, 3, 4), CollQueryFactory.from(intVar2, list2).distinct().fetch());
    }

    @Test
    public void bothSources() {
        assertEquals(100, CollQueryFactory.from(intVar1, list1).from(intVar2, list2).select(intVar1, intVar2).fetch().size());
        assertEquals(12, CollQueryFactory.from(intVar1, list1).from(intVar2, list2).distinct().select(intVar1, intVar2).fetch().size());

        assertEquals(12, CollQueryFactory.from(intVar1, list1).from(intVar2, list2).distinct().select(intVar1, intVar2).fetch().size());
    }

    @Test
    public void countDistinct() {
        assertEquals(10, CollQueryFactory.from(intVar1, list1).fetchCount());
        assertEquals(4, CollQueryFactory.from(intVar1, list1).distinct().fetchCount());
        assertEquals(3, CollQueryFactory.from(intVar2, list2).distinct().fetchCount());

        assertEquals(3, CollQueryFactory.from(intVar2, list2).distinct().fetchCount());
    }

    @Test
    public void null_() {
        assertEquals(Arrays.asList(null, 1),
                CollQueryFactory.from(intVar1, Arrays.asList(null, 1)).distinct().fetch());
    }

}
