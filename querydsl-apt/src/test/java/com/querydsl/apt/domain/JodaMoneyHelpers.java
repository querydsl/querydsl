package com.querydsl.apt.domain;

import java.math.BigDecimal;

import org.joda.money.Money;
import org.joda.money.QMoney;

import com.querydsl.core.annotations.QueryDelegate;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

public class JodaMoneyHelpers {

    @QueryDelegate(Money.class)
    public static NumberExpression<BigDecimal> sum(QMoney money) {
        return Expressions.<BigDecimal>numberOperation(BigDecimal.class, Ops.AggOps.SUM_AGG, money);
    }

}
