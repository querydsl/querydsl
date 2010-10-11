package com.mysema.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class MathUtilsTest {

    @Test
    public void Cast() {
        Integer value = Integer.valueOf(1);
        assertEquals(BigDecimal.class, MathUtils.cast(value, BigDecimal.class).getClass());
        assertEquals(BigInteger.class, MathUtils.cast(value, BigInteger.class).getClass());
        assertEquals(Double.class, MathUtils.cast(value, Double.class).getClass());
        assertEquals(Float.class, MathUtils.cast(value, Float.class).getClass());
        assertEquals(Integer.class, MathUtils.cast(value, Integer.class).getClass());
        assertEquals(Long.class, MathUtils.cast(value, Long.class).getClass());
        assertEquals(Short.class, MathUtils.cast(value, Short.class).getClass());
        assertEquals(Byte.class, MathUtils.cast(value, Byte.class).getClass());
    }

}
