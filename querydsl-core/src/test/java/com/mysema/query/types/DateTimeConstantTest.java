/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.DateTimeConstant;

public class DateTimeConstantTest {

    @Test
    public void test(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.YEAR,  2000);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE,      30);
        cal.set(Calendar.SECOND,      12);
        cal.set(Calendar.MILLISECOND,  3);
        System.out.println(cal.getTime());

        DateTimeExpression<Date> date = DateTimeConstant.create(cal.getTime());
        assertEquals("1",    date.dayOfMonth().toString());
        assertEquals("1",    date.month().toString());
        assertEquals("2000", date.year().toString());
        assertEquals("7",    date.dayOfWeek().toString());
        assertEquals("1",    date.dayOfYear().toString());
        assertEquals("13",   date.hour().toString());
        assertEquals("30",   date.minute().toString());
        assertEquals("12",   date.second().toString());
        assertEquals("3",    date.milliSecond().toString());
    }

}
