/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;

import com.mysema.query.types.expr.DateExpression;

public class DateConstantTest {

    @Test
    public void test(){
        // 1.1.2000
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR,  2000);
        System.out.println(cal.getTime());

        DateExpression<Date> date = DateConstant.create(new Date(cal.getTimeInMillis()));
        assertEquals("1",   date.dayOfMonth().toString());
        assertEquals("1",   date.month().toString());
        assertEquals("2000",date.year().toString());
        assertEquals("7", date.dayOfWeek().toString());
        assertEquals("1", date.dayOfYear().toString());
    }

}
