package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

public class JSR310LocalDateTypeTest extends AbstractJSR310DateTimeTypeTest<LocalDate> {

    public JSR310LocalDateTypeTest() {
        super(new JSR310LocalDateType());
    }

    @Test
    public void set() throws SQLException {
        LocalDate value = LocalDate.now();
        Date date = new Date(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setDate(1, date, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        LocalDate value = LocalDate.now();
        Date date = new Date(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setDate(1, date, UTC);
        EasyMock.replay(stmt);

        new LocalDateType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getDate(1, UTC)).andReturn(new Date(UTC.getTimeInMillis()));
        EasyMock.replay(resultSet);

        LocalDate result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toEpochDay() == 0);
    }
    /**
     * Unit Test for issue #3001
     * JSR310LocalDateType produces incorrect value for 2014-03-31
     * dummyTest1 is testing the getValue() method with a result set includes 2014/03/31
     * the getDayOfMonth() method in dummyTest1 should return 31, but it returns 30 which shown as a bug
     * dummyTest2 is testing the getValue() method with a result set doesn't include 2014/03/31
     * the getDayOfMonth() method in dummyTest2 return the value correctly
     * dummyTestNull is testing the getValue() method with Null, which returns Null
     */

    @Test
    public void dummyTest1() throws SQLException, ParseException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //format.setTimeZone(TimeZone.getTimeZone("UTC"));

        java.util.Date date = format.parse("2014-03-31 00:00:00");
        long millis = date.getTime();

        EasyMock.expect(resultSet.getDate(1, UTC)).andReturn(new Date(millis));


        EasyMock.replay(resultSet);

        @Nullable LocalDate result = type.getValue(resultSet, 1);
        System.out.println(result);

        EasyMock.verify(resultSet);

        assertNotNull(result);
        //Test for value of month
        assertTrue(result.getMonthValue() == 3);
        //Test for day of month, excepted 31, got 30
        assertTrue(result.getDayOfMonth() == 31);
    }


}
