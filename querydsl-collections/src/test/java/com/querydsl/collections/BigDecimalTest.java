package com.querydsl.collections;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import com.querydsl.core.types.path.NumberPath;

public class BigDecimalTest {

    @Test
    public void Arithmetic() {
        NumberPath<BigDecimal> num = new NumberPath<BigDecimal>(BigDecimal.class, "num");
        CollQuery query = CollQueryFactory.from(num, Arrays.asList(BigDecimal.ONE, BigDecimal.ONE));
        query.list(num.add(BigDecimal.ONE));
        query.list(num.subtract(BigDecimal.ONE));
        query.list(num.multiply(BigDecimal.ONE));
        query.list(num.divide(BigDecimal.ONE));
    }
    
}
