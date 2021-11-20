package com.querydsl.core.types.dsl;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.easymock.EasyMock;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathExpressionsTest {

    /**
     * @throws ParseException
     */
    @Test
    public void dummyTest_max() throws ParseException {
        ResultSet resultSet1 = EasyMock.createNiceMock(ResultSet.class);
        ResultSet resultSet2 = EasyMock.createNiceMock(ResultSet.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-31 00:00:00");
        java.util.Date date2 = format.parse("2014-02-31 00:00:00");
        EasyMock.replay(resultSet1);
        EasyMock.replay(resultSet2);
        EasyMock.verify(resultSet1);
        EasyMock.verify(resultSet2);

        Date result_max = (Date) MathExpressions.max(date1,date2);

        assertNotNull(result_max);
        assertTrue(result_max  == date1);


    }

    @Test
    public void dummyTest_min() throws ParseException {
        ResultSet resultSet1 = EasyMock.createNiceMock(ResultSet.class);
        ResultSet resultSet2 = EasyMock.createNiceMock(ResultSet.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-31 00:00:00");
        java.util.Date date2 = format.parse("2014-03-20 00:00:00");
        EasyMock.replay(resultSet1);
        EasyMock.replay(resultSet2);
        EasyMock.verify(resultSet1);
        EasyMock.verify(resultSet2);
        java.util.Date result_min = MathExpressions.min(date1,date2);

        assertNotNull(result_min);

        assertTrue(result_min  == date2);
    }

    @Test
    public void dummyTest2() throws ParseException {
        ResultSet resultSet1 = EasyMock.createNiceMock(ResultSet.class);
        ResultSet resultSet2 = EasyMock.createNiceMock(ResultSet.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date1 = format.parse("2014-03-31 00:00:00");
        java.util.Date date2 = format.parse("2014-03-20 00:00:00");
        EasyMock.replay(resultSet1);
        EasyMock.replay(resultSet2);
        EasyMock.verify(resultSet1);
        EasyMock.verify(resultSet2);

        java.util.Date result_max = MathExpressions.max(date1,date2);
        java.util.Date result_min = MathExpressions.min(date1,date2);

        assertNotNull(result_max);
        assertNotNull(result_min);
        assertTrue(result_max  == date1);
        assertTrue(result_min  == date2);
    }

}
