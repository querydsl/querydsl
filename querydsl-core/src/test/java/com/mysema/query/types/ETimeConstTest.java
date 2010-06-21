/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Calendar;

import org.junit.Test;

import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.ETimeConst;

public class ETimeConstTest {

    @Test
    public void test(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE,      30);
        cal.set(Calendar.SECOND,      12);
        cal.set(Calendar.MILLISECOND,  3);
        System.out.println(cal.getTime());

        ETime<Time> time = ETimeConst.create(new Time(cal.getTimeInMillis()));
        assertEquals("13",   time.hour().toString());
        assertEquals("30",   time.minute().toString());
        assertEquals("12",   time.second().toString());
//        assertEquals("3",    time.getMilliSecond().toString());
    }

}
