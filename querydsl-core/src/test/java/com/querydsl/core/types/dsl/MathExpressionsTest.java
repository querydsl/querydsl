package com.querydsl.core.types.dsl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;


import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathExpressionsTest {

    /**
     * @throws ParseException
     * dummyTest_max() is testing if the MathExpressions.max() function can return larger value of two date
     */
    @Test
    public void dummyTest_max() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-31 00:00:00");
        java.util.Date date2 = format.parse("2014-02-31 00:00:00");
        java.util.Date date3 = format.parse("2018-09-31 00:00:00");
        java.util.Date date4 = format.parse("2020-12-30 00:00:00");

        java.util.Date result_max = MathExpressions.max(date1,date2);
        assertNotNull(result_max);
        assertTrue(result_max  == date1);
        java.util.Date result_max1 = MathExpressions.max(date3,date2);
        assertTrue(result_max1  == date3);
        java.util.Date result_max2 = MathExpressions.max(date3,date4);
        assertTrue(result_max2  == date4);


    }
    /**
     * @throws ParseException
     * dummyTest_min() is testing if the MathExpressions.max() function can return smaller value of two dates
     */
    @Test
    public void dummyTest_min() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-31 00:00:00");
        java.util.Date date2 = format.parse("2010-02-20 00:00:00");
        java.util.Date date3 = format.parse("2018-12-11 00:00:00");
        java.util.Date date4 = format.parse("1994-03-20 00:00:00");

        java.util.Date result_min = MathExpressions.min(date1,date2);
        assertNotNull(result_min);
        assertTrue(result_min  == date2);

        java.util.Date result_min1 = MathExpressions.min(date3,date2);
        assertNotNull(result_min1);
        assertTrue(result_min1  == date2);

        java.util.Date result_min2 = MathExpressions.min(date3,date4);
        assertNotNull(result_min2);
        assertTrue(result_min2  == date4);
    }
    /**
     * @throws ParseException
     * dummyTest() is testing MathExpressions.max() and MathExpressions.min() using same values
     * both functions can handle same inputs
     */
    @Test
    public void dummyTest() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-20 00:00:00");
        java.util.Date date2 = format.parse("2014-03-20 00:00:00");

        java.util.Date result_max = MathExpressions.max(date1,date2);
        java.util.Date result_min = MathExpressions.min(date1,date2);


        assertNotNull(result_max);
        assertNotNull(result_min);
        assertTrue(result_max.equals(result_min));
        assertTrue(result_min.equals(result_max));


    }

    /**
     * @throws ParseException
     * dummyNull() is testing MathExpressions.max() and MathExpressions.min() using null
     * both functions can handle input as null
     */
    @Test
    public void dummyNull() throws ParseException {

        java.util.Date date1 = null;
        java.util.Date date2 = null;

        java.util.Date result_max = MathExpressions.max(date1,date2);
        java.util.Date result_min = MathExpressions.min(date1,date2);

        assertNull(result_max);
        assertNull(result_min);

    }

}
