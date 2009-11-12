/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Calendar;

import org.junit.Test;

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
        assertEquals("13",   time.getHour().toString());
        assertEquals("30",   time.getMinute().toString());
        assertEquals("12",   time.getSecond().toString());
//        assertEquals("3",    time.getMilliSecond().toString());
    }

}
