package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import org.easymock.EasyMock;
import org.junit.Test;

public class JSR310LocalTimeTypeTest extends AbstractJSR310DateTimeTypeTest<LocalTime> {

    public JSR310LocalTimeTypeTest() {
        super(new JSR310LocalTimeType());
    }

    @Test
    public void set() throws SQLException {
        LocalTime value = LocalTime.now();

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setObject(1, value);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void jodaSet() throws SQLException {
        LocalTime value = LocalTime.now();
        Time time = new Time(value.get(ChronoField.MILLI_OF_DAY));

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTime(1, time, UTC);
        EasyMock.replay(stmt);

        new LocalTimeType().setValue(stmt, 1, toJoda(value));
        EasyMock.verify(stmt);
    }

    @Test
    public void get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getObject(1, LocalTime.class)).andReturn(LocalDateTime.ofInstant(UTC.toInstant(),
                UTC.getTimeZone().toZoneId()).toLocalTime());
        EasyMock.replay(resultSet);

        LocalTime result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.getSecond() == 0);
    }
}
