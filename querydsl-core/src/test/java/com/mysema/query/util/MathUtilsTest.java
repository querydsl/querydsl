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
