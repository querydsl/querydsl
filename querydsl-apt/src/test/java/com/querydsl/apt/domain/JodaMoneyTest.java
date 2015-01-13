package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.joda.money.QMoney;
import org.junit.Test;

import com.querydsl.core.types.expr.NumberExpression;

public class JodaMoneyTest {

    @Test
    public void test() {
        NumberExpression<BigDecimal> sum = QMoney.money.sum();
        assertNotNull(sum);
    }
}
