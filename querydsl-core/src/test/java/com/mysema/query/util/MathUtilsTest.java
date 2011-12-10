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
package com.mysema.query.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.util.MathUtils;

public class MathUtilsTest {

    @Test
    public void Sum() {
        assertEquals(Integer.valueOf(5), MathUtils.sum(2, 3.0));
    }

    @Test
    public void Difference() {
        assertEquals(Integer.valueOf(2), MathUtils.difference(5, 3.0));
    }

    @Test
    public void Cast_Integer_To_Long() {
        assertEquals(Long.valueOf(2), MathUtils.cast(2, Long.class));
    }

    @Test
    public void Cast_Double_To_Long() {
        assertEquals(Long.valueOf(3), MathUtils.cast(3.2, Long.class));
    }

}
