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
package com.querydsl.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void sum() {
        assertEquals(Integer.valueOf(5), MathUtils.sum(2, 3.0));
    }

    @Test
    public void difference() {
        assertEquals(Integer.valueOf(2), MathUtils.difference(5, 3.0));
    }

    @Test
    public void cast_returns_correct_type() {
        checkCast(1, BigDecimal.class);
        checkCast(1, BigInteger.class);
        checkCast(1, Double.class);
        checkCast(1, Float.class);
        checkCast(1, Integer.class);
        checkCast(1, Long.class);
        checkCast(1, Short.class);
        checkCast(1, Byte.class);
    }

    @Test
    public void cast_returns_argument_as_is_when_compatible() {
        checkSame(BigDecimal.ONE, BigDecimal.class);
        checkSame(BigInteger.ONE, BigInteger.class);
        checkSame((double) 1, Double.class);
        checkSame((float) 1, Float.class);
        checkSame(1, Integer.class);
        checkSame((long) 1, Long.class);
        checkSame((short) 1, Short.class);
        checkSame((byte) 1, Byte.class);
    }

    private static void checkCast(Number value, Class<? extends Number> targetClass) {
        Number target = MathUtils.cast(value, targetClass);
        assertSame(targetClass, target.getClass());
    }

    private static <N extends Number> void checkSame(N value, Class<N> targetClass) {
        N target = MathUtils.cast(value, targetClass);
        assertSame(value, target);
    }

}
