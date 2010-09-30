/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import org.junit.Test;

import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.TimeExpression;

public class DateTimeTest extends AbstractQueryTest {

    @Test
    public void DateOperations() {
        // current_date(), current_time(), current_timestamp()
        assertToString("current_date()", DateExpression.currentDate());
        assertToString("current_date()", DateTimeExpression.currentDate());
        assertToString("current_time()", TimeExpression.currentTime());
        assertToString("current_timestamp()", DateTimeExpression.currentTimestamp());
        // second(...), minute(...), hour(...), day(...), month(...), year(...),
        catalog.effectiveDate.second();
        catalog.effectiveDate.minute();
        catalog.effectiveDate.hour();
        catalog.effectiveDate.dayOfMonth();
        catalog.effectiveDate.month();
        catalog.effectiveDate.year();
    }

}
