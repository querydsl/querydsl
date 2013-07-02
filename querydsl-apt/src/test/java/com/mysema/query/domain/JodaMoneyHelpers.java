package com.mysema.query.domain;

import java.math.BigDecimal;

import org.joda.money.Money;
import org.joda.money.QMoney;

import com.mysema.query.annotations.QueryDelegate;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

public class JodaMoneyHelpers {

    @QueryDelegate(Money.class)
    public static NumberExpression<BigDecimal> sum(QMoney money) {
        return NumberOperation.<BigDecimal>create(BigDecimal.class, Ops.AggOps.SUM_AGG, money);
    }

}
