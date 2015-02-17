package com.querydsl.sql.types;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

import org.easymock.EasyMock;
import org.junit.Test;

public class JSR310LocalTimeTypeTest extends AbstractJSR310DateTimeTypeTest<LocalTime> {

    public JSR310LocalTimeTypeTest() {
        super(new JSR310LocalTimeType());
    }

    @Test
    public void Set() throws SQLException {
        LocalTime value = LocalTime.now();
        Time time = Time.valueOf(value);

        PreparedStatement stmt = EasyMock.createNiceMock(PreparedStatement.class);
        stmt.setTime(1, time, UTC);
        EasyMock.replay(stmt);

        type.setValue(stmt, 1, value);
        EasyMock.verify(stmt);
    }

    @Test
    public void Get() throws SQLException {
        ResultSet resultSet = EasyMock.createNiceMock(ResultSet.class);
        EasyMock.expect(resultSet.getTime(1, UTC)).andReturn(new Time(UTC.getTimeInMillis()));
        EasyMock.replay(resultSet);

        LocalTime result = type.getValue(resultSet, 1);
        EasyMock.verify(resultSet);

        assertNotNull(result);
        assertTrue(result.toSecondOfDay() == 0);
    }
}
