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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NullSafeComparableComparatorTest {

    private final NullSafeComparableComparator<String> comparator = new NullSafeComparableComparator<String>();

    @Test
    public void Null_Before_Object() {
        assertTrue(comparator.compare(null, "X") < 0);
    }

    @Test
    public void Object_After_Null() {
        assertTrue(comparator.compare("X", null) > 0);
    }

    @Test
    public void Object_Eq_Object() {
        assertEquals(0, comparator.compare("X", "X"));
    }

    @Test
    public void Object_Lt_Object() {
        assertTrue(comparator.compare("X", "Y") < 0);
    }

    @Test
    public void Object_Gt_Object() {
        assertTrue(comparator.compare("Z", "Y") > 0);
    }

}
