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
    public void CurrentDate() {
        assertToString("current_date()", DateExpression.currentDate());
    }
    
    @Test
    public void CurrentDate2(){
        assertToString("current_date()", DateTimeExpression.currentDate());
    }
    
    @Test
    public void CurrentTime(){
        assertToString("current_time()", TimeExpression.currentTime());   
    }
    
    @Test
    public void CurrentTimestamp(){
        assertToString("current_timestamp()", DateTimeExpression.currentTimestamp());
    }
    
    @Test
    public void DateOperations2() {
        catalog.effectiveDate.second();
        catalog.effectiveDate.minute();
        catalog.effectiveDate.hour();
        catalog.effectiveDate.dayOfMonth();
        catalog.effectiveDate.month();
        catalog.effectiveDate.year();
    }

}
