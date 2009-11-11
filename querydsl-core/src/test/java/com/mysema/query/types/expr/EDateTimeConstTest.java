/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class EDateTimeConstTest {
    
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
        
        EDateTime<Date> date = EDateTime.__create(cal.getTime());
        assertEquals("1",    date.getDayOfMonth().toString());
        assertEquals("1",    date.getMonth().toString());
        assertEquals("2000", date.getYear().toString());        
        assertEquals("7",    date.getDayOfWeek().toString());
        assertEquals("1",    date.getDayOfYear().toString());
        assertEquals("13",   date.getHour().toString());
        assertEquals("30",   date.getMinute().toString());
        assertEquals("12",   date.getSecond().toString());
        assertEquals("3",    date.getMilliSecond().toString());
    }

}
