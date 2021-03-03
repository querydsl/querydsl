package com.querydsl.collections;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

public class NumberTest {

    @Test
    public void sum() throws Exception {
        NumberPath<BigDecimal> num = Expressions.numberPath(BigDecimal.class, "num");
        CollQuery<BigDecimal> query = CollQueryFactory.<BigDecimal> from(num, Arrays.<BigDecimal> asList(new BigDecimal("1.6"), new BigDecimal("1.3")));

        assertEquals(new BigDecimal("2.9"), query.<BigDecimal> select(num.sum()).fetchOne());
    }
}
