package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

public class BigDecimalTest {

    @Test
    public void Arithmetic() {
        NumberPath<BigDecimal> num = Expressions.numberPath(BigDecimal.class, "num");
        CollQuery<?> query = CollQueryFactory.from(num, Arrays.asList(BigDecimal.ONE, BigDecimal.valueOf(2)));
        assertEquals(Arrays.asList(BigDecimal.valueOf(11), BigDecimal.valueOf(12)),
                query.select(num.add(BigDecimal.TEN)).fetch());
        assertEquals(Arrays.asList(BigDecimal.valueOf(-9), BigDecimal.valueOf(-8)),
                query.select(num.subtract(BigDecimal.TEN)).fetch());
        assertEquals(Arrays.asList(BigDecimal.valueOf(10), BigDecimal.valueOf(20)),
                query.select(num.multiply(BigDecimal.TEN)).fetch());
        assertEquals(Arrays.asList(new BigDecimal("0.1"), new BigDecimal("0.2")),
                query.select(num.divide(BigDecimal.TEN)).fetch());
    }

}
