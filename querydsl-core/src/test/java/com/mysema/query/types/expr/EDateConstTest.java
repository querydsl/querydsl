package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Calendar;

import org.junit.Test;

public class EDateConstTest {
    
    @Test
    public void test(){
        // 1.1.2000
        Calendar cal = Calendar.getInstance();        
        cal.set(Calendar.DAY_OF_MONTH, 1); 
        cal.set(Calendar.MONTH, 0);        
        cal.set(Calendar.YEAR,  2000);    
        System.out.println(cal.getTime());
        
        EDate<Date> date = EDate.create(new Date(cal.getTimeInMillis()));
        assertEquals("1",   date.getDayOfMonth().toString());
        assertEquals("1",   date.getMonth().toString());
        assertEquals("2000",date.getYear().toString());        
        assertEquals("7", date.getDayOfWeek().toString());
        assertEquals("1", date.getDayOfYear().toString());
    }

}
